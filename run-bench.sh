#!/bin/sh

set -eu

mvn clean package
java -cp target/microbenchmarks.jar com.insightfullogic.java_final_benchmarks.JavaFinalBenchmark
