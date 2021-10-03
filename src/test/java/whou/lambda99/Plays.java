package whou.lambda99;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import com.jnape.palatable.lambda.functions.builtin.fn2.Into;
import com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr;
import com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse;
import com.jnape.palatable.lambda.io.IO;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * This class exists for me to play with lambda FP concepts.
 */
public class Plays {
    @Test
    void testZip() {
        Assertions.assertThat(
                Maybe.just(1).zip(Maybe.just(Fn1.fn1(x -> x + 1)))).isEqualTo(Maybe.just(2));
    }

    private static IO<Iterable<Boolean>> whetherToKeep(long numOfBooleans, long trueBooleans) {
        return IO.io(Fn0.fn0(Random::new))
                .flatMap(random -> IO.io(() -> Unfoldr.unfoldr(Into.into((truesRemaining, sizeRemaining) -> {
                    if (truesRemaining <= 0L) {
                        return Maybe.nothing();
                    }

                    return IO.io(random::nextLong)
                            .fmap(j -> Math.abs(j) % sizeRemaining)
                            .fmap(i -> {
                                Fn1<Long, Maybe<Tuple2<Boolean, Tuple2<Long, Long>>>> longMaybeFn1 = IfThenElse.ifThenElse(k -> k < truesRemaining,
                                        Constantly.constantly(Maybe.just(Tuple2.tuple(true, Tuple2.tuple(truesRemaining - 1, sizeRemaining - 1)))),
                                        Constantly.constantly(Maybe.just(Tuple2.tuple(false, Tuple2.tuple(truesRemaining, sizeRemaining - 1)))));
                                return longMaybeFn1.apply(i);
                            })
                            .unsafePerformIO();
                }), Tuple2.tuple(trueBooleans, numOfBooleans))));
    }

    @Test
    void testRandom() {
        Iterable<Boolean> booleans = whetherToKeep(2, 1).unsafePerformIO();
        System.out.println(Lists.newArrayList(booleans));
        Fn1<Long, Integer> mod2 = j -> Math.toIntExact(Math.abs(j) % 2);
        Fn1<Random, IO<Integer>> randomInt = random -> IO.io(random::nextLong).fmap(mod2);
        System.out.println(IO.io(Fn0.fn0(Random::new)).flatMap(randomInt).unsafePerformIO());
    }
}
