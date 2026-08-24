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
 * Large array (length > 7), always calls vectorizedMismatch — no length guard.
 *
 * For arrays above the threshold the JDK also calls vectorizedMismatch, so
 * this benchmark and ArraysMismatch_LargeArray_WithThreshold should produce
 * nearly identical scores. Any difference reflects the cost of the threshold
 * branch itself.
 *
 * Arrays are fully equal; vectorizedMismatch returns -1.
 *
 * Options:
 *   arrayLength (default 64, should be > 7 to be above the JDK threshold)
 */
public final class ArraysMismatch_LargeArray_NoThreshold extends MicroBench {

    static final int ARRAY_LENGTH = option("arrayLength", 64);

    private static final boolean[] A = new boolean[ARRAY_LENGTH];
    private static final boolean[] B = new boolean[ARRAY_LENGTH];

    static {
        // Non-zero final element so the full array is scanned.
        A[ARRAY_LENGTH - 1] = true;
        B[ARRAY_LENGTH - 1] = true;
    }

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
