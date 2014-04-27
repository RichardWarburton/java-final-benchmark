/*
 * Copyright 2014 Richard Warburton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insightfullogic.java_final_benchmarks;

import org.openjdk.jmh.annotations.CompilerControl;

import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;

/**
 * .A class with a single method which may be overridden by children
 */
public class Polymorph
{
    @CompilerControl(DONT_INLINE)
    public void polymorphicMethod()
    {

    }
}
