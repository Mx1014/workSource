// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型，此处为community, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>paperSizePriceDTO : 打印/复印价格，参考 {@link com.everhomes.rest.print.PrintSettingPaperSizePriceDTO}</li>
 * <li>colorTypeDTO : 扫描价格，参考 {@link com.everhomes.rest.print.PrintSettingColorTypeDTO}</li>
 * <li>hotline : 咨询电话</li>
 * <li>printCourseList : 打印教程数组，四个步骤</li>
 * <li>scanCopyCourseList : 复印扫描教程数组，五个步骤</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class UpdatePrintSettingCommand {
	private String ownerType;
	private Long ownerId;
	private PrintSettingPaperSizePriceDTO paperSizePriceDTO;
	private PrintSettingColorTypeDTO colorTypeDTO;
	private String hotline;
}
