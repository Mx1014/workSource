package com.everhomes.rest.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

/**
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/17.
 */
public class AddContractTemplateCommand {
	
	private Long id;
	
	private Integer namespaceId;
	private Long communityId;
	private String name;


	
	
	private Long managerUid;
	private String managerName;

	private String contact;
	
	private String address;
	
	private Double areaSize;
	
	@NotNull
	private String geoString;
	
	private String description;
	
	private String posterUri;
	

	
	@ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

	private String floorCount;
	private String trafficDescription;

	private String liftDescription;
	private String pmDescription;
	private String parkingLotDescription;
	private String environmentalDescription;
	private String powerDescription;
	private String telecommunicationDescription;
	private String airConditionDescription;
	private String securityDescription;
	private String fireControlDescription;

	private Long generalFormId;
	private Byte customFormFlag;

	private String constructionCompany;
	private Long entryDate;
	private Double height;

	private String buildingNumber;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;

	
	
}
