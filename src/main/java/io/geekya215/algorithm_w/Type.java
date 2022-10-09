package io.geekya215.algorithm_w;

public sealed interface Type
    permits Type.TVar, Type.TInt, Type.TBool, Type.TFun {
    record TVar(String x) implements Type {
    }

    record TInt() implements Type {
    }

    record TBool() implements Type {
    }

    record TFun(Type t1, Type t2) implements Type {
    }
}
