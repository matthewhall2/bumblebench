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
 * WITH first-element pre-check; element 0 matches.
 *
 * Uses the JDK-style implementation:
 *   if (a[0] != b[0]) return 0;
 *   return vectorizedMismatch(...);
 *
 * The pre-check passes (a[0] == b[0]), so execution falls through to
 * vectorizedMismatch. The arrays are fully equal so vectorizedMismatch
 * returns -1. This is the common "equal arrays" hot path — the pre-check
 * costs an extra branch + load on every call for no benefit here.
 *
 * Compare with ArraysMismatch_NoFirstElCheck_FirstElMatches to measure that
 * overhead.
 *
 * Options:
 *   arrayLength (default 64, must be > 7 to exercise the vectorized path)
 */
public final class ArraysMismatch_WithFirstElCheck_FirstElMatches extends MicroBench {

    static final int ARRAY_LENGTH = option("arrayLength", 64);

    // Both arrays are fully equal. a[0] == b[0] so the pre-check passes.
    // A non-zero element is set at the end so the vectorized scan must
    // traverse the entire array and cannot short-circuit.
    private static final boolean[] A = new boolean[ARRAY_LENGTH];
    private static final boolean[] B = new boolean[ARRAY_LENGTH];

    static {
        A[ARRAY_LENGTH - 1] = true;
        B[ARRAY_LENGTH - 1] = true;
    }

    @Override
    protected long doBatch(long numIterations) throws InterruptedException {
        int sink = 0;
        for (long i = 0; i < numIterations; i++) {
            sink += ArraysMismatchBenchHelpers.mismatchWithFirstElCheck(A, B, ARRAY_LENGTH);
        }
        // Prevent dead-code elimination; -1 per call so sum should be negative.
        if (sink > 0) throw new RuntimeException("unexpected mismatch result: " + sink);
        return numIterations;
    }
}
