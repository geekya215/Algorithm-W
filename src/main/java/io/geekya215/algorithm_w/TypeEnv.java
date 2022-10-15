package io.geekya215.algorithm_w;

import io.geekya215.algorithm_w.scheme.Scheme;
import io.geekya215.algorithm_w.type.Type;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record TypeEnv(Map<String, Scheme> env) {
    Set<String> ftv() {
        return env.values().stream()
            .map(s -> s.ftv())
            .reduce(new HashSet<>(), (s1, s2) -> {
                s1.addAll(s2);
                return s1;
            });
    }

    TypeEnv apply(Map<String, Type> subst) {
        var res = env.entrySet()
            .stream()
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().apply(subst)));
        return new TypeEnv(res);
    }

}
