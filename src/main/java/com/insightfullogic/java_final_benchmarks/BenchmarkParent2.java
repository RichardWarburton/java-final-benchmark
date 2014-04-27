package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.CompilerControl;

import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;

/**
 * .
 */
public class BenchmarkParent2 extends BenchmarkParent3
{
    @CompilerControl(DONT_INLINE)
    public void alwaysOverriddenTarget()
    {

    }

    @CompilerControl(DONT_INLINE)
    public void inheritedTarget2()
    {

    }

    @CompilerControl(DONT_INLINE)
    public final void inheritedFinalTarget2()
    {

    }
}
