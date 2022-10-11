package io.geekya215.algorithm_w.type;

import java.util.Map;
import java.util.Set;

// data Type = TVar String
//           = TInt
//           = TBool
//           = TFun Type Type
public sealed interface Type permits TBool, TFun, TInt, TVar {

//  ftv (TVar n) = {n}
//  ftv TInt = ∅
//  ftv TBool = ∅
//  ftv (TFun t1 t2 ) = ftv t1 ∪ ftv t2
    default Set<String> ftv() {
        return switch (this) {
            case TVar tVar -> Set.of(tVar.x());
            case TInt tInt -> Set.of();
            case TBool tBool -> Set.of();
            case TFun tFun -> {
                var t1 = tFun.t1();
                var t2 = tFun.t2();
                var res = t1.ftv();
                res.addAll(t2.ftv());
                yield res;
            }
        };
    }
//  apply s (TVar n) = case Map.lookup n s of
//                      Nothing → TVar n
//                      Just t → t
//  apply s (TFun t1 t2 ) = TFun (apply s t1 ) (apply s t2 )
//  apply s t = t
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
