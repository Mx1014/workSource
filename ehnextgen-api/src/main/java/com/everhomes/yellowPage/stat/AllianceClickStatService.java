package com.everhomes.yellowPage.stat;

import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.yellowPage.IdNameInfoDTO;
import com.everhomes.rest.yellowPage.ListServiceNamesCommand;
import com.everhomes.rest.yellowPage.stat.ClickTypeDTO;
import com.everhomes.rest.yellowPage.stat.ListClickStatCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailResponse;
import com.everhomes.rest.yellowPage.stat.ListClickStatResponse;
import com.everhomes.rest.yellowPage.stat.ListInterestStatResponse;
import com.everhomes.rest.yellowPage.stat.ListServiceTypeNamesCommand;
import com.everhomes.rest.yellowPage.stat.ListStatCommonCommand;
import com.everhomes.rest.yellowPage.stat.TestClickStatCommand;

public interface AllianceClickStatService {
	
	/**********************埋点统计***************************/
	//统计类型 0-按服务类型统计 1-按服务统计
	public final Byte TYPE_STAT_SEARCH_BY_CATEGORY  = 0;
	public final Byte TYPE_STAT_SEARCH_BY_SERVICE  = 1;
	
	// 排序方式 0-倒序 1-顺序
	public final Byte TYPE_SORT_INTEREST_STAT_DESC  = 0;
	public final Byte TYPE_SORT_INTEREST_STAT_ASC  = 1;
	
	
    //转化率需要计算保留小数
    public static final int CONVERSION_PERCENT_RETAIN_DECIMAL = 2;
    //保留小数时的策略：直接胜率后面的小数
    public static final RoundingMode CONVERSION_PERCENT_ROUNDING_MODE = RoundingMode.DOWN;
	
	ListInterestStatResponse listInterestStat(ListStatCommonCommand cmd);

	ListClickStatResponse listClickStat(ListClickStatCommand cmd);

	ListClickStatDetailResponse listClickStatDetail(ListClickStatDetailCommand cmd);

	void exportClickStatDetail(ListClickStatDetailCommand cmd, HttpServletRequest request, HttpServletResponse resp);

	void exportClickStat(ListClickStatCommand cmd, HttpServletRequest request, HttpServletResponse resp);

	void exportInterestStat(ListStatCommonCommand cmd, HttpServletRequest request, HttpServletResponse resp);

	List<ClickTypeDTO> listClickTypes();

	List<IdNameInfoDTO> listServiceNames(ListServiceNamesCommand cmd);

	List<IdNameInfoDTO> listServiceTypeNames(ListServiceTypeNamesCommand cmd);

	String testClickStat(TestClickStatCommand cmd);
}
