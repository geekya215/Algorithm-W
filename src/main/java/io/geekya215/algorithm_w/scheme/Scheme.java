package io.geekya215.algorithm_w.scheme;

import io.geekya215.algorithm_w.type.Type;

import java.util.Map;
import java.util.Set;

// instance Types Scheme where
//    ftv (Scheme vars t) =  Set.difference (ftv t) (Set.fromList vars)
//    apply s (Scheme vars t) = Scheme vars (apply (foldr Map.delete s vars) t)
public sealed interface Scheme permits _Scheme {
    default Set<String> ftv() {
        return switch (this) {
            case _Scheme scheme -> {
                var vars = scheme.vars();
                var t = scheme.t();
                var res = t.ftv();
                vars.forEach(res::remove);
                yield res;
            }
        };
    }

    default Scheme apply(Map<String, Type> subst) {
        return switch (this) {
            case _Scheme scheme -> {
                var vars = scheme.vars();
                var t = scheme.t();
                vars.forEach(subst::remove);
                yield new _Scheme(vars, t.apply(subst));
            }
        };
    }
}
