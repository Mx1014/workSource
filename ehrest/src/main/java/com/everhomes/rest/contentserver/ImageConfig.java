package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

public class ImageConfig {
	private int width;

	private int height;

	private int gary;

	private int proportion;

	private int rotate;

	private String format;

	private int quality = 75;

	private int x = -1;

	private int y = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getGary() {
		return gary;
	}

	public void setGary(int gary) {
		this.gary = gary;
	}

	public int getProportion() {
		return proportion;
	}

	public void setProportion(int proportion) {
		this.proportion = proportion;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
