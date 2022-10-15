package io.geekya215.algorithm_w.expr;

public sealed interface Expr permits EAbs, EApp, ELet, ELit, EVar {
}
