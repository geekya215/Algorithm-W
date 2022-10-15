package io.geekya215.algorithm_w;

import io.geekya215.algorithm_w.scheme.Scheme;
import io.geekya215.algorithm_w.scheme._Scheme;
import io.geekya215.algorithm_w.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class Utils {
    //  nullSubst :: Subst
    //  nullSubst = Map.empty
    public static Map nullSubst() {
        return new HashMap();
    }

    //  composeSubst :: Subst -> Subst -> Subst
    //  composeSubst s1 s2 = Map.map (apply s1) s2 `Map.union` s1
    public static Map<String, Type> composeSubst(Map<String, Type> s1, Map<String, Type> s2) {
        var res = s2.entrySet()
            .stream()
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().apply(s1)));
        res.putAll(s1);
        return res;
    }

    //  generalize :: TypeEnv -> Type -> Scheme
    //  generalize env t = Scheme vars t
    //    where vars = Set.toList (Set.difference (ftv t) (ftv env))
    public static Scheme generalize(TypeEnv env, Type t) {
        var vars = t.ftv();
        vars.removeAll(env.ftv());
        return new _Scheme(vars.stream().collect(Collectors.toList()), t);
    }

    //  remove :: TypeEnv -> String -> TypeEnv
    //  remove (TypeEnv env) var = TypeEnv (Map.delete var env)
    public static TypeEnv remove(TypeEnv typeEnv, String var) {
        var env = typeEnv.env();
        env.remove(var);
        return new TypeEnv(env);
    }
}
