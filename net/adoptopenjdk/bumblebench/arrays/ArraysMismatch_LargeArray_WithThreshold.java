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
 * Large array (length > 7), with the JDK threshold guard (calls vectorizedMismatch
 * because length > 7).
 *
 * Mirrors exactly the JDK path for large arrays (without the first-element
 * pre-check, to isolate the threshold question):
 *   if (length > 7) { return vectorizedMismatch(...); }
 *
 * Arrays are fully equal; vectorizedMismatch returns -1.
 *
 * Compare with ArraysMismatch_LargeArray_NoThreshold to confirm that the
 * threshold branch has negligible cost for large arrays, and with
 * ArraysMismatch_SmallArray_WithThreshold to see the threshold's value at
 * the boundary.
 *
 * Options:
 *   arrayLength (default 64, should be > 7 to be above the JDK threshold)
 */
public final class ArraysMismatch_LargeArray_WithThreshold extends MicroBench {

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
