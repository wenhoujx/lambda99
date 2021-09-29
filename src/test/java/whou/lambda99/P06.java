package whou.lambda99;

import com.jnape.palatable.lambda.functions.builtin.fn1.Reverse;
import com.jnape.palatable.lambda.functions.builtin.fn2.All;
import com.jnape.palatable.lambda.functions.builtin.fn2.Zip;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class P06 {
    private <T> boolean palindrome(Iterable<T> ts) {
        return All.all(tuple -> tuple.into(Object::equals), Zip.zip(ts, Reverse.reverse(ts)));
    }

    @Test
    void test() {
        Assertions.assertThat(palindrome(List.of(1,2,3,2,1))).isTrue();
        Assertions.assertThat(palindrome(List.of())).isTrue();
        Assertions.assertThat(palindrome(List.of(1,2,3,2,2))).isFalse();
    }
}
