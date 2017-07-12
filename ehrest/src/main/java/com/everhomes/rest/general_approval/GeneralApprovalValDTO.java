package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class GeneralApprovalValDTO {
    private Long     approvalId;
    private String     dataSourceType;
    private String     fieldType;
    private Long     fieldInt1;
    private Long     fieldInt2;
    private Integer     namespaceId;
    private String     fieldStr2;
    private String     fieldStr3;
    private String     fieldStr1;
    private Long     formOriginId;
    private String     fieldName;
    private Long     formVersion;
    private Byte     valType;
    private Long     fieldInt3;
    private Long     id;
    private Long     requestId;

    public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Long getFieldInt1() {
		return fieldInt1;
	}

	public void setFieldInt1(Long fieldInt1) {
		this.fieldInt1 = fieldInt1;
	}

	public Long getFieldInt2() {
		return fieldInt2;
	}

	public void setFieldInt2(Long fieldInt2) {
		this.fieldInt2 = fieldInt2;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getFieldStr2() {
		return fieldStr2;
	}

	public void setFieldStr2(String fieldStr2) {
		this.fieldStr2 = fieldStr2;
	}

	public String getFieldStr3() {
		return fieldStr3;
	}

	public void setFieldStr3(String fieldStr3) {
		this.fieldStr3 = fieldStr3;
	}

	public String getFieldStr1() {
		return fieldStr1;
	}

	public void setFieldStr1(String fieldStr1) {
		this.fieldStr1 = fieldStr1;
	}

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Long getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Long formVersion) {
		this.formVersion = formVersion;
	}

	public Byte getValType() {
		return valType;
	}

	public void setValType(Byte valType) {
		this.valType = valType;
	}

	public Long getFieldInt3() {
		return fieldInt3;
	}

	public void setFieldInt3(Long fieldInt3) {
		this.fieldInt3 = fieldInt3;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

