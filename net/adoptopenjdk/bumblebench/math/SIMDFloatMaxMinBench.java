/*******************************************************************************
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/

package net.adoptopenjdk.bumblebench.math;

import java.lang.Math;
import java.util.Random;

import net.adoptopenjdk.bumblebench.core.MicroBench;

public final class SIMDFloatMaxMinBench extends MicroBench {
    protected long doBatch(long numIterations) throws InterruptedException {
        pauseTimer();
        Float a, b;
        Float minRange = 0.0f; Float maxRange = 100.0f; Float rangeDiff = maxRange - minRange;
        Random randGen = new Random();
        Float min = 0.0f;
        Float max = 0.0f;
        System.out.println(String.format("numIterations: %d", numIterations));
        for (long i = 0; i < numIterations; i++)
        {
            a=randGen.nextFloat(); b=randGen.nextFloat();
            a=(a*rangeDiff)+minRange; b=(b*rangeDiff)+minRange;

            if (a == 0.0f && b == 0.0f) {
                System.out.println("both are zero");
            }
            else if (a == 0.0f) {
                System.out.println("a is zero");
            }   
            
            else if (b == 0.0f) {
                System.out.println("b is zero");
            }  

            startTimer();
            max = Math.max(a+max,b+max);
            min = Math.min(a+max,b+max);
            pauseTimer();
        }
        return numIterations;
    }
}

