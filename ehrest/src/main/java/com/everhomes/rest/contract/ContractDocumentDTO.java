package com.everhomes.rest.contract;
/**
 * <ul>
 * <li>id: 合同文档id</li>
 * <li>content: 合同文档内容</li>
 * </ul>
 */
public class ContractDocumentDTO {
	
	private Long id;
	private String content;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

}
