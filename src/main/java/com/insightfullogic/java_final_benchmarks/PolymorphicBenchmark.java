package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.WarmupMode;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.CompilerControl.Mode.*;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Thread;


@BenchmarkMode(AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = SECONDS)
@Fork(5)
@OutputTimeUnit(NANOSECONDS)
@State(Thread)
public class PolymorphicBenchmark
{
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

    @GenerateMicroBenchmark
    public void monomorphicInvoke_warmup() {
        invoke(polymorph);
    }

    @GenerateMicroBenchmark
    public void monomorphicInvoke_measure() {
        invoke(polymorph);
    }

    @GenerateMicroBenchmark
    public void bimorphicInvoke_warmup() {
        invoke(childA);
        invoke(childB);
    }

    @GenerateMicroBenchmark
    public void bimorphicInvoke_measure() {
        invoke(childA);
    }

    @GenerateMicroBenchmark
    public void megamorphicInvoke_warmup() {
        invoke(polymorph);
        invoke(childA);
        invoke(childB);
    }

    @GenerateMicroBenchmark
    public void megamorphicInvoke_measure() {
        invoke(childA);
    }

    @CompilerControl(DONT_INLINE)
    private void invoke(Polymorph polymorph) {
        polymorph.polymorphicMethod();
    }

    @GenerateMicroBenchmark
    public void inlinableMonomorphicInvoke_warmup() {
        inlinableInvoke(inlinablePolymorph);
    }

    @GenerateMicroBenchmark
    public void inlinableMonomorphicInvoke_measure() {
        inlinableInvoke(inlinablePolymorph);
    }

    @GenerateMicroBenchmark
    public void inlinableBimorphicInvoke_warmup() {
        inlinableInvoke(inlinableChildA);
        inlinableInvoke(inlinableChildB);
    }

    @GenerateMicroBenchmark
    public void inlinableBimorphicInvoke_measure() {
        inlinableInvoke(inlinableChildA);
    }

    @GenerateMicroBenchmark
    public void inlinableMegamorphicInvoke_warmup() {
        inlinableInvoke(inlinablePolymorph);
        inlinableInvoke(inlinableChildA);
        inlinableInvoke(inlinableChildB);
    }

    @GenerateMicroBenchmark
    public void inlinableMegamorphicInvoke_measure() {
        inlinableInvoke(inlinableChildA);
    }

    @CompilerControl(DONT_INLINE)
    private void inlinableInvoke(InlinablePolymorph polymorph) {
        polymorph.polymorphicMethod();
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

    public static class InlinablePolymorph {
        public void polymorphicMethod() {
        }
    }

    public static class InlinableOverridingClassA extends InlinablePolymorph {
        public void polymorphicMethod() {
        }
    }

    public static class InlinableOverridingClassB extends InlinablePolymorph {
        public void polymorphicMethod() {
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
        print("Mono", monomorphResult.getPrimaryResult());
        print("Bi",   bimorphResult.getPrimaryResult());
        print("Mega", megamorphResult.getPrimaryResult());
        print("Inlinable Mono", inlinableMonomorphResult.getPrimaryResult());
        print("Inlinable Bi",   inlinableBimorphResult.getPrimaryResult());
        print("Inlinable Mega", inlinableMegamorphResult.getPrimaryResult());
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
