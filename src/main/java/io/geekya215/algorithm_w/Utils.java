package io.geekya215.algorithm_w;

import io.geekya215.algorithm_w.scheme.Scheme;
import io.geekya215.algorithm_w.scheme._Scheme;
import io.geekya215.algorithm_w.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class Utils {
    public static Map nullSubst() {
        return new HashMap();
    }

    public static Map<String, Type> composeSubst(Map<String, Type> s1, Map<String, Type> s2) {
        var res = s2.entrySet()
            .stream()
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().apply(s1)));
        res.putAll(s1);
        return res;
    }

    public static Scheme generalize(TypeEnv env, Type t) {
        var vars = t.ftv();
        vars.removeAll(env.ftv());
        return new _Scheme(vars.stream().collect(Collectors.toList()), t);
    }

    public static TypeEnv remove(TypeEnv typeEnv, String var) {
        var env = typeEnv.env();
        env.remove(var);
        return new TypeEnv(env);
    }
}
