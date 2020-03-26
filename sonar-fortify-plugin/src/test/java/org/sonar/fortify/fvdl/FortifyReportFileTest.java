/*
 * Fortify Plugin for SonarQube
 * Copyright (C) 2014 Vivien HENRIET and SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.fortify.fvdl;

import com.google.common.io.Closeables;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.MessageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FortifyReportFileTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    FileSystem fs = mock(FileSystem.class);

    @Before
    public void before() throws Exception {
        when(this.fs.baseDir()).thenReturn(this.temp.newFolder());
    }

    @Test
    public void testExists() {
        FortifySensorConfiguration configuration = mock(FortifySensorConfiguration.class);
        when(configuration.getReportPath()).thenReturn("src/test/resources/report/audit.fvdl");

        FileSystem fileSystem = mock(FileSystem.class);
        when(fileSystem.baseDir()).thenReturn(new File(System.getProperty("user.dir")));
        FortifyReportFile fortifyReportFile = new FortifyReportFile(configuration, fileSystem);

        assertTrue(fortifyReportFile.exist());
    }

    @Test
    public void testNotExists() {
        FortifySensorConfiguration configuration = mock(FortifySensorConfiguration.class);
        when(configuration.getReportPath()).thenReturn(null);

        FortifyReportFile fortifyReportFile = new FortifyReportFile(configuration, this.fs);

        assertFalse(fortifyReportFile.exist());
    }

    @Test
    public void testWrongPathExists() {
        FortifySensorConfiguration configuration = mock(FortifySensorConfiguration.class);
        when(configuration.getReportPath()).thenReturn("/do/not/exist/audit.fvdl");

        FortifyReportFile fortifyReportFile = new FortifyReportFile(configuration, this.fs);

        try {
            fortifyReportFile.exist();
            fail("An exception is expected!");
        } catch (MessageException e) {
            // expected
        }
    }

    @Test
    public void testDirExists() {
        FortifySensorConfiguration configuration = mock(FortifySensorConfiguration.class);
        when(configuration.getReportPath()).thenReturn(System.getProperty("user.dir"));

        FortifyReportFile fortifyReportFile = new FortifyReportFile(configuration, this.fs);

        try {
            fortifyReportFile.exist();
            fail("An exception is expected!");
        } catch (MessageException e) {
            // expected
        }
    }

    @Test
    public void testNotExistgetInputStream() throws IOException {
        FortifySensorConfiguration configuration = mock(FortifySensorConfiguration.class);
        when(configuration.getReportPath()).thenReturn(null);

        FortifyReportFile fortifyReportFile = new FortifyReportFile(configuration, this.fs);

        InputStream input = null;
        try {
            input = fortifyReportFile.getInputStream();
            fail("An exception is expected!");
        } catch (FileNotFoundException e) {
            // expected
        } finally {
            Closeables.closeQuietly(input);
        }
    }

    @Test
    public void testFVDLgetInputStream() throws IOException {
        FortifySensorConfiguration configuration = mock(FortifySensorConfiguration.class);
        when(configuration.getReportPath()).thenReturn(System.getProperty("user.dir") + File.separator + "src/test/resources/report/audit.fvdl");

        FortifyReportFile fortifyReportFile = new FortifyReportFile(configuration, this.fs);

        InputStream input = null;
        try {
            input = fortifyReportFile.getInputStream();
        } finally {
            Closeables.closeQuietly(input);
        }
    }

    @Test
    public void testgetInputStreamFromFprFile() {

        FortifyReportFile fortifyReportFile = new FortifyReportFile(null, this.fs);

        File file = new File("C:/Users/jimiao/Desktop/result.fpr");
        InputStream input = null;
        try {
            input = fortifyReportFile.getInputStreamFromFprFile(file);
            // 经测试发现， 会输出xml格式的结果文件
            System.out.println("输出结果：" + IOUtils.toString(input, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closeables.closeQuietly(input);
        }

    }

    /**
     * 测试一下普通的解压zip的方法 获取fpr文件内容是否可行
     */
    @Test
    public void testMygetInputStreamFromFprFile() {

        FortifyReportFile fortifyReportFile = new FortifyReportFile(null, this.fs);

        File file = new File("C:/Users/jimiao/Desktop/result.fpr");
        InputStream input = null;
        try {
            ZipFile zipFile = new ZipFile(file);
            for (Enumeration enumeration = zipFile.entries(); enumeration.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                System.out.println("循环输出:" + zipEntryName);
      /*  之前猜想是因为普通的解压缩软件无法解压fpr文件, 所以认为fpr无法解压，  实际上， 使用代码进行zip解压， 则可以得到完整的解压后目录，
        其中，audit.fvdl文件就是我们需要的结果文件*/
 /*       循环输出:audit.fvdl
        循环输出:audit.fvdl.mac
        循环输出:VERSION
        循环输出:metatable
        循环输出:src-xrefdata/0
        循环输出:src-xrefdata/1
        循环输出:src-xrefdata/2
        循环输出:src-xrefdata/3
        循环输出:src-xrefdata/4
        循环输出:src-xrefdata/index.xml
        循环输出:src-archive/0
        循环输出:src-archive/1
        循环输出:src-archive/2
        循环输出:src-archive/3
        循环输出:src-archive/4
        循环输出:src-archive/index.xml
        循环输出:src-archive/ScanUUID
        循环输出:ExternalMetadata/externalmetadata.xml*/
            }
            input = fortifyReportFile.getInputStreamFromFprFile(file);
            // 经测试发现， 会输出xml格式的结果文件
            System.out.println("输出结果：" + IOUtils.toString(input, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closeables.closeQuietly(input);
        }

    }
}
