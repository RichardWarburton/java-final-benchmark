package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.WarmupMode;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Thread;


@BenchmarkMode(AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = SECONDS)
@Fork(5)
@OutputTimeUnit(NANOSECONDS)
@State(Thread)
public class PolymorphicBenchmark
{
    // Deliberately a field, JMH avoids constant folding
    private double x = Math.PI;

    private Polymorph polymorph;
    private Polymorph childA;
    private Polymorph childB;

    private InlinablePolymorph inlinablePolymorph;
    private InlinablePolymorph inlinableChildA;
    private InlinablePolymorph inlinableChildB;

    @Setup
    public void setup() {
        polymorph = new Polymorph();
        childA = new OverridingClassA();
        childB = new OverridingClassB();

        inlinablePolymorph = new InlinablePolymorph();
        inlinableChildA = new InlinableOverridingClassA();
        inlinableChildB = new InlinableOverridingClassB();
    }

    @Benchmark
    public void monomorphicInvoke_warmup() {
        invoke(polymorph);
    }

    @Benchmark
    public void monomorphicInvoke_measure() {
        invoke(polymorph);
    }

    @Benchmark
    public void bimorphicInvoke_warmup() {
        invoke(childA);
        invoke(childB);
    }

    @Benchmark
    public void bimorphicInvoke_measure() {
        invoke(childA);
    }

    @Benchmark
    public void megamorphicInvoke_warmup() {
        invoke(polymorph);
        invoke(childA);
        invoke(childB);
    }

    @Benchmark
    public void megamorphicInvoke_measure() {
        invoke(childA);
    }

    @CompilerControl(DONT_INLINE)
    private void invoke(Polymorph polymorph) {
        polymorph.polymorphicMethod();
    }

    @Benchmark
    public double inlinableMonomorphicInvoke_warmup() {
        return inlinableInvoke(inlinablePolymorph);
    }

    @Benchmark
    public double inlinableMonomorphicInvoke_measure() {
        return inlinableInvoke(inlinablePolymorph);
    }

    @Benchmark
    public double inlinableBimorphicInvoke_warmup() {
        return inlinableInvoke(inlinableChildA)
             + inlinableInvoke(inlinableChildB);
    }

    @Benchmark
    public double inlinableBimorphicInvoke_measure() {
        return inlinableInvoke(inlinableChildA);
    }

    @Benchmark
    public double inlinableMegamorphicInvoke_warmup() {
        return inlinableInvoke(inlinablePolymorph)
             + inlinableInvoke(inlinableChildA)
             + inlinableInvoke(inlinableChildB);
    }

    @Benchmark
    public double inlinableMegamorphicInvoke_measure() {
        return inlinableInvoke(inlinableChildA);
    }

    private double inlinableInvoke(InlinablePolymorph polymorph) {
        return polymorph.polymorphicMethod();
    }

    public static class Polymorph {
        @CompilerControl(DONT_INLINE)
        public void polymorphicMethod() {
        }
    }

    public static class OverridingClassA extends Polymorph {
        @CompilerControl(DONT_INLINE)
        public void polymorphicMethod() {
        }
    }

    public static class OverridingClassB extends Polymorph {
        @CompilerControl(DONT_INLINE)
        public void polymorphicMethod() {
        }
    }

    public class InlinablePolymorph {
        public double polymorphicMethod() {
            return x;
        }
    }

    public class InlinableOverridingClassA extends InlinablePolymorph {
        public double polymorphicMethod() {
            return x;
        }
    }

    public class InlinableOverridingClassB extends InlinablePolymorph {
        public double polymorphicMethod() {
            return x;
        }
    }

    public static void main(String[] args) throws RunnerException {
        RunResult monomorphResult = makeRunner("monomorphic");
        RunResult bimorphResult = makeRunner("bimorphic");
        RunResult megamorphResult = makeRunner("megamorphic");

        RunResult inlinableMonomorphResult = makeRunner("inlinableMonomorphic");
        RunResult inlinableBimorphResult = makeRunner("inlinableBimorphic");
        RunResult inlinableMegamorphResult = makeRunner("inlinableMegamorphic");

        System.out.println("----------------------------------");
        print("Monomorphic", monomorphResult.getPrimaryResult());
        print("Bimorphic",   bimorphResult.getPrimaryResult());
        print("Megamorphic", megamorphResult.getPrimaryResult());
        print("Inlinable Monomorphic", inlinableMonomorphResult.getPrimaryResult());
        print("Inlinable Bimorphic",   inlinableBimorphResult.getPrimaryResult());
        print("Inlinable Megamorphic", inlinableMegamorphResult.getPrimaryResult());
        System.out.println("----------------------------------");
    }

    private static RunResult makeRunner(String method) throws RunnerException {
        return new Runner(new OptionsBuilder()
                .warmupMode(WarmupMode.BULK)
                .include(".*" + PolymorphicBenchmark.class.getSimpleName() + ".*" + method + ".*_measure")
                .includeWarmup(".*" + PolymorphicBenchmark.class.getSimpleName() + ".*" + method + ".*_warmup")
                .build()).runSingle();
    }

    private static void print(String label, Result res) {
        double standardDeviation = res.getStatistics().getStandardDeviation();
        System.out.printf("%5s: %.3f +- %.3f %s%n", label, res.getScore(), standardDeviation, res.getScoreUnit());
    }

}
