package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.SampleTime;
import static org.openjdk.jmh.annotations.Scope.Thread;

@BenchmarkMode(SampleTime)
@Fork(1)
@State(Thread)
public class BrokenBenchmark
{
    private final Polymorph polymorph = new Polymorph();
    private final Polymorph childA = new OverridingClassA();
    private final Polymorph childB = new OverridingClassB();

    @GenerateMicroBenchmark
    public void monomorphicInvoke()
    {
        polymorph.polymorphicMethod();
    }

    @GenerateMicroBenchmark
    public void bimorphicInvokeWarmup()
    {
        invokeBimorphic(childA);
    }

    @GenerateMicroBenchmark
    public void bimorphicInvoke()
    {
        invokeBimorphic(childB);
    }

    @GenerateMicroBenchmark
    public void megamorphicInvokeWarmup()
    {
        invokeMegamorphic(childA);
        invokeMegamorphic(childB);
    }

    @GenerateMicroBenchmark
    public void megamorphicInvoke()
    {
        invokeMegamorphic(polymorph);
    }

    private void invokeBimorphic(Polymorph polymorph)
    {
        polymorph.polymorphicMethod();
    }

    private void invokeMegamorphic(Polymorph polymorph)
    {
        polymorph.polymorphicMethod();
    }


    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include(".*" + BrokenBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(10)
                .measurementIterations(10)
                .timeUnit(NANOSECONDS)
                .build();

        new Runner(opt).run();
    }

}
