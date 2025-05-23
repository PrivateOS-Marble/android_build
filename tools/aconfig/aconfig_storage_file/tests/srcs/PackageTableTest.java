/*
 * Copyright (C) 2024 The Android Open Source Project
 *
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
 */

package android.aconfig.storage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.aconfig.storage.FileType;
import android.aconfig.storage.PackageTable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PackageTableTest {

    @Test
    public void testPackageTable_rightHeader() throws Exception {
        PackageTable packageTable =
                PackageTable.fromBytes(TestDataUtils.getTestPackageMapByteBuffer(1));
        PackageTable.Header header = packageTable.getHeader();
        assertEquals(1, header.getVersion());
        assertEquals("mockup", header.getContainer());
        assertEquals(FileType.PACKAGE_MAP, header.getFileType());
        assertEquals(209, header.getFileSize());
        assertEquals(3, header.getNumPackages());
        assertEquals(31, header.getBucketOffset());
        assertEquals(59, header.getNodeOffset());
    }

    @Test
    public void testPackageTable_rightHeader_v2() throws Exception {
        PackageTable packageTable =
                PackageTable.fromBytes(TestDataUtils.getTestPackageMapByteBuffer(2));
        PackageTable.Header header = packageTable.getHeader();
        assertEquals(2, header.getVersion());
        assertEquals("mockup", header.getContainer());
        assertEquals(FileType.PACKAGE_MAP, header.getFileType());
        assertEquals(233, header.getFileSize());
        assertEquals(3, header.getNumPackages());
        assertEquals(31, header.getBucketOffset());
        assertEquals(59, header.getNodeOffset());
    }

    @Test
    public void testPackageTable_rightNode() throws Exception {
        PackageTable packageTable =
                PackageTable.fromBytes(TestDataUtils.getTestPackageMapByteBuffer(1));

        PackageTable.Node node1 = packageTable.get("com.android.aconfig.storage.test_1");
        PackageTable.Node node2 = packageTable.get("com.android.aconfig.storage.test_2");
        PackageTable.Node node4 = packageTable.get("com.android.aconfig.storage.test_4");

        assertEquals("com.android.aconfig.storage.test_1", node1.getPackageName());
        assertEquals("com.android.aconfig.storage.test_2", node2.getPackageName());
        assertEquals("com.android.aconfig.storage.test_4", node4.getPackageName());

        assertEquals(0, node1.getPackageId());
        assertEquals(1, node2.getPackageId());
        assertEquals(2, node4.getPackageId());

        assertEquals(0, node1.getBooleanStartIndex());
        assertEquals(3, node2.getBooleanStartIndex());
        assertEquals(6, node4.getBooleanStartIndex());

        assertEquals(159, node1.getNextOffset());
        assertEquals(-1, node2.getNextOffset());
        assertEquals(-1, node4.getNextOffset());

        assertFalse(node1.hasPackageFingerprint());
        assertFalse(node2.hasPackageFingerprint());
        assertFalse(node4.hasPackageFingerprint());
    }

    @Test
    public void testPackageTable_rightNode_v2() throws Exception {
        PackageTable packageTable =
                PackageTable.fromBytes(TestDataUtils.getTestPackageMapByteBuffer(2));

        PackageTable.Node node1 = packageTable.get("com.android.aconfig.storage.test_1");
        PackageTable.Node node2 = packageTable.get("com.android.aconfig.storage.test_2");
        PackageTable.Node node4 = packageTable.get("com.android.aconfig.storage.test_4");

        assertEquals("com.android.aconfig.storage.test_1", node1.getPackageName());
        assertEquals("com.android.aconfig.storage.test_2", node2.getPackageName());
        assertEquals("com.android.aconfig.storage.test_4", node4.getPackageName());

        assertEquals(0, node1.getPackageId());
        assertEquals(1, node2.getPackageId());
        assertEquals(2, node4.getPackageId());

        assertEquals(0, node1.getBooleanStartIndex());
        assertEquals(3, node2.getBooleanStartIndex());
        assertEquals(6, node4.getBooleanStartIndex());

        assertEquals(175, node1.getNextOffset());
        assertEquals(-1, node2.getNextOffset());
        assertEquals(-1, node4.getNextOffset());

        assertTrue(node1.hasPackageFingerprint());
        assertTrue(node2.hasPackageFingerprint());
        assertTrue(node4.hasPackageFingerprint());

        assertEquals(-3197795563119393530L, node1.getPackageFingerprint());
        assertEquals(4431940502274857964L, node2.getPackageFingerprint());
        assertEquals(-2213514155997929241L, node4.getPackageFingerprint());
    }
}
