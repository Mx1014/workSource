package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>name: 名称</li>
 * <li>entryId: 实际入口id</li>
 * <li>publishPrivilege: 活动发布模式，参考 {@link com.everhomes.rest.common.ActivityPublishPrivilegeFlag}</li>
 * <li>livePrivilege: 是否支持直播，参考{@link VideoSupportType}</li>
 * <li>listStyle: 活动列表样式，参考{@link com.everhomes.rest.common.ActivityListStyleFlag}</li>
 * <li>scope：活动可见范围，参考{@link com.everhomes.rest.ui.user.ActivityLocationScope}</li>
 * <li>style: 主题分类项的样式，参考{@link com.everhomes.rest.widget.AssociactionCategoryStyle}</li>
 * <li>categoryFlag: 是否有主题分类 0-无，1-有</li>
 * <li>categoryDTOList: 子分类</li>
 * </ul>
 */
public class ActivityEntryConfigulation {

	private Long id;
	private String name;

	private Long entryId;

	private Byte publishPrivilege;

	private Byte livePrivilege;

	private Byte listStyle;

	private Byte scope;

	private Byte style;

	private Byte categoryFlag;

	@ItemType(ActivityCategoryDTO.class)
	private List<ActivityCategoryDTO> categoryDTOList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getPublishPrivilege() {
		return publishPrivilege;
	}

	public void setPublishPrivilege(Byte publishPrivilege) {
		this.publishPrivilege = publishPrivilege;
	}

	public Byte getLivePrivilege() {
		return livePrivilege;
	}

	public void setLivePrivilege(Byte livePrivilege) {
		this.livePrivilege = livePrivilege;
	}

	public Byte getListStyle() {
		return listStyle;
	}

	public void setListStyle(Byte listStyle) {
		this.listStyle = listStyle;
	}

	public Byte getScope() {
		return scope;
	}

	public void setScope(Byte scope) {
		this.scope = scope;
	}

	public Byte getStyle() {
		return style;
	}

	public void setStyle(Byte style) {
		this.style = style;
	}

	public Byte getCategoryFlag() {
		return categoryFlag;
	}

	public void setCategoryFlag(Byte categoryFlag) {
		this.categoryFlag = categoryFlag;
	}

	public List<ActivityCategoryDTO> getCategoryDTOList() {
		return categoryDTOList;
	}

	public void setCategoryDTOList(List<ActivityCategoryDTO> categoryDTOList) {
		this.categoryDTOList = categoryDTOList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
