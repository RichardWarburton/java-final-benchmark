package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;
import static org.openjdk.jmh.annotations.Mode.SampleTime;
import static org.openjdk.jmh.annotations.Scope.Thread;

@BenchmarkMode(SampleTime)
@Fork(1)
@State(Thread)
public class JavaFinalBenchmark extends BenchmarkParent1
{

    @GenerateMicroBenchmark
    public void virtualInvoke()
    {
        targetVirtual();
    }

    @GenerateMicroBenchmark
    public void finalInvoke()
    {
        targetFinal();
    }

    /**
     * Inherited Methods
     *
     * Test the hypothesis of distance up the class hierarchy affects the invoke performance.
     * Numbers refer to how far up the class hierarchy the inherited method is from
     */

    @GenerateMicroBenchmark
    public void parentMethod1()
    {
        inheritedTarget1();
    }

    @GenerateMicroBenchmark
    public void parentMethod2()
    {
        inheritedTarget2();
    }

    @GenerateMicroBenchmark
    public void parentMethod3()
    {
        inheritedTarget3();
    }

    @GenerateMicroBenchmark
    public void parentMethod4()
    {
        inheritedTarget4();
    }

    @GenerateMicroBenchmark
    public void parentFinalMethod1()
    {
        inheritedFinalTarget1();
    }

    @GenerateMicroBenchmark
    public void parentFinalMethod2()
    {
        inheritedFinalTarget2();
    }

    @GenerateMicroBenchmark
    public void parentFinalMethod3()
    {
        inheritedFinalTarget3();
    }

    @GenerateMicroBenchmark
    public void parentFinalMethod4()
    {
        inheritedFinalTarget4();
    }

    /**
     * Inherited Methods
     *
     * Test the hypothesis of distance up the class hierarchy affects the invoke performance.
     * Numbers refer to how far up the class hierarchy the inherited method is from
     */

    @GenerateMicroBenchmark
    public void alwaysOverriddenMethod()
    {
        alwaysOverriddenTarget();
    }

    @GenerateMicroBenchmark
    public void specialAlwaysOverriddenMethod()
    {
        super.alwaysOverriddenTarget();
    }

    // Invoked target methods,see also parent classes.

    @CompilerControl(DONT_INLINE)
    public void alwaysOverriddenTarget()
    {

    }

    @CompilerControl(DONT_INLINE)
    public void targetVirtual()
    {

    }

    @CompilerControl(DONT_INLINE)
    public final void targetFinal()
    {

    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include(".*" + JavaFinalBenchmark.class.getSimpleName() + ".*")
                .warmupIterations(10)
                .measurementIterations(10)
                .timeUnit(NANOSECONDS)
                .build();

        new Runner(opt).run();
    }
}
