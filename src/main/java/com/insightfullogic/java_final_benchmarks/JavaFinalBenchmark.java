package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Thread;

@BenchmarkMode(AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = SECONDS)
@Fork(5)
@OutputTimeUnit(NANOSECONDS)
@State(Thread)
public class JavaFinalBenchmark {

    // Deliberately a field, JMH avoids constant folding
    private double x = Math.PI;

    private TargetClass1 target;
    private InlinableTargetClass1 inlinableTarget;

    @Setup
    public void setup() {
        target = new TargetClass1();
        inlinableTarget = new InlinableTargetClass1();
    }

    @Benchmark
    public void virtualInvoke() {
        target.targetVirtual();
    }

    @Benchmark
    public void finalInvoke() {
        target.targetFinal();
    }

    @Benchmark
    public void inlinableVirtualInvoke() {
        inlinableTarget.targetVirtual();
    }

    @Benchmark
    public void inlinableFinalInvoke() {
        inlinableTarget.targetFinal();
    }

    /**
     * Inherited Methods
     * <p/>
     * Test the hypothesis of distance up the class hierarchy affects the invoke performance.
     * Numbers refer to how far up the class hierarchy the inherited method is from
     */

    @Benchmark
    public void parentMethod1() {
        target.inheritedTarget1();
    }

    @Benchmark
    public void parentMethod2() {
        target.inheritedTarget2();
    }

    @Benchmark
    public void parentMethod3() {
        target.inheritedTarget3();
    }

    @Benchmark
    public void parentMethod4() {
        target.inheritedTarget4();
    }

    @Benchmark
    public void parentFinalMethod1() {
        target.inheritedFinalTarget1();
    }

    @Benchmark
    public void parentFinalMethod2() {
        target.inheritedFinalTarget2();
    }

    @Benchmark
    public void parentFinalMethod3() {
        target.inheritedFinalTarget3();
    }

    @Benchmark
    public void parentFinalMethod4() {
        target.inheritedFinalTarget4();
    }

    @Benchmark
    public void alwaysOverriddenMethod() {
        target.alwaysOverriddenTarget();
    }

    @Benchmark
    public double inlinableParentMethod1() {
        return inlinableTarget.inheritedTarget1();
    }

    @Benchmark
    public double inlinableParentMethod2() {
        return inlinableTarget.inheritedTarget2();
    }

    @Benchmark
    public double inlinableParentMethod3() {
        return inlinableTarget.inheritedTarget3();
    }

    @Benchmark
    public double inlinableParentMethod4() {
        return inlinableTarget.inheritedTarget4();
    }

    @Benchmark
    public double inlinableParentFinalMethod1() {
        return inlinableTarget.inheritedFinalTarget1();
    }

    @Benchmark
    public double inlinableParentFinalMethod2() {
        return inlinableTarget.inheritedFinalTarget2();
    }

    @Benchmark
    public double inlinableParentFinalMethod3() {
        return inlinableTarget.inheritedFinalTarget3();
    }

    @Benchmark
    public double inlinableParentFinalMethod4() {
        return inlinableTarget.inheritedFinalTarget4();
    }

    @Benchmark
    public double inlinableAlwaysOverriddenMethod() {
        return inlinableTarget.alwaysOverriddenTarget();
    }


    public class InlinableTargetClass1 extends InlinableTargetClass2 {
        public double alwaysOverriddenTarget() {
            return x;
        }

        public double inheritedTarget1() {
            return x;
        }

        public final double inheritedFinalTarget1() {
            return x;
        }

        public double targetVirtual() {
            return x;
        }

        public final double targetFinal() {
            return x;
        }
    }

    public class InlinableTargetClass2 extends InlinableTargetClass3 {
        public double alwaysOverriddenTarget() {
            return x;
        }

        public double inheritedTarget2() {
            return x;
        }

        public final double inheritedFinalTarget2() {
            return x;
        }
    }

    public class InlinableTargetClass3 extends InlinableTargetClass4 {
        public double alwaysOverriddenTarget() {
            return x;
        }

        public double inheritedTarget3() {
            return x;
        }

        public final double inheritedFinalTarget3() {
            return x;
        }
    }

    public class InlinableTargetClass4 {
        public double alwaysOverriddenTarget() {
            return x;
        }

        public double inheritedTarget4() {
            return x;
        }

        public final double inheritedFinalTarget4() {
            return x;
        }
    }

    @CompilerControl(DONT_INLINE)
    public static class TargetClass1 extends TargetClass2 {
        public void alwaysOverriddenTarget() {
        }

        public void inheritedTarget1() {
        }

        public final void inheritedFinalTarget1() {
        }

        public void targetVirtual() {
        }

        public final void targetFinal() {
        }
    }

    @CompilerControl(DONT_INLINE)
    public static class TargetClass2 extends TargetClass3 {
        public void alwaysOverriddenTarget() {
        }

        public void inheritedTarget2() {
        }

        public final void inheritedFinalTarget2() {
        }
    }

    @CompilerControl(DONT_INLINE)
    public static class TargetClass3 extends TargetClass {
        public void alwaysOverriddenTarget() {
        }

        public void inheritedTarget3() {
        }

        public final void inheritedFinalTarget3() {
        }
    }

    @CompilerControl(DONT_INLINE)
    public static class TargetClass {
        public void alwaysOverriddenTarget() {
        }

        public void inheritedTarget4() {
        }

        public final void inheritedFinalTarget4() {
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + JavaFinalBenchmark.class.getSimpleName() + ".*")
                .build();

        new Runner(opt).run();
    }
}
