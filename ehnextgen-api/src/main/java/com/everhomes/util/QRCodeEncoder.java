package com.everhomes.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeEncoder
{

	/**
	 * 生成二维码图片[带logo小图片]，默认内容编码字符集、图片格式和图片前背景色,图片大小,格式png。
	 *
	 * @param content 二维码文字内容
	 * @throws WriterException 如果对二维码进行编码出错则抛该异常
	 * @throws IOException 如果写文件出错则抛该异常
	 * @return 二进制流
	 */
	public static ByteArrayOutputStream createSimpleQrCode(String content) throws IOException, WriterException {
		BufferedImage image = createQrCode(content, 270, 270, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, QRCodeConfig.FORMAT_PNG, out);
		return out;
	}
	/**
	 * 生成二维码图片[带logo小图片]，默认内容编码字符集、图片格式和图片前背景色。
	 * 
	 * @param content 二维码文字内容
	 * @param width 二维码图片宽度
	 * @param height 二维码图片高度
	 * @param imageSavePath 二维码保存路径
	 * @param avatar logo小图片路径
	 * @throws WriterException 如果对二维码进行编码出错则抛该异常
	 * @throws IOException 如果写文件出错则抛该异常
	 */
	public static BufferedImage createQrCode(String content, int width, int height, String logoPath)
			throws IOException, WriterException {
		String charset = QRCodeConfig.CHARSET_UTF8;
		String format = QRCodeConfig.FORMAT_PNG;
		int fontColor = QRCodeConfig.BLACK;
		// 如果背景是纯白色（0xFFFFFFFF）时，中间的logo图标也会被写成黑色，该问题尚未弄清楚，故先用非纯白来规避 by lqs 20131106
		// int bgColor = QRCodeConfig.WHITE;
		int bgColor = 0xEEFFFFFF;
		return createQrCode(content, charset, width, height, logoPath, format, fontColor, bgColor);
	}

	/**
	 * 生成二维码图片[带logo小图片]
	 * @param content 二维码文字内容
	 * @param charset 对内容进行编码的字符编码形式
	 * @param width 二维码图片宽度
	 * @param height 二维码图片高度
	 * @param imageSavePath 二维码保存路径
	 * @param avatar logo小图片路径
	 * @param format 二维码图片格式，jpg/png等等
	 * @param fontColor 字体颜色
	 * @param bgColor 二维码图片背景色
	 * @throws WriterException 如果对二维码进行编码出错则抛该异常
	 * @throws IOException 如果写文件出错则抛该异常
	 */
	public static BufferedImage createQrCode(String content, String charset, int width, int height, String logoPath,
			String format, int fontColor, int bgColor) throws IOException, WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 设置字符编码
		hints.put(EncodeHintType.CHARACTER_SET, charset);
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage image = toBufferedImage(matrix, new QRCodeConfig(fontColor, bgColor));
        // 添加logo图片
        overlapImage(image, logoPath);
        
        return image;
	}

	/**
	 * 把图片写入到磁盘文件，中间添加logo图片
	 * @param @param matrix
	 * @param @param format
	 * @param @param imagePath
	 * @param @throws IOException
	 * @throws
	 */
	public static void writeToFile(BitMatrix matrix, String formate, String imageSavePath, String logoPath,
			int fontColor, int bgColor) throws IOException {
		BufferedImage image = toBufferedImage(matrix, new QRCodeConfig(fontColor, bgColor));
		// 添加logo图片
		overlapImage(image, logoPath);

		File imageFile = new File(imageSavePath);
		if(!imageFile.getParentFile().exists()) {
		    imageFile.getParentFile().mkdirs();
        }
		
		ImageIO.write(image, formate, imageFile);
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix, QRCodeConfig config) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
		int onColor = config.getPixelOnColor();
		int offColor = config.getPixelOffColor();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
			}
		}
		
		return image;
	}

	/**
	 * 在二维码图片中添加自定义logo
	 * 
	 * @param qrcodeImage 二维码图片
	 * @param avatar logo图片路径
	 * @throws IOException
	 */
	public static void overlapImage(BufferedImage qrcodeImage, String logoPath) throws IOException {
		if(logoPath == null) {
			return;
		}
		
		// logo文件不存在时，则不添加log
		File logoFile = new File(logoPath);
		if (!logoFile.exists()) {
			return;
		}

		Graphics2D g = null;
		try {
			BufferedImage logo = ImageIO.read(logoFile);
			g = qrcodeImage.createGraphics();
			// logo宽高
			int width = qrcodeImage.getWidth() / 5;
			int height = qrcodeImage.getHeight() / 5;
			// logo起始位置，此目的是为logo居中显示
			int x = (qrcodeImage.getWidth() - width) / 2;
			int y = (qrcodeImage.getHeight() - height) / 2;
			g.drawImage(logo, x, y, width, height, null);
		} finally {
			if (g != null)
			{
				g.dispose();
			}
		}
	}
}
