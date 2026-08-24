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
 * WITHOUT first-element pre-check; element 0 matches.
 *
 * Skips the pre-check and calls vectorizedMismatch directly:
 *   return vectorizedMismatch(...);
 *
 * The arrays are fully equal so vectorizedMismatch returns -1. By removing
 * the extra branch + load of the pre-check, this should be marginally faster
 * than WithFirstElCheck_FirstElMatches when element 0 always matches.
 *
 * Compare with ArraysMismatch_WithFirstElCheck_FirstElMatches to measure the
 * cost/benefit of the pre-check on the "match" hot path.
 *
 * Options:
 *   arrayLength (default 64, must be > 7 to exercise the vectorized path)
 */
public final class ArraysMismatch_NoFirstElCheck_FirstElMatches extends MicroBench {

    static final int ARRAY_LENGTH = option("arrayLength", 64);

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
            sink += ArraysMismatchBenchHelpers.mismatchNoFirstElCheck(A, B, ARRAY_LENGTH);
        }
        if (sink > 0) throw new RuntimeException("unexpected mismatch result: " + sink);
        return numIterations;
    }
}
