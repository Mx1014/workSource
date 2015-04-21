package com.everhomes.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
	
	/**
	 * Delete the file. If the file denotes a directory, it will delete its children file recursively.
	 * @param filePath The file path which denotes the file to be deleted
	 * @return if and only if the file or directory is successfully deleted; false otherwise
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		
		return deleteFile(file);
	}
	
	/**
	 * Delete the file. If the file denotes a directory, it will delete its children file recursively.
	 * @param file The file to be deleted
	 * @return true if and only if the file or directory is successfully deleted; false otherwise
	 */
	public static boolean deleteFile(File file) {
		if(file != null) {
			if(file.isDirectory()) {
				// Delete children files first
				File[] childFiles = file.listFiles();
				boolean result = true;
				for(File childFile : childFiles) {
					result = deleteFile(childFile);
					if(!result) {
						return result;
					}
				}
				
				// Delete the directory after all the children files are deleted
				return file.delete();
			} else {
				return file.delete();
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Closes the specific input stream and releases any system resources associated with the stream.
	 * @param in The specific inputstream object
	 */
	public static void closeInputStream(InputStream in) {
		if(in != null) {
			try {
				in.close();
			} catch(Exception e) {
				LOGGER.error("Failed to close the input stream", e);
			}
		}
	}
	
	/**
	 * Closes the specific output stream and releases any system resources associated with the stream.
	 * @param out The specific outputstream object
	 */
	public static void closeOuputStream(OutputStream out) {
		if(out != null) {
			try {
				out.close();
			} catch(Exception e) {
				LOGGER.error("Failed to close the output stream", e);
			}
		}
	}
}
