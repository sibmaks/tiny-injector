package com.github.sibmaks.ti.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author drobyshev-ma
 * Created at 21-08-2021
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Pair<L, R> {
    private final L left;
    private final R right;

    public static<L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
