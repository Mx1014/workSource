package com.everhomes.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atomikos.datasource.ResourceException;

public class FileHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
	
	/**
	 * Copy the specific source file to the destination.
	 * @param srcFile source file
	 * @param dstFile destination file
	 * @return true if copy successfully, false otherwise
	 */
	public static boolean copyFile(File srcFile, File dstFile) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		try {
			in = new BufferedInputStream(new FileInputStream(srcFile));
			out = new BufferedOutputStream(new FileOutputStream(dstFile));
			
			readAndWriteStream(in, out);
			
			return true;
		} catch(Exception e) {
			LOGGER.error("Failed to copy file, srcFile=" + srcFile + ", dstFile=" + dstFile, e);
		} finally {
			closeInputStream(in);
			closeOuputStream(out);
		}
		
		return false;
	}
	
	/**
	 * Copy the specific source file to the destination.
	 * @param inStream source file input stream
	 * @param dstFile destination file
	 * @return true if copy successfully, false otherwise
	 */
	public static boolean copyFile(InputStream inStream, File dstFile) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		try {
			in = new BufferedInputStream(inStream);
			out = new BufferedOutputStream(new FileOutputStream(dstFile));
			
			readAndWriteStream(in, out);
			
			return true;
		} catch(Exception e) {
			LOGGER.error("Failed to copy file, dstFile=" + dstFile, e);
		} finally {
			closeOuputStream(out);
		}
		
		return false;
	}
	
	/**
	 * Read the data from inputstream and write to the outputstream
	 * @param in inputstream
	 * @param out outputstream
	 * @throws IOException if an I/O error occurs
	 */
	public static void readAndWriteStream(BufferedInputStream in, BufferedOutputStream out) throws IOException {
		byte[] buf = new byte[4096];
		int len = -1;
		while((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}
	
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
	
	
	 /**
		 * 读取文本数据 生成 List<String[]> 数组数据列表
		 * 
		 * @param files 文件数组, dataSeperator 数据分隔符（从配置表中读取）
		 * 
		 * @return List<String[]> 数据对应的 String 数组
		 * @throws IOException 
		 * @throws ResourceException 
		 */
		public static List<String[]> getDataArrayByFile(File[] files, String dataSeperator) throws ResourceException, IOException 
		{
			List<String[]> dataList = new ArrayList<String[]>();
			if(files != null && files.length > 0)
			{
				for (File file : files)
				{
					dataList.addAll(getDataArrayByFile(file, dataSeperator));
				}
				
			}
			return dataList;
		}
		
		/**
		 * 读取文本数据 生成 List<String[]> 数组数据列表
		 * 
		 * @param filePath 文件路径 , dataSeperator 数据分隔符（从配置表中读取）
		 * 
		 * @return List<String[]> 数据对应的 String 数组
		 */
		public static List<String[]> getDataArrayByFile(File file, String dataSeperator)
		{
			List<String[]> dataList = new ArrayList<String[]>();

			BufferedReader br = null;
			try
			{
				FileInputStream fis = new FileInputStream(file);
				br = new BufferedReader(new InputStreamReader(fis, "UTF8"));
				String st = "";
				while ((st = br.readLine()) != null && !st.equals(""))
				{
					// split : 默认情况下 第二个参数=0 智能去除最后的空值 导致数组长度变小。
					// 第二个参数： 如果是正数：参数将会匹配n次，
					// 第二个参数： 如果是负数：参数将会匹配尽可能多的次数，
					// 如果是0： 参数将会匹配尽可能多的次数，但是会智能去除最后若干个无效项（空值）
					String str[] = st.trim().split(dataSeperator, -1);
					String rowDatas[] = new String[str.length];
					for (int i = 0; i < str.length; i++)
					{
						rowDatas[i] = str[i].trim();

					}
					dataList.add(rowDatas);
				}

			} catch (IOException e)
			{
				LOGGER.error("failed to processor the file .error io", e);
			}

			finally
			{
				closeBufferedReader(br);
			}
			return dataList;
		}
		
		/**
		 * 读取文本数据 生成 List<String[]> 数组数据列表
		 * 
		 * @param filePath 文件路径 , dataSeperator 数据分隔符（从配置表中读取）
		 * 
		 * @return List<String[]> 数据对应的 String 数组
		 */
		public static List<String[]> getDataArrayByInputStream(InputStream is, String dataSeperator)
		{
			List<String[]> dataList = new ArrayList<String[]>();

			BufferedReader br = null;
			try
			{
				br = new BufferedReader(new InputStreamReader(is, "UTF8"));
				String st = "";
				while ((st = br.readLine()) != null && !st.equals(""))
				{
					// split : 默认情况下 第二个参数=0 智能去除最后的空值 导致数组长度变小。
					// 第二个参数： 如果是正数：参数将会匹配n次，
					// 第二个参数： 如果是负数：参数将会匹配尽可能多的次数，
					// 如果是0： 参数将会匹配尽可能多的次数，但是会智能去除最后若干个无效项（空值）
					String str[] = st.trim().split(dataSeperator, -1);
					String rowDatas[] = new String[str.length];
					for (int i = 0; i < str.length; i++)
					{
						rowDatas[i] = str[i].trim();

					}
					dataList.add(rowDatas);
				}

			} catch (IOException e)
			{
				LOGGER.error("failed to processor the file .error io", e);
			}

			finally
			{
				closeBufferedReader(br);
			}
			return dataList;
		}
		
		/**
		 * 关闭缓冲流
		 * 
		 * @param 缓冲输入流
		 * @return
		 */
		public static void closeBufferedReader(BufferedReader br)
		{
			if (br != null)
			{
				try
				{
					br.close();
				} catch (IOException e)
				{
					LOGGER.error("failed to close BufferedReader .BufferedReader = " + br, e);
				}
			}
		}
		
		/**
		 * 关闭缓冲流
		 * 
		 * @param 缓冲输出流
		 * @return
		 */
		public static void closeBufferedWriter(BufferedWriter bw)
		{
			if (bw != null)
			{
				try
				{
					bw.close();
				} catch (IOException e)
				{
					LOGGER.error("failed to close BufferedWriter .BufferedWriter = " + bw, e);
				}
			}
		}
}
