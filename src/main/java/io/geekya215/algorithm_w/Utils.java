package io.geekya215.algorithm_w;

import io.geekya215.algorithm_w.scheme.Scheme;
import io.geekya215.algorithm_w.scheme._Scheme;
import io.geekya215.algorithm_w.type.Type;

import java.util.Map;
import java.util.stream.Collectors;

public final class Utils {
    //  nullSubst :: Subst
    //  nullSubst = Map.empty
    static Map nullSubst() {
        return Map.of();
    }

    //  composeSubst :: Subst → Subst → Subst
    //  composeSubst s1 s2 = (Map.map (apply s1 ) s2 ) ‘Map.union‘ s1
    static Map<String, Type> composeSubst(Map<String, Type> s1, Map<String, Type> s2) {
        s2.forEach((s, t) -> t.apply(s1));
        s2.putAll(s1);
        return s2;
    }

    //  generalize :: TypeEnv → Type → Scheme
    //  generalize env t = Scheme vars t
    //    where vars = Set.toList ((ftv t) \ (ftv env))
    static Scheme generalize(TypeEnv env, Type t) {
        var vars = t.ftv();
        vars.removeAll(env.ftv());
        return new _Scheme(vars.stream().collect(Collectors.toList()), t);
    }
}
