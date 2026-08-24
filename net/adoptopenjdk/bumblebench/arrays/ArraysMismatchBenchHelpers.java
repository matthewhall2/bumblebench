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

import jdk.internal.misc.Unsafe;
import jdk.internal.util.ArraysSupport;

/**
 * Shared mismatch strategy implementations used by all ArraysMismatch benchmarks.
 *
 * Requires at compile-time and runtime:
 *   --add-exports java.base/jdk.internal.util=ALL-UNNAMED
 *   --add-exports java.base/jdk.internal.misc=ALL-UNNAMED
 */
public class ArraysMismatchBenchHelpers {

    public static final int  LOG2_BYTE_SCALE = ArraysSupport.LOG2_ARRAY_BOOLEAN_INDEX_SCALE;
    public static final long BOOL_BASE       = Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;

    /**
     * Mirrors the current JDK implementation exactly (WITH first-element pre-check).
     *   if (length > 7) { if (a[0] != b[0]) return 0; return vectorizedMismatch(...); }
     *   else { scalar loop }
     */
    public static int mismatchWithFirstElCheck(boolean[] a, boolean[] b, int length) {
        if (length > 7) {
            if (a[0] != b[0])
                return 0;
            return ArraysSupport.vectorizedMismatch(
                    a, BOOL_BASE,
                    b, BOOL_BASE,
                    length, LOG2_BYTE_SCALE);
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != b[i]) return i;
        }
        return -1;
    }

    /**
     * Like the JDK, but WITHOUT the first-element pre-check.
     *   if (length > 7) { return vectorizedMismatch(...); }
     *   else { scalar loop }
     */
    public static int mismatchNoFirstElCheck(boolean[] a, boolean[] b, int length) {
        if (length > 7) {
            return ArraysSupport.vectorizedMismatch(
                    a, BOOL_BASE,
                    b, BOOL_BASE,
                    length, LOG2_BYTE_SCALE);
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != b[i]) return i;
        }
        return -1;
    }

    /**
     * Always calls vectorizedMismatch regardless of array length — no length threshold guard.
     */
    public static int mismatchVectorizedAlways(boolean[] a, boolean[] b, int length) {
        return ArraysSupport.vectorizedMismatch(
                a, BOOL_BASE,
                b, BOOL_BASE,
                length, LOG2_BYTE_SCALE);
    }
}
