package whou.lambda99;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.Last;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 99 problems: https://www.ic.unicamp.br/~meidanis/courses/mc336/2006s2/funcional/L-99_Ninety-Nine_Lisp_Problems.html
 */
public class P01 {
    private static <T> Maybe<T> solution(Iterable<T> as) {
        return Last.last(as);
    }

    @Test
    void test() {
        assertThat(solution(List.of(1,2,3))).isEqualTo(Maybe.maybe(3));
        assertThat(solution(List.of())).isEqualTo(Maybe.nothing());
    }
}
