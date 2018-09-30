// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>printPriceDTO : 打印价格，参考 {@link com.everhomes.rest.print.PrintSettingPaperSizePriceDTO}</li>
 * <li>copyPriceDTO : 复印价格，参考 {@link com.everhomes.rest.print.PrintSettingPaperSizePriceDTO}</li>
 * <li>colorTypeDTO : 扫描价格，参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * <li>hotline : 咨询电话</li>
 * <li>printCourseList : 打印教程（string）数组，四个步骤</li>
 * <li>scanCopyCourseList : 复印扫描教程,(string)数组，五个步骤</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */

public class GetPrintSettingResponse {
	private PrintSettingPaperSizePriceDTO printPriceDTO;
	private PrintSettingPaperSizePriceDTO copyPriceDTO;
	private PrintSettingColorTypeDTO colorTypeDTO;
	private String hotline;
	@ItemType(String.class)
	private List<String> printCourseList;
	@ItemType(String.class)
	private List<String> scanCopyCourseList;

	public PrintSettingPaperSizePriceDTO getPrintPriceDTO() {
		return printPriceDTO;
	}
	public void setPrintPriceDTO(PrintSettingPaperSizePriceDTO printPriceDTO) {
		this.printPriceDTO = printPriceDTO;
	}
	public PrintSettingPaperSizePriceDTO getCopyPriceDTO() {
		return copyPriceDTO;
	}
	public void setCopyPriceDTO(PrintSettingPaperSizePriceDTO copyPriceDTO) {
		this.copyPriceDTO = copyPriceDTO;
	}
	public PrintSettingColorTypeDTO getColorTypeDTO() {
		return colorTypeDTO;
	}
	public void setColorTypeDTO(PrintSettingColorTypeDTO colorTypeDTO) {
		this.colorTypeDTO = colorTypeDTO;
	}
	public String getHotline() {
		return hotline;
	}
	public void setHotline(String hotline) {
		this.hotline = hotline;
	}
	public List<String> getPrintCourseList() {
		return printCourseList;
	}
	public void setPrintCourseList(List<String> printCourseList) {
		this.printCourseList = printCourseList;
	}
	public List<String> getScanCopyCourseList() {
		return scanCopyCourseList;
	}
	public void setScanCopyCourseList(List<String> scanCopyCourseList) {
		this.scanCopyCourseList = scanCopyCourseList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
