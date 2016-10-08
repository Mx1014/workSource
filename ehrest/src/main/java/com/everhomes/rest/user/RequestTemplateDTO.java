package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id:模板id</li>
 *  <li>templateType:模板类型</li>
 *  <li>name:模板名称</li>
 *  <li>buttonTitle:按钮名称</li>
 *  <li>emailFlag:是否邮箱推送 0 否， 1 是</li>
 *  <li>msgFlag:是否消息推送 0 否， 1 是</li>
 *  <li>dtos:模板字段列表 参考{@link com.everhomes.rest.user.FieldDTO}</li>
 * </ul>
 */
public class RequestTemplateDTO {
	
	private Long id;
	
	private String templateType;

	private String name;
	
	private String buttonTitle;

	private Byte emailFlag;
	
	private Byte msgFlag;
	@ItemType(FieldDTO.class)
	private List<FieldDTO> dtos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getButtonTitle() {
		return buttonTitle;
	}

	public void setButtonTitle(String buttonTitle) {
		this.buttonTitle = buttonTitle;
	}

	public Byte getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(Byte emailFlag) {
		this.emailFlag = emailFlag;
	}

	public Byte getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(Byte msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FieldDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<FieldDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
