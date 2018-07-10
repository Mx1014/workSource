//@formatter:off
package com.everhomes.rest.contract;
import java.sql.Timestamp;

/**
* Created by tangcen on 2018/6/6.
*/
import com.everhomes.util.StringHelper;
/**
*<ul>
* <li>namespaceId:域空间id</li>
* <li>contractId:合同编号</li>
* <li>operatorUid: 操作人id</li>
* <li>operatorName: 操作人名称</li>
* <li>opearteTime:操作时间</li>
* <li>opearteType:操作类型（1：增加，2：删除，3：修改）</li>
* <li>content:日志内容</li>
*</ul>
*/
public class ContractEventDTO {
	
	private Integer namespaceId;
	private Long contractId;
	private Long operatorUid;
	private String operatorName;
	private Timestamp opearteTime;
	private Byte opearteType;
	private String content; 
	
	public ContractEventDTO() {
		
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Timestamp getOpearteTime() {
		return opearteTime;
	}

	public void setOpearteTime(Timestamp opearteTime) {
		this.opearteTime = opearteTime;
	}

	public Byte getOpearteType() {
		return opearteType;
	}

	public void setOpearteType(Byte opearteType) {
		this.opearteType = opearteType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}




