package com.everhomes.investmentAd;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.investment.InvitedCustomerService;
import com.everhomes.investmentAd.InvestmentAdService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.investmentAd.InvestmentAdDetailDTO;
import com.everhomes.rest.investmentAd.InvestmentAdErrorCode;
import com.everhomes.rest.investmentAd.InvestmentAdGeneralStatus;
import com.everhomes.rest.investmentAd.InvestmentAdOrderDTO;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.BuildingAdminStatus;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;
import com.everhomes.rest.investment.CreateInvitedCustomerCommand;
import com.everhomes.rest.investment.CustomerContactDTO;
import com.everhomes.rest.investment.CustomerContactType;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.CustomerRequirementAddressDTO;
import com.everhomes.rest.investment.CustomerRequirementDTO;
import com.everhomes.rest.investment.InvitedCustomerDTO;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.investmentAd.ChangeInvestmentAdOrderCommand;
import com.everhomes.rest.investmentAd.ChangeInvestmentStatusCommand;
import com.everhomes.rest.investmentAd.CreateInvestmentAdCommand;
import com.everhomes.rest.investmentAd.DeleteInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetRelatedAssetsCommand;
import com.everhomes.rest.investmentAd.IntentionCustomerCommand;
import com.everhomes.rest.investmentAd.InvestmentAdAssetType;
import com.everhomes.rest.investmentAd.InvestmentAdBannerDTO;
import com.everhomes.rest.investmentAd.InvestmentAdDTO;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdResponse;
import com.everhomes.rest.investmentAd.NewIntentionCustomerDTO;
import com.everhomes.rest.investmentAd.RelatedAssetDTO;
import com.everhomes.rest.investmentAd.UpdateInvestmentAdCommand;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.expansion.IntentionCustomerDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.expansion.EnterpriseApplyEntryProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;

@Component
public class InvestmentAdServiceImpl implements InvestmentAdService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdServiceImpl.class);
	
	@Autowired
	private GeneralFormService generalFormService;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private GeneralFormValProvider generalFormValProvider;
	
	@Autowired
	private InvestmentAdProvider investmentAdProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;
	
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	
	@Autowired
	private GeneralFormProvider generalFormProvider;
	
	@Autowired
	private InvitedCustomerService invitedCustomerService;
	
	@Autowired
	protected UserPrivilegeMgr userPrivilegeMgr;
	
	
	
	@Override
	public void createInvestmentAd(CreateInvestmentAdCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_ADVERTISEMENT_CREATE, cmd.getOrganizationId(), cmd.getCommunityId());
		
		//创建招商广告
		InvestmentAd investmentAd = ConvertHelper.convert(cmd,InvestmentAd.class);
		if (cmd.getLatitude()!=null && cmd.getLongitude()!=null) {
			String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
			investmentAd.setGeohash(geohash);
		}
		investmentAd.setStatus(InvestmentAdGeneralStatus.ACTIVE.getCode());
		Long investmentAdId = investmentAdProvider.createInvestmentAd(investmentAd);
		//记录招商广告关联的资产
		addInvestmentAdAssets(cmd.getRelatedAssets(),investmentAd);
		//记录招商广告关联的轮播图
		addInvestmentAdBanners(cmd.getBanners(), investmentAd);
		//处理自定义表单
		if (cmd.getCustomFormFlag()!=null && cmd.getCustomFormFlag()==1) {
			addInvestmentAdGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(),investmentAdId);
		}
	}

	@Override
	public void deleteInvestmentAd(DeleteInvestmentAdCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_ADVERTISEMENT_DELETE, cmd.getOrganizationId(), cmd.getCommunityId());
		
		InvestmentAd investmentAd = investmentAdProvider.findInvestmentAdById(cmd.getId());
		if (investmentAd == null) {
			LOGGER.error("investmentAd not found, cmd={}", cmd);
			throw errorWith(InvestmentAdErrorCode.SCOPE, InvestmentAdErrorCode.INVESTMENTAD_NOT_FOUND,"investmentAd not found.");
		}
		investmentAdProvider.deleteInvestmentAdById(investmentAd.getId());
	}
	
	@Override
	public void updateInvestmentAd(UpdateInvestmentAdCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_ADVERTISEMENT_UPDATE, cmd.getOrganizationId(), cmd.getCommunityId());
		
		InvestmentAd existInvestmentAd = investmentAdProvider.findInvestmentAdById(cmd.getId());
		if (existInvestmentAd == null) {
			LOGGER.error("investmentAd not found, cmd={}", cmd);
			throw errorWith(InvestmentAdErrorCode.SCOPE, InvestmentAdErrorCode.INVESTMENTAD_NOT_FOUND,"investmentAd not found.");
		}
		
		InvestmentAd investmentAd = ConvertHelper.convert(cmd,InvestmentAd.class);
		if (cmd.getLatitude()!=null && cmd.getLongitude()!=null) {
			String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
			investmentAd.setGeohash(geohash);
		}
		investmentAd.setCreateTime(existInvestmentAd.getCreateTime());
		investmentAd.setCreatorUid(existInvestmentAd.getCreatorUid());
		investmentAd.setDefaultOrder(existInvestmentAd.getDefaultOrder());
		investmentAd.setStatus(existInvestmentAd.getStatus());
		investmentAdProvider.updateInvestmentAd(investmentAd);
		//更新招商广告关联的资产
		investmentAdProvider.deleteInvestmentAdAssetByInvestmentAdId(investmentAd.getId());
		addInvestmentAdAssets(cmd.getRelatedAssets(),investmentAd);
		//更新招商广告关联的轮播图
		investmentAdProvider.deleteInvestmentAdBannerByInvestmentAdId(investmentAd.getId());
		addInvestmentAdBanners(cmd.getBanners(), investmentAd);
		//处理自定义表单
		generalFormValProvider.deleteGeneralFormVals(EntityType.INVESTMENT_ADVERTISEMENT.getCode(), investmentAd.getId());
		if (cmd.getCustomFormFlag() != null && cmd.getCustomFormFlag() == 1) {
			addInvestmentAdGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(),investmentAd.getId());
		}
	}

	@Override
	public ListInvestmentAdResponse listInvestmentAds(ListInvestmentAdCommand cmd) {
		ListInvestmentAdResponse response = new ListInvestmentAdResponse();
		
		if(cmd.getPageAnchor() == null){
			cmd.setPageAnchor(0L);
		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageSize(pageSize);
		
		List<InvestmentAd> investmentAds = investmentAdProvider.listInvestmentAds(cmd);
		if (investmentAds != null && investmentAds.size() > 0) {
			if (investmentAds.size() > cmd.getPageSize()) {
				investmentAds.remove(investmentAds.size()-1);
				response.setNextPageAnchor(cmd.getPageAnchor() + cmd.getPageSize().longValue());
			}
			
			List<InvestmentAdDTO> advertisements = new ArrayList<>();
			for(InvestmentAd investmentAd : investmentAds){
				InvestmentAdDTO dto = convertToInvestmentAdDTO(investmentAd);
				advertisements.add(dto);
			}
			response.setAdvertisements(advertisements);
		}
		return response;
	}

	@Override
	public InvestmentAdDetailDTO getInvestmentAd(GetInvestmentAdCommand cmd) {
		InvestmentAd investmentAd = investmentAdProvider.findInvestmentAdById(cmd.getId());
		if (investmentAd == null) {
			LOGGER.error("investmentAd not found, cmd={}", cmd);
			throw errorWith(InvestmentAdErrorCode.SCOPE, InvestmentAdErrorCode.INVESTMENTAD_NOT_FOUND,"investmentAd not found.");
		}
		
		InvestmentAdDetailDTO dto = ConvertHelper.convert(investmentAd, InvestmentAdDetailDTO.class);
		//设置封面图url
		Long userId = UserContext.currentUserId();
		if (null != dto.getPosterUri()) {
			dto.setPosterUrl(contentServerService.parserUri(dto.getPosterUri(), EntityType.USER.getCode(), userId));
		}
		//处理招商广告关联的轮播图
		List<InvestmentAdBanner> investmentAdBanners = investmentAdProvider.findBannersByInvestmentAdId(investmentAd.getId());
		if (investmentAdBanners != null && investmentAdBanners.size() > 0) {
			List<InvestmentAdBannerDTO> banners = new ArrayList<>();
			for (InvestmentAdBanner investmentAdBanner : investmentAdBanners) {
				InvestmentAdBannerDTO bannerDTO = new InvestmentAdBannerDTO();
				bannerDTO.setContentUri(investmentAdBanner.getContentUri());
				bannerDTO.setContentUrl(contentServerService.parserUri(investmentAdBanner.getContentUri(), EntityType.USER.getCode(), userId));
				banners.add(bannerDTO);
			}
			dto.setBanners(banners);
		}
		//处理招商广告关联的资产
		List<InvestmentAdAsset> investmentAdAssets = investmentAdProvider.findAssetsByInvestmentAdId(investmentAd.getId());
		if (investmentAdAssets != null && investmentAdAssets.size() > 0) {
			List<RelatedAssetDTO> relatedAssets = new ArrayList<>();
			for (InvestmentAdAsset investmentAdAsset : investmentAdAssets) {
				//如果是已出租的房源或者是被删除的房源则不再显示
				Boolean available = checkAssetStatus(investmentAdAsset,investmentAd.getOwnerId());
				if (available) {
					RelatedAssetDTO assetDTO = convertToAssetDTO(investmentAdAsset);
					relatedAssets.add(assetDTO);
				}
			}
			dto.setRelatedAssets(relatedAssets);
		}
		//处理自定义表单
		if (investmentAd.getCustomFormFlag() != null && investmentAd.getCustomFormFlag() == 1) {
			dto.setFormValues(getInvestmentAdGeneralFormValues(investmentAd.getId()));
		}
		return dto;
	}

	@Override
	public void changeInvestmentAdOrder(ChangeInvestmentAdOrderCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_ADVERTISEMENT_CHANGE_ORDER, cmd.getOrganizationId(), cmd.getCommunityId());
		
		if (cmd.getInvestmentAdOrders() != null && cmd.getInvestmentAdOrders().size() > 0) {
			List<InvestmentAdOrderDTO> investmentAdOrders = cmd.getInvestmentAdOrders();
			
			List<Long> investmentAdIds = new ArrayList<>();
			Map<Long, Long> newInvestmentAdIdOrderMap = new HashMap<>();
			investmentAdOrders.stream().forEach(r->{
				investmentAdIds.add(r.getInvestmentAdId());
				newInvestmentAdIdOrderMap.put(r.getInvestmentAdId(), r.getDefaultOrder());
			});
			
			Map<Long, InvestmentAd> InvestmentAdMap = investmentAdProvider.mapIdAndInvestmentAd(investmentAdIds);
			for (Long investmentAdId : investmentAdIds) {
				Long newOrder = newInvestmentAdIdOrderMap.get(investmentAdId);
				InvestmentAd investmentAd = InvestmentAdMap.get(investmentAdId);
				Long oldOrder = investmentAd.getDefaultOrder();
				if (!newOrder.equals(oldOrder)) {
					investmentAd.setDefaultOrder(newOrder);
					investmentAdProvider.updateInvestmentAd(investmentAd);
				}
			}
		}
	}
	
	@Override
	public void exportInvestmentAds(ListInvestmentAdCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_ADVERTISEMENT_EXPORT, cmd.getOrganizationId(), cmd.getCommunityId());

		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("ListInvestmentAdCommand", cmd);
		
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if (community == null) {
			LOGGER.error("Community does not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, "Community is not exist.");
		}
		String fileName = String.format("房源招商广告信息_%s_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), InvestmentAdsExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}
	
	@Override
	public void changeInvestmentStatus(ChangeInvestmentStatusCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_ADVERTISEMENT_UPDATE, cmd.getOrganizationId(), cmd.getCommunityId());
		investmentAdProvider.changeInvestmentStatus(cmd.getId(),cmd.getInvestmentStatus());
	}
	
	@Override
	public List<Long> transformToCustomer(IntentionCustomerCommand cmd) {
		checkPrivilegeAuth(cmd.getNamespaceId(), PrivilegeConstants.INVESTMENT_APPLY_TRANSFORM_TO_CUSTOMER, cmd.getOrganizationId(), cmd.getCommunityId());
		List<NewIntentionCustomerDTO> intentionCustomers = cmd.getIntentionCustomers();
		
		Map<String, CreateInvitedCustomerCommand> finalCommandMap = new HashMap<>();
		for (NewIntentionCustomerDTO dto : intentionCustomers) {
			if (finalCommandMap.containsKey(dto.getCustomerName())) {
				CreateInvitedCustomerCommand cmd2 = finalCommandMap.get(dto.getCustomerName());
				//企业客户意向房源
				//TODO 得考虑addressId重复的问题,目前招商客户管理和此处都未考虑该问题，2018年9月20日17:23:08
				CustomerRequirementDTO requirement = cmd2.getRequirement();
				List<CustomerRequirementAddressDTO> addresses = requirement.getAddresses();
				List<Long> addressIds = dto.getAddressIds();
				if (addressIds!=null && addressIds.size()>0) {
					for (Long addressId : addressIds) {
						//如果不存在addressId，有时addressId前端会传0
						if (addressId != null && addressId != 0) {
							CustomerRequirementAddressDTO addressDTO = new CustomerRequirementAddressDTO();
							addressDTO.setAddressId(addressId);
							addresses.add(addressDTO);
						}
					}
				}
				requirement.setAddresses(addresses);
				//企业客户联系人
				//TODO 得考虑联系人重复的问题，目前招商客户管理和此处都未考虑该问题，2018年9月20日17:23:12
				List<CustomerContactDTO> contacts = cmd2.getContacts();
				CustomerContactDTO contactDTO = new  CustomerContactDTO();
				contactDTO.setName(dto.getApplyUserName());
				contactDTO.setPhoneNumber(dto.getApplyContact());
				contactDTO.setContactType(CustomerContactType.CUSTOMER_CONTACT.getCode());
				contactDTO.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
				contacts.add(contactDTO);
				
				generalFormProvider.updateInvestmentAdApplyTransformStatus(dto.getApplyEntryId(), 1L);
			}else {
				CreateInvitedCustomerCommand cmd2 = new CreateInvitedCustomerCommand();
				//企业客户
				cmd2.setName(dto.getCustomerName());
				cmd2.setLevelItemId((long)CustomerLevelType.INTENTIONAL_CUSTOMER.getCode());
				cmd2.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
				//企业客户意向房源
				List<CustomerRequirementAddressDTO> addressDTOs = new ArrayList<>();
				CustomerRequirementDTO requirementDTO = new CustomerRequirementDTO();
				List<Long> addressIds = dto.getAddressIds();
				if (addressIds!=null && addressIds.size()>0) {
					for (Long addressId : addressIds) {
						//如果不存在addressId，有时addressId前端会传0
						if (addressId != null && addressId != 0) {
							CustomerRequirementAddressDTO addressDTO = new CustomerRequirementAddressDTO();
							addressDTO.setAddressId(addressId);
							addressDTOs.add(addressDTO);
						}
					}
				}
				requirementDTO.setAddresses(addressDTOs);
				cmd2.setRequirement(requirementDTO);
				//企业客户联系人
				List<CustomerContactDTO> contacts = new ArrayList<>();
				CustomerContactDTO contactDTO = new  CustomerContactDTO();
				contactDTO.setName(dto.getApplyUserName());
				contactDTO.setPhoneNumber(dto.getApplyContact());
				contactDTO.setContactType(CustomerContactType.CUSTOMER_CONTACT.getCode());
				contactDTO.setCustomerSource(InvitedCustomerType.INVITED_CUSTOMER.getCode());
				contacts.add(contactDTO);
				cmd2.setContacts(contacts);
				
				finalCommandMap.put(dto.getCustomerName(), cmd2);
				
				generalFormProvider.updateInvestmentAdApplyTransformStatus(dto.getApplyEntryId(), 1L);
			}
		}
		
		List<Long> customerIds = new ArrayList<>(); 
		Set<String> customerNameSet = finalCommandMap.keySet();
		for (String customerName : customerNameSet) {
			CreateInvitedCustomerCommand createInvitedCustomerCommand = finalCommandMap.get(customerName);
			createInvitedCustomerCommand.setNamespaceId(cmd.getNamespaceId());
			createInvitedCustomerCommand.setCommunityId(cmd.getCommunityId());
			createInvitedCustomerCommand.setOrgId(cmd.getOrganizationId());
			InvitedCustomerDTO invitedCustomer = invitedCustomerService.createInvitedCustomerWithoutAuth(createInvitedCustomerCommand);
			customerIds.add(invitedCustomer.getId());
		}
		return customerIds;
	}
	
	@Override
	public List<RelatedAssetDTO> getRelatedAssets(GetRelatedAssetsCommand cmd) {
		List<RelatedAssetDTO> relatedAssets = new ArrayList<>();
		List<InvestmentAdAsset> investmentAdAssets = investmentAdProvider.findAssetsByInvestmentAdId(cmd.getId());
		if (investmentAdAssets != null && investmentAdAssets.size() > 0) {
			for (InvestmentAdAsset investmentAdAsset : investmentAdAssets) {
				//如果是已出租的房源或者是被删除的房源则不再显示
				Boolean available = checkAssetStatus(investmentAdAsset,cmd.getOrganizationId());
				if (available) {
					RelatedAssetDTO assetDTO = convertToAssetDTO(investmentAdAsset);
					relatedAssets.add(assetDTO);
				}
			}
		}
		return relatedAssets;
	}
	
	private InvestmentAdDTO convertToInvestmentAdDTO(InvestmentAd investmentAd) {
		InvestmentAdDTO dto = new InvestmentAdDTO();
		dto = ConvertHelper.convert(investmentAd, InvestmentAdDTO.class);
//		dto.setId(investmentAd.getId());
//		dto.setTitle(investmentAd.getTitle());
//		dto.setInvestmentStatus(investmentAd.getInvestmentStatus());
//		dto.setAvailableAreaMin(investmentAd.getAvailableAreaMin());
//		dto.setAvailableAreaMax(investmentAd.getAvailableAreaMax());
//		dto.setPriceUnit(investmentAd.getPriceUnit());
//		dto.setAssetPriceMin(investmentAd.getAssetPriceMin());
//		dto.setAssetPriceMax(investmentAd.getAssetPriceMax());
//		dto.setApartmentFloorMin(investmentAd.getApartmentFloorMin());
//		dto.setApartmentFloorMax(investmentAd.getApartmentFloorMax());
//		dto.setOrientation(investmentAd.getOrientation());
//		dto.setCreateTime(investmentAd.getCreateTime());
//		dto.setDefaultOrder(investmentAd.getDefaultOrder());
//		dto.setPosterUri(investmentAd.getPosterUri());
//		dto.setInvestmentType(investmentAd.getInvestmentType());
		
		Long userId = UserContext.currentUserId();
		if (null != dto.getPosterUri()) {
			dto.setPosterUrl(contentServerService.parserUri(dto.getPosterUri(), EntityType.USER.getCode(), userId));
		}
		return dto;
	}
	
	private void addInvestmentAdAssets(List<RelatedAssetDTO> relatedAssets,InvestmentAd investmentAd){
		if (relatedAssets != null && relatedAssets.size() > 0) {
			for (RelatedAssetDTO assetDTO : relatedAssets) {
				InvestmentAdAsset adAsset = new InvestmentAdAsset();
				adAsset.setNamespaceId(investmentAd.getNamespaceId());
				adAsset.setAdvertisementId(investmentAd.getId());
				adAsset.setStatus(InvestmentAdGeneralStatus.ACTIVE.getCode());
				if (assetDTO.getApartmentId() != null) {
					adAsset.setAssetType(InvestmentAdAssetType.APARTMENT.getCode());
					adAsset.setAssetId(assetDTO.getApartmentId());
				}else{
					adAsset.setAssetType(InvestmentAdAssetType.BUILDING.getCode());
					adAsset.setAssetId(assetDTO.getBuildingId());
				}
				investmentAdProvider.createInvestmentAdAsset(adAsset);
			}
		}
	}
	
	private Boolean checkAssetStatus(InvestmentAdAsset investmentAdAsset,Long organizationId) {
		//资产类型是楼宇时，如果楼宇下没有待租状态的房源了，则不在招商广告中显示
		//资产类型是房源时，如果房源不是待租状态，则不在招商广告中显示
		if (InvestmentAdAssetType.BUILDING.equals(InvestmentAdAssetType.fromCode(investmentAdAsset.getAssetType()))) {
			Building building = communityProvider.findBuildingById(investmentAdAsset.getAssetId());
			if (building.getStatus() == BuildingAdminStatus.INACTIVE.getCode()) {
				return false;
			}
			//如果楼宇下的房源都不是待租状态，则返回false
			List<Address> apartments = addressProvider.findActiveApartmentsByBuildingId(building.getId());
			List<Long> aptIdList = apartments.stream().map(a->a.getId()).collect(Collectors.toList());
			Map<Long, CommunityAddressMapping> communityAddressMappingMap = propertyMgrProvider.mapAddressMappingByAddressIds(aptIdList);
			Set<Entry<Long, CommunityAddressMapping>> entrySet = communityAddressMappingMap.entrySet();
			for (Entry<Long, CommunityAddressMapping> entry : entrySet) {
				if (entry.getValue().getLivingStatus() == AddressMappingStatus.FREE.getCode()) {
					return true;
				}
			}
			return false;
		}else if (InvestmentAdAssetType.APARTMENT.equals(InvestmentAdAssetType.fromCode(investmentAdAsset.getAssetType()))) {
			Address address = addressProvider.findAddressById(investmentAdAsset.getAssetId());
			if (address.getStatus() == AddressAdminStatus.INACTIVE.getCode()) {
				return false;
			}
			CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMapping(organizationId, address.getCommunityId(), address.getId());
			if (communityAddressMapping.getLivingStatus() != AddressMappingStatus.FREE.getCode()) {
				return false;
			}
		}
		return true;
	}
	
	private RelatedAssetDTO convertToAssetDTO(InvestmentAdAsset investmentAdAsset){
		RelatedAssetDTO assetDTO = new RelatedAssetDTO();
		if (InvestmentAdAssetType.BUILDING.equals(InvestmentAdAssetType.fromCode(investmentAdAsset.getAssetType()))) {
			Building building = communityProvider.findBuildingById(investmentAdAsset.getAssetId());
			assetDTO.setBuildingId(building.getId());
			assetDTO.setBuildingName(building.getName());
		}else if (InvestmentAdAssetType.APARTMENT.equals(InvestmentAdAssetType.fromCode(investmentAdAsset.getAssetType()))) {
			Address address = addressProvider.findAddressById(investmentAdAsset.getAssetId());
			assetDTO.setBuildingId(address.getBuildingId());
			assetDTO.setBuildingName(address.getBuildingName());
			assetDTO.setApartmentId(address.getId());
			assetDTO.setApartmentName(address.getApartmentName());
		}
		return assetDTO;
	}
	
	private void addInvestmentAdBanners(List<InvestmentAdBannerDTO> banners,InvestmentAd investmentAd){
		if (banners != null && banners.size() > 0) {
			for (InvestmentAdBannerDTO bannerDTO : banners) {
				InvestmentAdBanner adBanner = new InvestmentAdBanner();
				adBanner.setNamespaceId(investmentAd.getNamespaceId());
				adBanner.setAdvertisementId(investmentAd.getId());
				adBanner.setContentUri(bannerDTO.getContentUri());
				adBanner.setStatus(InvestmentAdGeneralStatus.ACTIVE.getCode());
				investmentAdProvider.createInvestmentAdBanner(adBanner);
			}
		}
	}
	
	private void addInvestmentAdGeneralFormInfo(Long generalFormId, List<PostApprovalFormItem> formValues,Long sourceId) {
		addGeneralFormValuesCommand generalFormCommand = new addGeneralFormValuesCommand();
		generalFormCommand.setGeneralFormId(generalFormId);
		generalFormCommand.setValues(formValues);
		generalFormCommand.setSourceId(sourceId);
		generalFormCommand.setSourceType(EntityType.INVESTMENT_ADVERTISEMENT.getCode());
		generalFormService.addGeneralFormValues(generalFormCommand);
	}
	
	private List<PostApprovalFormItem> getInvestmentAdGeneralFormValues(Long sourceId){
		GetGeneralFormValuesCommand getGeneralFormValuesCommand = new GetGeneralFormValuesCommand();
		getGeneralFormValuesCommand.setSourceType(EntityType.INVESTMENT_ADVERTISEMENT.getCode());
		getGeneralFormValuesCommand.setSourceId(sourceId);
		getGeneralFormValuesCommand.setOriginFieldFlag(NormalFlag.NEED.getCode());
		List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(getGeneralFormValuesCommand);
		return formValues;
	}
	
	private void checkPrivilegeAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.INVESTMENT_AD, null, null, null, communityId);
	}


}
