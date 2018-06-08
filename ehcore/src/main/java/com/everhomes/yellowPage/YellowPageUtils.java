package com.everhomes.yellowPage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.elasticsearch.common.lang3.StringUtils;

import jdk.nashorn.internal.runtime.Context.ThrowErrorManager;

public class YellowPageUtils {

	
	
	
	/**
	 * 从网络Url中下载文件
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static File getFileFormUrl(String urlStr, String fileName, String inputSavePath) throws IOException {
		
		if (StringUtils.isEmpty(urlStr)) {
			return null;
		}
		
		String finalFileName = StringUtils.isEmpty(fileName) ? System.currentTimeMillis() + "" : fileName;
		
		String savePath = inputSavePath;
		if (StringUtils.isBlank(inputSavePath)) {
			savePath = getTmpDirPath();
		}
		
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// 设置超时间为10秒
		conn.setConnectTimeout(10 * 1000);

		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		File file = new File(saveDir + File.separator + finalFileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}

		if (inputStream != null) {
			inputStream.close();
		}

		return file;
	}

	/**
	 * 获取一个缓存目录
	 * @return
	 */
	public static String getTmpDirPath() {
		
		StringBuffer tmpdirBuffer = new StringBuffer(System.getProperty("java.io.tmpdir"));
		Long currentMillisecond = System.currentTimeMillis();
		tmpdirBuffer.append(File.separator);
		tmpdirBuffer.append(currentMillisecond);

		// 附件目录
		File baseDir = new File(tmpdirBuffer.toString());
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}

		return tmpdirBuffer.toString();
	}


	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}

		bos.close();

		return bos.toByteArray();
	}

	
	
}
