package io.geekya215.algorithm_w.type;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public sealed interface Type permits TBool, TFun, TInt, TVar {
    default Set<String> ftv() {
        return switch (this) {
            case TVar tVar -> {
                var set = new HashSet<String>();
                set.add(tVar.x());
                yield set;
            }
            case TInt tInt -> new HashSet<>();
            case TBool tBool -> new HashSet<>();
            case TFun tFun -> {
                var t1 = tFun.t1();
                var t2 = tFun.t2();
                var res = t1.ftv();
                res.addAll(t2.ftv());
                yield res;
            }
        };
    }

    default Type apply(Map<String, Type> subst) {
        return switch (this) {
            case TVar tVar -> {
                var n = tVar.x();
                yield subst.getOrDefault(n, this);
            }
            case TFun tFun -> {
                var t1 = tFun.t1();
                var t2 = tFun.t2();
                yield new TFun(t1.apply(subst), t2.apply(subst));
            }
            case TBool tBool -> this;
            case TInt tInt -> this;
        };
    }

}
