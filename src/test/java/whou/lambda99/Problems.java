package whou.lambda99;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn1.*;
import com.jnape.palatable.lambda.functions.builtin.fn2.*;
import com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.recursion.Trampoline;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import com.jnape.palatable.lambda.monoid.builtin.Concat;
import com.jnape.palatable.lambda.semigroup.builtin.Max;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

public class Problems {
    private static <T> Maybe<T> p01(Iterable<T> as) {
        return Last.last(as);
    }

    @Test
    void test01() {
        assertThat(p01(List.of(1,2,3))).isEqualTo(Maybe.maybe(3));
        assertThat(p01(List.of())).isEqualTo(Maybe.nothing());
    }

    private static <T> Iterable<T> p02(Iterable<T> as) {
        return Reverse.<T>reverse().fmap(Take.take(2)).fmap(Reverse.reverse()).apply(as);
    }

    @Test
    void test02() {
        assertThat(p02(List.of(1,2,3))).containsExactly(2,3);
        assertThat(p02(List.of(1,2))).containsExactly(1,2);
        assertThat(p02(List.of(1))).containsExactly(1);
    }

    private static <T> Maybe<T> p03(Iterable<T> as, int k) {
        if (k < 1){
            return Maybe.nothing();
        }

        return Drop.<T>drop(k-1).fmap(Head.head()).apply(as);
    }

    @Test
    void test03() {
        assertThat(p03(List.of(1,2,3),2)).isEqualTo(Maybe.maybe(2));
        assertThat(p03(List.of(1,2,3), 4)).isEqualTo(Maybe.nothing());
        assertThat(p03(List.of(1,2,3), -1)).isEqualTo(Maybe.nothing());
    }

    private static <T> long p04(Iterable<T> as) {
        // do not use Size.Size
        return FoldRight.foldRight((_unused, currentSize) -> currentSize.fmap(x -> x+1 ), Lazy.lazy(0L), as)
                .value();
    }

    @Test
    void test04() {
        Assertions.assertThat(p04(List.of())).isEqualTo(0L);
        Assertions.assertThat(p04(List.of(1))).isEqualTo(1L);
    }

    private <T> Iterable<T> p05(Iterable<T> as) {
//         FoldRight.<T, List<T>>foldRight(
//                (element, acc) -> acc.fmap(Cons.cons(element)),
//                Lazy.lazy(List.of()),
//                 as
//        );
        return Reverse.reverse(as);
    }

    @Test
    void test05() {
        Assertions.assertThat(p05(List.of(1,2,3))).containsExactly(3,2,1);
    }

    private <T> boolean p06(Iterable<T> ts) {
        return All.all(tuple -> tuple.into(Object::equals), Zip.zip(ts, Reverse.reverse(ts)));
    }

    @Test
    void test06() {
        Assertions.assertThat(p06(List.of(1,2,3,2,1))).isTrue();
        Assertions.assertThat(p06(List.of())).isTrue();
        Assertions.assertThat(p06(List.of(1,2,3,2,2))).isFalse();
    }

    private <T> Iterable<T> p07(Iterable<?> ts) {
        return (Iterable<T>) Uncons.uncons(ts).match(
                _nothing -> List.of(),
                Into.into((head, rest) -> Concat.concat(
                        flattenOne(head),
                        flatten(rest)
                ))
        );
    }

    private <T> Iterable<T> flattenOne(T head) {
        return head instanceof Iterable? flatten((Iterable<?>) head) : List.of(head);

    }

    @Test
    void test07() {
        assertThat(p07(List.of(1,2,3))).containsExactly(1,2,3);
        assertThat(p07(List.of(1,List.of(2,3)))).containsExactly(1,2,3);
        assertThat(p07(List.of(List.of(List.of(1)),List.of(2,3)))).containsExactly(1,2,3);
    }

    private static <T> Iterable<T> p08(Iterable<T> ts) {
        return FoldLeft.<T, Iterable<T>>foldLeft((acc, element) ->
                Last.last(acc).filter(element::equals).match(
                        _different -> Snoc.snoc(element, acc),
                        _same -> acc
                ),
                List.of(), ts);
    }

    @Test
    void test08() {
        Assertions.assertThat(p08(List.of('a', 'a', 'b', 'c', 'c'))).containsExactly('a', 'b', 'c');
    }


    private static <T> Iterable<Iterable<T>> p09(Iterable<T> ts) {
        Fn1<Iterable<T>, Maybe<Tuple2<Iterable<T>, Iterable<T>>>> unfold = xs -> IfThenElse.ifThenElse(
                Eq.eq(0),
                len -> Maybe.nothing(),
                len -> Maybe.just(Both.both(Take.take(len), Drop.drop(len), xs)),
                consecutiveRunLength(xs)
        );
        return Unfoldr.unfoldr(unfold, ts);

    }

    /**
     * Finds the consecutive running sequence starting from head.
     * <p>
     * e.g. List.of(1,1,2,..) -> 2
     */
    private static <T> int consecutiveRunLength(Iterable<T> ts) {
        return Uncons.uncons(ts).fmap(Into.into((head, tail) -> TakeWhile.takeWhile(element -> element.equals(head), tail)))
                .fmap(Size.size())
                .fmap(Long::intValue)
                // to compensate head element.
                .fmap(x -> x + 1)
                .orElse(0);


    }

    @Test
    void test09() {
        assertThat(consecutiveRunLength(List.of(1, 1, 1, 2))).isEqualTo(3);
        Iterable<Iterable<Integer>> output = p09(List.of(1, 1, 1, 2, 2, 1, 1, 3));
        Iterator<Iterable<Integer>> iter = output.iterator();
        // sorry, i don't know a better way to assert this :(
        assertThat(iter.next()).containsExactly(1, 1, 1);
        assertThat(iter.next()).containsExactly(2, 2);
        assertThat(iter.next()).containsExactly(1, 1);
        assertThat(iter.next()).containsExactly(3);
        assertThat(iter.hasNext()).isFalse();

    }

    private static <T> Iterable<Tuple2<Integer, T>> p10(Iterable<T> ts) {
        Iterable<Iterable<T>> previousResult = p09(ts);
        Fn1<Iterable<T>, Maybe<Tuple2<Integer, T>>> transform = xs -> Head.head(xs).fmap(head -> Tuple2.tuple(Size.size(xs).intValue(), head));
        return Map.map(transform).fmap(CatMaybes.catMaybes()).apply(previousResult);
    }

    @Test
    void test10() {
        List<Character> ts = List.of('a', 'a', 'a', 'b', 'b', 'c');
        assertThat(p10(ts)).containsExactly(Tuple2.tuple(3, 'a'), Tuple2.tuple(2, 'b'), Tuple2.tuple(1, 'c'));
    }

    private static <T> Iterable<Choice2<Tuple2<Integer, T>, T>> p11(Iterable<T> ts) {
        Fn1<Tuple2<Integer, T>, Choice2<Tuple2<Integer, T>, T>> transform = tuple -> tuple.into((count, t) -> count == 1 ?
                Choice2.b(t) : Choice2.a(tuple));

        return Fn1.<Iterable<T>, Iterable<Tuple2<Integer, T>>>fn1(Problems::p10).fmap(Map.map(transform)).apply(ts);
    }

    @Test
    void test11() {
        List<Character> ts = List.of('a', 'a', 'a', 'b', 'b', 'c');
        assertThat(p11(ts)).containsExactly(Choice2.a(Tuple2.tuple(3, 'a')), Choice2.a(Tuple2.tuple(2, 'b')), Choice2.b('c'));
    }


    private static <T> Iterable<T> p12(Iterable<Choice2<Tuple2<Integer, T>, T>> encoded) {
        Fn1<Choice2<Tuple2<Integer, T>, T>, Iterable<T>> decodeTransform = choice -> choice.match(Into.into((count, t) -> Take.take(count, Repeat.repeat(t))),
                Collections::singletonList);

        return Map.map(decodeTransform).fmap(Flatten.flatten()).apply(encoded);
    }

    @Test
    void test12() {
        List<Character> ts = List.of('a', 'a', 'a', 'b', 'b', 'c');
        assertThat(p12(p11(ts))).containsExactlyElementsOf(ts);
        assertThat(p12(p11(List.of()))).isEmpty();
    }

    private static <T> Iterable<Tuple2<Integer, T>> p13(Iterable<T> ts) {
        Fn1<Iterable<T>, Maybe<Tuple2<Tuple2<Integer, T>, Iterable<T>>>> split = xs -> IfThenElse.ifThenElse(
                Eq.eq(0),
                _empty -> Maybe.<Tuple2<Tuple2<Integer, T>, Iterable<T>>>nothing(),
                groupLen -> Maybe.just(Tuple2.tuple(Tuple2.tuple(groupLen, Head.head(xs).orElseThrow(() -> {
                            throw new RuntimeException("longer than 1");
                        })),
                        Drop.drop(groupLen, xs))),
                consecutiveRunLength(xs)
        );

        return Unfoldr.unfoldr(split, ts);
    }

    @Test
    void test13() {
        List<Character> ts = List.of('a', 'a', 'a', 'b', 'b', 'c');
        assertThat(p13(ts)).containsExactly(Tuple2.tuple(3, 'a'), Tuple2.tuple(2, 'b'), Tuple2.tuple(1, 'c'));
    }

    private static <T> Iterable<T> p14(Iterable<T> ts) {
        Fn1<Iterable<T>, Iterable<T>> dup = Map.<T, Iterable<T>>map(t -> Take.take(2, Repeat.repeat(t))).fmap(Flatten.flatten());
        return dup.apply(ts);
    }

    @Test
    void test14() {
        assertThat(p14(List.of('a', 'b', 'c'))).containsExactly('a', 'a', 'b', 'b', 'c', 'c');
    }

    private static <T> Iterable<T> p15(Iterable<T> ts, int k) {
        return Map.<T, Iterable<T>>map(t -> Take.take(k, Repeat.repeat(t))).fmap(Flatten.flatten())
                .apply(ts);
    }

    @Test
    void test15() {
        assertThat(p15(List.of('a', 'b'), 3)).containsExactly('a', 'a', 'a', 'b', 'b', 'b');
    }

    private static <T> Iterable<T> p16(Iterable<T> ts, int n) {
        if (n <= 0) {
            return ts;
        }
        Fn1<Iterable<T>, Iterable<Tuple2<T, Integer>>> indexed = xs -> Zip.zip(xs, Iterate.iterate(i -> i + 1, 1));
        Predicate<Tuple2<T, Integer>> filter = tuple -> tuple._2() % n != 0;
        return indexed.fmap(Filter.filter(filter)).fmap(Map.map(Tuple2::_1)).apply(ts);
    }

    @Test
    void test16() {
        assertThat(p16(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"), 3)).containsExactly("a", "b", "d", "e", "g", "h", "j", "k");
        assertThat(p16(Arrays.asList("a", "b"), 3)).containsExactly("a", "b");
        assertThat(p16(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"), 0)).containsExactly("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");
    }

    private static <T> Tuple2<Iterable<T>, Iterable<T>> p17(Iterable<T> ts, int len) {
        return Both.both(Take.<T>take(len), Drop.<T>drop(len)).apply(ts);
    }

    @Test
    void test17() {
        Tuple2<Iterable<String>, Iterable<String>> output = p17(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "k"), 3);
        assertThat(output._1()).containsExactly("a", "b", "c");
        assertThat(output._2()).containsExactly("d", "e", "f", "g", "h", "i", "k");
    }

    private static <T> Iterable<T> p18(Iterable<T> ts, int fromInclusive, int toInclusive) {
        return Drop.<T>drop(fromInclusive - 1).fmap(Take.take(toInclusive - fromInclusive + 1)).apply(ts);
    }

    @Test
    void test18() {
        assertThat(p18(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "k"), 3, 7)).contains(
                "c", "d", "e", "f", "g"
        );
    }

    private static <T> Iterable<T> p19(Iterable<T> ts, int rotateLen) {
        Fn1<Maybe<T>, Iterable<T>> streamMaybe = Fn1.<Maybe<T>, Iterable<Maybe<T>>>fn1(Collections::singletonList).fmap(CatMaybes.catMaybes());

        Fn1<Iterable<T>, Iterable<T>> rotateLeft1 = Both.both(Head.<T>head().fmap(streamMaybe), Tail.tail())
                .fmap(Into.into((iterA, iterB) -> Concat.concat(iterB, iterA)));

        Fn1<Iterable<T>, Iterable<T>> rotateRight1 = Both.both(Init.init(),
                        Last.<T>last().fmap(streamMaybe))
                .fmap(Into.into((iterA, iterB) -> Concat.concat(iterB, iterA)));

        Fn1<Boolean, Fn1<Iterable<T>, Iterable<T>>> rotate = left -> left ? rotateLeft1 : rotateRight1;

        // moveToZero(0) -> 0
        Fn1<Integer, Integer> moveToZero = i -> IfThenElse.ifThenElse(
                j -> j >= 0,
                j -> Max.max(0, j - 1),
                j -> (j + 1),
                i);

        Fn1<java.util.Map.Entry<Integer, Iterable<T>>, RecursiveResult<Tuple2<Integer, Iterable<T>>, Iterable<T>>> recurseFunction =
                Into.into((count, xs) -> count != 0 ? RecursiveResult.recurse(Tuple2.tuple(
                        moveToZero.apply(count),
                        rotate.apply(count > 0).apply(xs)))
                        : RecursiveResult.terminate(xs));

        return Trampoline.trampoline(recurseFunction, Tuple2.tuple(rotateLen, ts));
    }

    @Test
    void test19() {
        assertThat(p19(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"), 3)).containsExactly("d", "e", "f", "g", "h", "a", "b", "c");
        assertThat(p19(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"), 0)).containsExactly("a", "b", "c", "d", "e", "f", "g", "h");
        assertThat(p19(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"), -2)).containsExactly("g", "h", "a", "b", "c", "d", "e", "f");
    }

    private static <T> Tuple2<Iterable<T>, Maybe<T>> p20(Iterable<T> ts, int k) {
        Fn2<Integer, T, Choice2<T, T>> partitionFunc = (i, t) -> i == k ? Choice2.b(t) : Choice2.a(t);
        return Zip.<Integer, T>zip(Iterate.iterate(x -> x + 1, 1)).fmap(Partition.partition(Into.into(partitionFunc)))
                .fmap(tuple -> tuple.biMapR(Head.<T>head()))
                .apply(ts);
    }

    @Test
    void test20() {
        Tuple2<Iterable<String>, Maybe<String>> output = p20(Arrays.asList("a", "b", "c", "d"), 2);
        assertThat(output._1()).containsExactly("a", "c", "d");
        assertThat(output._2()).isEqualTo(Maybe.just("b"));
    }

    private static <T> Iterable<T> p21(Iterable<T> ts, T t, int k) {
        Fn3<Iterable<T>, T, Iterable<T>, Iterable<T>> concat3 = (firstPart, element, secondPart) -> Flatten.<T>flatten().apply(
                List.of(firstPart, Collections.singleton(element), secondPart)
        );
        return Both.both(Take.<T>take(k - 1), Drop.<T>drop(k - 1)).fmap(tuple -> concat3.apply(tuple._1(), t, tuple._2())).apply(ts);
    }

    @Test
    void test21() {
        assertThat(p21(List.of("a", "b", "c", "d"), "alfa", 2)).containsExactly("a", "alfa", "b", "c", "d");
        assertThat(p21(List.of("a", "b", "c", "d"), "alfa", 1)).containsExactly("alfa", "a", "b", "c", "d");
        assertThat(p21(List.of("a", "b", "c", "d"), "alfa", 5)).containsExactly("a", "b", "c", "d", "alfa");
    }

    private static Iterable<Integer> p22(int fromInclusive, int toInclusive) {
return         TakeWhile.takeWhile(LTE.lte(toInclusive), Iterate.iterate(x -> x+1, fromInclusive));

    }

    @Test
    void test22() {
        assertThat(p22(4, 9)).containsExactly(4, 5, 6, 7, 8, 9);
    }


}
