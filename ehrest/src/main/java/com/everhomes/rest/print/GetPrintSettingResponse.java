// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

/**
 * 
 * <ul>
 * <li>paperSizePriceDTO : 打印/复印价格，参考 {@link com.everhomes.rest.print.PrintSettingPaperSizePriceDTO}</li>
 * <li>colorTypeDTO : 扫描价格，参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * <li>hotline : 咨询电话</li>
 * <li>printCourseList : 打印教程（string）数组，四个步骤</li>
 * <li>scanCopyCourseList : 复印扫描教程,(string)数组，五个步骤</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */

public class GetPrintSettingResponse {
	private PrintSettingPaperSizePriceDTO paperSizePriceDTO;
	private PrintSettingColorTypeDTO colorTypeDTO;
	private String hotline;
	private List<String> printCourseList;
	private List<String> scanCopyCourseList;
}
