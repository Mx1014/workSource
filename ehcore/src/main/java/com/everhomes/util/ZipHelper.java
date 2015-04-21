package com.everhomes.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
    public static void compress(String srcPath, String zipFilePath) {  
        File srcFile = new File(srcPath);  
        if (!srcFile.exists()) {
        	throw new RuntimeException("The source file/directory '" + srcPath + " is not existed.");  
        }
        
        ZipOutputStream out = null;
        try {  
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);  
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());  
            out = new ZipOutputStream(cos); 
            
            // The base directory for the compressing file when compress recursively,
            // The root base directory is empty.
            String basedir = "";
            compress(srcFile, out, basedir);  
        } catch (Exception e) {  
            throw new RuntimeException("Failed to compress file, path=" + srcPath, e);  
        } finally {
        	FileHelper.closeOuputStream(out);
        }
    }  
  
    /**
     * Compress the file/directory recursively
     * @param file The file/directory need to be compressed
     * @param out The output stream of zip file
     * @param basedir The base directory for the compressing file when compress recursively
     */
    private static void compress(File file, ZipOutputStream out, String basedir) {
    	// The way of compressing a file or directory is different
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);  
        } else { 
            compressFile(file, out, basedir);  
        }  
    }  
  
    /**
     * Compress the directory only, it will compress its subfiles and sub-directories recursively.
     * @param dir The directory need to be compressed
     * @param out The output stream of zip file
     * @param basedir The base directory for the compressing file when compress recursively
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {  
        if (!dir.exists()) {
        	return;  
        }
  
        File[] files = dir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            compress(files[i], out, basedir + dir.getName() + "/");  
        }  
    }  
  
    /**
     * Compress the file only(without directory), it wil compress the file into zip file.
     * @param file The file need to be compressed
     * @param out The output stream of zip file
     * @param basedir The base directory for the compressing file when compress recursively
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {  
        if (!file.exists()) {  
            return;  
        }
        
        BufferedInputStream bis = null;
        try {  
            bis = new BufferedInputStream(new FileInputStream(file));  
            ZipEntry entry = new ZipEntry(basedir + file.getName());  
            out.putNextEntry(entry);  
            int len = -1;  
            byte buf[] = new byte[4096];  
            while ((len = bis.read(buf, 0, buf.length)) != -1) {  
                out.write(buf, 0, len);  
            }
        } catch (Exception e) {  
            throw new RuntimeException("Failed to commpress file, file=" + file, e);  
        } finally {
        	FileHelper.closeInputStream(bis);
        }
    } 
    
    /**
     * Decompress the zip file to the specific directory
     * @param zipFilePath
     * @param dstDirPath
     */
    public static void decompress(String zipFilePath, String dstDirPath) {
    	File distDir = new File(dstDirPath);
    	if(distDir.exists() && distDir.isFile()) {
    		throw new RuntimeException("The dstDirPath should be a directory, dstDirPath=" + dstDirPath);
    	}
    	
    	ZipInputStream zipIn = null;
        try {  
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath)); 
            File fout = null;
            ZipEntry entry = null;  
            while((entry = zipIn.getNextEntry())!=null){
        		fout = new File(dstDirPath, entry.getName());
            	if(entry.isDirectory()) {
            		decompressDirectory(fout, zipIn);
            	} else {
            		decompressFile(fout, zipIn);
            	}
            }
        } catch (Exception e) {  
        	throw new RuntimeException("Failed to decommpress file, file=" + zipFilePath, e); 
        } finally {
        	FileHelper.closeInputStream(zipIn);
        }
    }  
    
    /**
     * Decompress a directory from the zip file
     * @param dir The directory to be decompressed
     * @param zipIn The inputstream of zip file
     */
    private static void decompressDirectory(File dir, ZipInputStream zipIn) {
    	dir.mkdirs();
    }
    
    /**
     * Decompress a file from the zip file
     * @param file The file to be decompressed
     * @param zipIn The inputstream of zip file
     */
    private static void decompressFile(File file, ZipInputStream zipIn) {
    	if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

        BufferedOutputStream out = null;
        try {
        	out = new BufferedOutputStream(new FileOutputStream(file)); 
            int len = -1;  
            byte buf[] = new byte[4096];  
            while ((len = zipIn.read(buf, 0, buf.length)) != -1) {  
                out.write(buf, 0, len);  
            }
        } catch (Exception e) {
        	throw new RuntimeException("Failed to decommpress file, file=" + file, e); 
        } finally {
        	FileHelper.closeOuputStream(out);
        }
    }
}
