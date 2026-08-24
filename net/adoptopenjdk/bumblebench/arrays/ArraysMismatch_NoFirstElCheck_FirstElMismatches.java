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
 * WITHOUT first-element pre-check; element 0 already differs.
 *
 * Skips the pre-check and calls vectorizedMismatch directly:
 *   return vectorizedMismatch(...);
 *
 * vectorizedMismatch is called even though the mismatch is at index 0. This
 * incurs the full vectorized call overhead for what could have been a trivial
 * early return.
 *
 * Compare with ArraysMismatch_WithFirstElCheck_FirstElMismatches to see how
 * much the pre-check saves when the first element is the mismatch.
 *
 * Options:
 *   arrayLength (default 64, must be > 7 to exercise the vectorized path)
 */
public final class ArraysMismatch_NoFirstElCheck_FirstElMismatches extends MicroBench {

    static final int ARRAY_LENGTH = option("arrayLength", 64);

    // a[0] = false, b[0] = true — mismatch at index 0.
    private static final boolean[] A = new boolean[ARRAY_LENGTH];
    private static final boolean[] B = new boolean[ARRAY_LENGTH];

    static {
        B[0] = true;
    }

    @Override
    protected long doBatch(long numIterations) throws InterruptedException {
        int sink = 0;
        for (long i = 0; i < numIterations; i++) {
            sink += ArraysMismatchBenchHelpers.mismatchNoFirstElCheck(A, B, ARRAY_LENGTH);
        }
        // Each call returns 0, so sink must be 0.
        if (sink != 0) throw new RuntimeException("unexpected result: " + sink);
        return numIterations;
    }
}
