
package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.CompilerControl;

import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;

/**
 * .
 */
public class OverridingClassA extends Polymorph
{
    @CompilerControl(DONT_INLINE)
    public void polymorphicMethod()
    {

    }
}
