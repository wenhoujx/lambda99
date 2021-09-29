package whou.lambda99;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse;

public class P09 {
    private static <T> Iterable<Iterable<T>> pack(Iterable<T> ts) {
        FoldLeft.foldLeft(
                (acc, element) -> IfThenElse<T, >
        )
    }
}
