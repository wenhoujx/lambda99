package whou.lambda99;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class P04 {
    private static <T> long size(Iterable<T> as) {
        // do not use Size.Size
        return FoldRight.foldRight((_unused, currentSize) -> currentSize.fmap(x -> x+1 ), Lazy.lazy(0L), as)
                .value();
    }

    @Test
    void test() {
        Assertions.assertThat(size(List.of())).isEqualTo(0L);
        Assertions.assertThat(size(List.of(1))).isEqualTo(1L);
    }
}
