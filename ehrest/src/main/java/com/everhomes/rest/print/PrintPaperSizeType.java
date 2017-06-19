// @formatter:off
package com.everhomes.rest.print;

/**
* 
* <ul>
* <li>A3(3) : A3纸张</li>
* <li>A4(4) : A4纸张</li>
* <li>A5(5) : A5纸张</li>
* <li>A6(6) : A6纸张</li>
* <li>OTHER_PAPER_SIZE(0) : 其他纸张</li>
* </ul>
*
*  @author:dengs 2017年6月16日
*/
public enum PrintPaperSizeType {
	A3((byte)3),A4((byte)4),A5((byte)5),A6((byte)6),OTHER_PAPER_SIZE((byte)0);
	
	private byte code;

	private PrintPaperSizeType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PrintPaperSizeType fromCode(byte code) {
		for (PrintPaperSizeType t : PrintPaperSizeType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}

