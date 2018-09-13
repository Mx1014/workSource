package com.everhomes.rest.yellowPage.stat;


/**
 * <ul>
 * <li>type:服务联盟类型</li>
 * <li>ownerId:归属项目id</li>
 * <li>categoryId: 服务类型id</li>
 * <li>searchType: 0-根据服务类型做统计 1-统计服务</li>
 * <li>startDate: 开始日期，默认一个月前</li>
 * <li>endDate: 结束日期，默认当天的前一天</li>
 * <li>sortType: 排序类型 根据获取的clickType填充</li>
 * <li>sortOrder: 0-降序 1-升序</li>
 * <li>pageAnchor: 下一页锚点（新版本使用）</li>
 * <li>pageSize: 每页的数量</li>
 * <li>currentPMId: 管理公司id</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 应用的originId</li>
 * </ul>
 */
public class ListClickStatCommand extends ListStatCommonCommand{
	
	private Byte sortType;

	public Byte getSortType() {
		return sortType;
	}

	public void setSortType(Byte sortType) {
		this.sortType = sortType;
	}



}
