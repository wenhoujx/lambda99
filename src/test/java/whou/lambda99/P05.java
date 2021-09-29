package whou.lambda99;

import com.jnape.palatable.lambda.functions.builtin.fn1.Reverse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class P05 {
    private <T> Iterable<T> reverse(Iterable<T> as) {
//         FoldRight.<T, List<T>>foldRight(
//                (element, acc) -> acc.fmap(Cons.cons(element)),
//                Lazy.lazy(List.of()),
//                 as
//        );
        return Reverse.reverse(as);
    }

    @Test
    void test() {
        Assertions.assertThat(reverse(List.of(1,2,3))).containsExactly(3,2,1);
    }
}
