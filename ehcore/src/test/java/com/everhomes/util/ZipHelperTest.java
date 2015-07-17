// @formatter:off
package com.everhomes.util;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ZipHelperTest extends TestCase {

    @Ignore @Test
    public void testCompressAndDecompress() {
    	// Commpress file, it should create a new zip file
        String srcPath = "E:/temp/temp/20150413";
        String zipFilePath = "E:/temp/temp/20150413_cmprs.zip";
        File zipFile = new File(zipFilePath);
        if(zipFile.exists()) {
        	zipFile.delete();
        }
        
        try {
        	ZipHelper.compress(srcPath, zipFilePath);
        	Assert.assertTrue("The zip file '" + zipFilePath + "' should be existed.", zipFile.exists());
        } catch(Exception e) {
        	Assert.fail(e.toString());
        }
        
        
        // Decompress file, it should create the directory for decompressed files
        String dstDirPath = "E:/temp/temp/old";
        File srcFile = new File(srcPath);
        File dstDir = new File(dstDirPath, srcFile.getName());
        FileHelper.deleteFile(dstDir);
        try {
        	ZipHelper.decompress(zipFilePath, dstDirPath);
        	Assert.assertTrue("The zip file '" + zipFilePath + "' should be existed.", dstDir.exists());
        	Assert.assertEquals(srcFile.listFiles().length, dstDir.listFiles().length);
        	Assert.assertTrue("The decompressed files should be deleted after test, filePath=" + dstDir, 
        			FileHelper.deleteFile(dstDir));
        } catch(Exception e) {
        	Assert.fail(e.toString());
        }
        Assert.assertTrue("The zip files should be deleted after test, zipFile=" + zipFile, 
    			FileHelper.deleteFile(zipFile));
    }
}

