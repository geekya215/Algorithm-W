package io.geekya215.algorithm_w.scheme;

import io.geekya215.algorithm_w.type.Type;

import java.util.Map;
import java.util.Set;

public sealed interface Scheme permits _Scheme {

    //  ftv (Scheme vars t) = (ftv t) \ (Set.fromList vars)
    default Set<String> ftv() {
        return switch (this) {
            case _Scheme scheme -> {
                var t = scheme.ftv();
                scheme.vars().forEach(t::remove);
                yield t;
            }
        };
    }

    //  apply s (Scheme vars t) = Scheme vars (apply (foldr Map.delete s vars) t)
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
