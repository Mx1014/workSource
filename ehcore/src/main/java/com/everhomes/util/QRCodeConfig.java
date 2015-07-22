package com.everhomes.util;

import java.awt.image.BufferedImage;

public class QRCodeConfig {
	/** 字符集：UTF8 */
	public static final String CHARSET_UTF8 = "utf8";
	
	/** 二维码图片格式：PNG */
	public static final String FORMAT_PNG = "png";
	
	/** 二维码图片格式：JPG */
	public static final String FORMAT_JPG = "jpg";
	
	/** 颜色值：黑色 */
	public static final int BLACK = 0xFF000000;
	
	/** 颜色值：白色 */
	public static final int WHITE = 0xFFFFFFFF;

	private final int onColor;
	private final int offColor;

	/**
	 * Creates a default config with on color {@link #BLACK} and off color
	 * {@link #WHITE}, generating normal black-on-white barcodes.
	 */
	public QRCodeConfig() {
		this(BLACK, WHITE);
	}

	/**
	 * @param onColor
	 *            pixel on color, specified as an ARGB value as an int
	 * @param offColor
	 *            pixel off color, specified as an ARGB value as an int
	 */
	public QRCodeConfig(int onColor, int offColor) {
		this.onColor = onColor;
		this.offColor = offColor;
	}

	public int getPixelOnColor() {
		return onColor;
	}

	public int getPixelOffColor() {
		return offColor;
	}

	public int getBufferedImageColorModel() {
		// Use faster BINARY if colors match default
		return onColor == BLACK && offColor == WHITE ? BufferedImage.TYPE_BYTE_BINARY : BufferedImage.TYPE_INT_RGB;
	}
}
