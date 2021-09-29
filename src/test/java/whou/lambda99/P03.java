package whou.lambda99;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.Head;
import com.jnape.palatable.lambda.functions.builtin.fn2.Drop;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class P03 {
    private static <T> Maybe<T> findKth(Iterable<T> as, int k) {
        if (k < 1){
            return Maybe.nothing();
        }

        return Drop.<T>drop(k-1).fmap(Head.head()).apply(as);
    }

    @Test
    void test() {
        assertThat(findKth(List.of(1,2,3),2)).isEqualTo(Maybe.maybe(2));
        assertThat(findKth(List.of(1,2,3), 4)).isEqualTo(Maybe.nothing());
        assertThat(findKth(List.of(1,2,3), -1)).isEqualTo(Maybe.nothing());
    }
}
