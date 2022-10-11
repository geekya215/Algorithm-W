package io.geekya215.algorithm_w;

import io.geekya215.algorithm_w.type.Type;

import java.util.Map;
import java.util.Set;

// class Types a where
//   ftv   :: a -> Set.Set String
//   apply :: Map.Map String Type -> a -> a
public interface Types<A> {
    Set<String> ftv(A a);

    A apply(Map<String, Type> subst, A a);
}
