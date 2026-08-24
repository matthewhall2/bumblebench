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

package net.adoptopenjdk.bumblebench.arrays;

import net.adoptopenjdk.bumblebench.core.MicroBench;

/**
 * Small array (length <= 7), always calls vectorizedMismatch — no length guard.
 *
 * The JDK uses a scalar loop for arrays of length <= 7, avoiding the overhead
 * of the vectorized call for very small inputs. This benchmark tests whether
 * calling vectorizedMismatch unconditionally is actually slower for small arrays.
 *
 * Arrays are fully equal; vectorizedMismatch returns -1.
 *
 * Compare with ArraysMismatch_SmallArray_WithThreshold (JDK behaviour: scalar
 * loop for length <= 7) to see whether the threshold guard is worth it.
 *
 * Options:
 *   arrayLength (default 4, should be <= 7 to stay below the JDK threshold)
 */
public final class ArraysMismatch_SmallArray_NoThreshold extends MicroBench {

    static final int ARRAY_LENGTH = option("arrayLength", 4);

    // All false — arrays are equal.
    private static final boolean[] A = new boolean[ARRAY_LENGTH];
    private static final boolean[] B = new boolean[ARRAY_LENGTH];

    @Override
    protected long doBatch(long numIterations) throws InterruptedException {
        int sink = 0;
        for (long i = 0; i < numIterations; i++) {
            sink += ArraysMismatchBenchHelpers.mismatchVectorizedAlways(A, B, ARRAY_LENGTH);
        }
        if (sink > 0) throw new RuntimeException("unexpected mismatch result: " + sink);
        return numIterations;
    }
}
