package io.geekya215.algorithm_w;

import java.util.List;

public sealed interface Scheme {
    record _Scheme(List<String> a, Type t) implements Scheme {
    }
}
