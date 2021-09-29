package whou.lambda99;

import com.jnape.palatable.lambda.functions.builtin.fn1.Reverse;
import com.jnape.palatable.lambda.functions.builtin.fn2.Take;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class P02 {
    private static <T> Iterable<T> solution(Iterable<T> as) {
        return Reverse.<T>reverse().fmap(Take.take(2)).fmap(Reverse.reverse()).apply(as);
    }

    @Test
    void test() {
        assertThat(solution(List.of(1,2,3))).containsExactly(2,3);
        assertThat(solution(List.of(1,2))).containsExactly(1,2);
        assertThat(solution(List.of(1))).containsExactly(1);
    }
}
