package io.geekya215.algorithm_w;

public sealed interface Expr
    permits Expr.EVar, Expr.ELit, Expr.EApp, Expr.EAbs, Expr.ELet {
    record EVar(String x) implements Expr {
    }

    record ELit(Lit x) implements Expr {
    }

    record EApp(Expr e1, Expr e2) implements Expr {
    }

    record EAbs(String x, Expr e) implements Expr {
    }

    record ELet(String x, Expr e1, Expr e2) implements Expr {
    }
}
