package io.geekya215.algorithm_w;

public sealed interface Lit permits Lit.LInt, Lit.LBool {
    record LInt(Integer x) implements Lit {
    }

    record LBool(Boolean x) implements Lit {
    }
}
