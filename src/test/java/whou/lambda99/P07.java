package whou.lambda99;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Flatten;
import com.jnape.palatable.lambda.functions.builtin.fn1.Uncons;
import com.jnape.palatable.lambda.functions.builtin.fn2.Iterate;
import com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse;
import com.jnape.palatable.lambda.monoid.builtin.Concat;

import java.util.List;

public class P07 {
    private <T> Iterable<T> flatten(Iterable<T> ts) {
        // TODO(whou): do it.
//        Maybe<Tuple2<T, Iterable<T>>> headAndTail = Uncons.uncons(ts);
//        headAndTail.fmap(tuple -> tuple.into((head, tail) -> Concat.concat(
//                IfThenElse.<Iterable<T>>ifThenElse(h -> h instanceof Iterable, h -> flatten((Iterable<? super T>) h), List::of, head),
//                flatten(tail)
//        ))).orElse(List.of());
        return null;
    }
}
