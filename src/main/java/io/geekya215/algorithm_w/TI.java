package io.geekya215.algorithm_w;

import io.geekya215.algorithm_w.expr.*;
import io.geekya215.algorithm_w.lit.LBool;
import io.geekya215.algorithm_w.lit.LInt;
import io.geekya215.algorithm_w.lit.Lit;
import io.geekya215.algorithm_w.scheme.Scheme;
import io.geekya215.algorithm_w.scheme._Scheme;
import io.geekya215.algorithm_w.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.geekya215.algorithm_w.Utils.*;

// use global variable instead of State monad
public class TI {
    static Integer counter = 0;

    public static Type newTyVar(String prefix) {
        return new TVar(prefix + counter++);
    }

    public static Type instantiate(Scheme scheme) {
        return switch (scheme) {
            case _Scheme _scheme -> {
                var vars = _scheme.vars();
                var t = _scheme.t();
                var s = vars
                    .stream().collect(Collectors.toMap(Function.identity(), e -> newTyVar("a")));
                yield t.apply(s);
            }
        };
    }

    public static Map<String, Type> mgu(Type t1, Type t2) throws Exception {
        if (t1 instanceof TFun _t1 && t2 instanceof TFun _t2) {
            var l = _t1.t1();
            var r = _t1.t2();
            var _l = _t2.t1();
            var _r = _t2.t2();
            var s1 = mgu(l, _l);
            var s2 = mgu(r.apply(s1), _r.apply(s1));
            return composeSubst(s1, s2);
        }

        if (t1 instanceof TVar _t1) {
            return varBind(_t1.x(), t2);
        }

        if (t2 instanceof TVar _t2) {
            return varBind(_t2.x(), t1);
        }

        if (t1 instanceof TInt && t2 instanceof TInt) {
            return nullSubst();
        }

        if (t1 instanceof TBool && t2 instanceof TBool) {
            return nullSubst();
        }

        throw new Exception("types do not unify " + t1 + " vs. " + t2);

    }

    public static Map<String, Type> varBind(String x, Type t) throws Exception {
        if (t instanceof TVar _t && _t.x() == x) {
            return nullSubst();
        }

        if (t.ftv().contains(x)) {
            throw new Exception("occurs check fails: " + x + " vs. " + t);
        }

        var res = new HashMap<String, Type>();
        res.put(x, t);
        return res;
    }

    public static Pair<Map<String, Type>, Type> tiLit(TypeEnv typeEnv, Lit lit) {
        return switch (lit) {
            case LBool lBool -> new Pair<>(nullSubst(), new TBool());
            case LInt lInt -> new Pair<>(nullSubst(), new TInt());
        };
    }
    public static Pair<Map<String, Type>, Type> ti(TypeEnv typeEnv, Expr expr) throws Exception {
        if (expr instanceof EVar _e) {
            var env = typeEnv.env();
            var n = _e.x();
            var sigma = env.get(n);
            if (Objects.isNull(sigma)) {
                throw new Exception("unbound variable: " + n);
            } else {
                var t = instantiate(sigma);
                return new Pair<>(nullSubst(), t);
            }
        }

        if (expr instanceof ELit _e) {
            return tiLit(typeEnv, _e.x());
        }

        if (expr instanceof EAbs _e) {
            var n = _e.x();
            var e = _e.e();
            var tv = newTyVar("a");
            var _typeEnv = remove(typeEnv, n);
            _typeEnv.env().put(n, new _Scheme(new ArrayList<>(), tv));
            // maybe should use mutable list?
//            _env.env().putAll(Map.of(n, new _Scheme(new ArrayList<>(), tv)));
            var __typeEnv = new TypeEnv(_typeEnv.env());
            var pair = ti(__typeEnv, e);
            var s1 = pair.fst();
            var t1 = pair.snd();
            return new Pair<>(s1, new TFun(tv.apply(s1), t1));
        }

        if (expr instanceof EApp _e) {
            var e1 = _e.e1();
            var e2 = _e.e2();
            var tv = newTyVar("a");
            var p1 = ti(typeEnv, e1);
            var s1 = p1.fst();
            var t1 = p1.snd();
            var p2 = ti(typeEnv.apply(s1), e2);
            var s2 = p2.fst();
            var t2 = p2.snd();
            var s3 = mgu(t1.apply(s2), new TFun(t2, tv));
            return new Pair<>(composeSubst(s3, composeSubst(s2, s1)), tv.apply(s3));
        }

        if (expr instanceof ELet _e) {
            var x = _e.x();
            var e1 = _e.e1();
            var e2 = _e.e2();
            var p1 = ti(typeEnv, e1);
            var s1 = p1.fst();
            var t1 = p1.snd();
            var _typeEnv = remove(typeEnv, x);
            var t = generalize(typeEnv.apply(s1), t1);
            _typeEnv.env().put(x, t);
            var p2 = ti(_typeEnv.apply(s1), e2);
            var s2 = p2.fst();
            var t2 = p2.snd();
            return new Pair<>(composeSubst(s1, s2), t2);
        }
        throw new Exception("unknown type inference");
    }

    public static Type typeInference(Map<String, Scheme> env, Expr expr) throws Exception {
        var pair = ti(new TypeEnv(env), expr);
        var s = pair.fst();
        var t = pair.snd();
        return t.apply(s);
    }
}
