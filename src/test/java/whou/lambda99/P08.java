package whou.lambda99;

import com.jnape.palatable.lambda.functions.builtin.fn1.Head;
import com.jnape.palatable.lambda.functions.builtin.fn1.Last;
import com.jnape.palatable.lambda.functions.builtin.fn2.Snoc;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class P08 {
    private static <T> Iterable<T> dedup(Iterable<T> ts) {
        return FoldLeft.<T, Iterable<T>>foldLeft((acc, element) ->
                        IfThenElse.ifThenElse(ele -> Last.last(acc).fmap(ele::equals).orElse(false),
                                ele -> acc,
                                ele -> Snoc.snoc(ele, acc),
                                element ),
                List.of(), ts);
    }

    @Test
    void test() {
        Assertions.assertThat(dedup(List.of('a', 'a', 'b', 'c', 'c'))).containsExactly('a', 'b', 'c');
    }
}
