package io.geekya215.algorithm_w.expr;

public record ELet(String x, Expr e1, Expr e2) implements Expr {
}
