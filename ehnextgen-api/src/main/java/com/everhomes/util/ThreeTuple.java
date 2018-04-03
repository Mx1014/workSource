// @formatter:off
package com.everhomes.util;

/**
 * Created by xq.tian on 2016/9/8.
 */
public class ThreeTuple<A, B, C> extends Tuple<A, B> {

    private C third;

    public ThreeTuple(A first, B second) {
        super(first, second);
    }

    private ThreeTuple(A first, B second, C third) {
        super(first, second);
        this.third = third;
    }

    public C third() {
        return this.third;
    }

    public static <A, B, C> ThreeTuple<A, B, C> create(A first, B second, C third) {
        return new ThreeTuple<>(first, second, third);
    }
}
