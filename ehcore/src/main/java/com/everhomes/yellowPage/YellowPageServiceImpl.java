package com.everhomes.yellowPage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowEvaluateItem;
import com.everhomes.flow.FlowEvaluateItemProvider;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.mail.MailHandler;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.handler.Utils.DateStyle;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.reserver.ReserverEntity;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.comment.OwnerTokenDTO;
import com.everhomes.rest.comment.OwnerType;
import com.everhomes.rest.common.PrivilegeType;
import com.everhomes.rest.common.ServiceAllianceActionData;
import com.everhomes.rest.common.TagSearchItem;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterprise.GetAuthOrgByProjectIdAndAppIdCommand;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralApprovalSupportType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormRenderType;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormTemplateType;
import com.everhomes.rest.general_approval.GeneralFormValidatorType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.NewsInstanceConfig;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.FieldDTO;
import com.everhomes.rest.user.FieldTemplateDTO;
import com.everhomes.rest.yellowPage.AddNotifyTargetCommand;
import com.everhomes.rest.yellowPage.AddServiceAllianceProviderCommand;
import com.everhomes.rest.yellowPage.AddYellowPageCommand;
import com.everhomes.rest.yellowPage.AllianceTagDTO;
import com.everhomes.rest.yellowPage.AllianceTagGroupDTO;
import com.everhomes.rest.yellowPage.ApplyExtraAllianceEventCommand;
import com.everhomes.rest.yellowPage.AttachmentDTO;
import com.everhomes.rest.yellowPage.DeleteNotifyTargetCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceProviderCommand;
import com.everhomes.rest.yellowPage.DeleteYellowPageCommand;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.ExtraAllianceEventDTO;
import com.everhomes.rest.yellowPage.ExtraEventAttachmentDTO;
import com.everhomes.rest.yellowPage.GetAllianceTagCommand;
import com.everhomes.rest.yellowPage.GetAllianceTagResponse;
import com.everhomes.rest.yellowPage.GetCategoryIdByEntryIdCommand;
import com.everhomes.rest.yellowPage.GetCategoryIdByEntryIdResponse;
import com.everhomes.rest.yellowPage.GetExtraAllianceEventCommand;
import com.everhomes.rest.yellowPage.GetExtraAllianceEventResponse;
import com.everhomes.rest.yellowPage.GetServiceAllianceCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceDisplayModeCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseListCommand;
import com.everhomes.rest.yellowPage.ListServiceAllianceProvidersCommand;
import com.everhomes.rest.yellowPage.ListServiceAllianceProvidersResponse;
import com.everhomes.rest.yellowPage.ListServiceNamesCommand;
import com.everhomes.rest.yellowPage.GetYellowPageDetailCommand;
import com.everhomes.rest.yellowPage.GetYellowPageListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.JumpModuleDTO;
import com.everhomes.rest.yellowPage.JumpType;
import com.everhomes.rest.yellowPage.ListAttachmentsCommand;
import com.everhomes.rest.yellowPage.ListAttachmentsResponse;
import com.everhomes.rest.yellowPage.ListJumpModulesCommand;
import com.everhomes.rest.yellowPage.ListNotifyTargetsCommand;
import com.everhomes.rest.yellowPage.ListNotifyTargetsResponse;
import com.everhomes.rest.yellowPage.ListOnlineServicesCommand;
import com.everhomes.rest.yellowPage.ListOnlineServicesResponse;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesAdminResponse;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesCommand;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesAdminResponse;
import com.everhomes.rest.yellowPage.NotifyTargetDTO;
import com.everhomes.rest.yellowPage.RequestInfoDTO;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayDestination;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDisplayMode;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceDisplayModeDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceListResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceLocalStringCode;
import com.everhomes.rest.yellowPage.ServiceAllianceOwnerType;
import com.everhomes.rest.yellowPage.ServiceAllianceProviderDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceSourceRequestType;
import com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus;
import com.everhomes.rest.yellowPage.IdNameDTO;
import com.everhomes.rest.yellowPage.SetNotifyTargetStatusCommand;
import com.everhomes.rest.yellowPage.UpdateAllianceTagCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDefaultOrderCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseDisplayFlagCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceProviderCommand;
import com.everhomes.rest.yellowPage.UpdateServiceTypeOrdersCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.VerifyNotifyTargetCommand;
import com.everhomes.rest.yellowPage.YellowPageAattchmentDTO;
import com.everhomes.rest.yellowPage.YellowPageDTO;
import com.everhomes.rest.yellowPage.YellowPageListResponse;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.rest.yellowPage.YellowPageType;
import com.everhomes.rest.yellowPage.stat.ClickCountDTO;
import com.everhomes.rest.yellowPage.stat.ClickStatDTO;
import com.everhomes.rest.yellowPage.stat.ClickStatDetailDTO;
import com.everhomes.rest.yellowPage.stat.ClickTypeDTO;
import com.everhomes.rest.yellowPage.stat.InterestStatDTO;
import com.everhomes.rest.yellowPage.stat.ListClickStatCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailResponse;
import com.everhomes.rest.yellowPage.stat.ListClickStatResponse;
import com.everhomes.rest.yellowPage.stat.ListInterestStatResponse;
import com.everhomes.rest.yellowPage.stat.ListServiceTypeNamesCommand;
import com.everhomes.rest.yellowPage.stat.ListStatCommonCommand;
import com.everhomes.rest.yellowPage.stat.ServiceAndTypeNameDTO;
import com.everhomes.rest.yellowPage.stat.StatClickOrSortType;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAllianceAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceAttachments;
import com.everhomes.server.schema.tables.records.EhLaunchPadItemsRecord;
import com.everhomes.server.schema.tables.records.EhServiceAllianceAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhServiceAlliancesRecord;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.RequestTemplates;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.file.FileUtils;
import com.everhomes.yellowPage.stat.ClickStat;
import com.everhomes.yellowPage.stat.ClickStatDetail;
import com.everhomes.yellowPage.stat.ClickStatDetailProvider;
import com.everhomes.yellowPage.stat.ClickStatProvider;
import com.everhomes.yellowPage.stat.ClickStatTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import static com.everhomes.yellowPage.YellowPageUtils.throwError;

@Component
public class YellowPageServiceImpl implements YellowPageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageServiceImpl.class);

	DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Autowired
	private HotlineService hotlineService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private YellowPageProvider yellowPageProvider;
	
	@Autowired
	private BuildingProvider buildingProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private AuditLogProvider auditLogProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private UserActivityProvider userActivityProvider;

	@Autowired
	private GeneralFormProvider generalFormProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;

	@Autowired
	private ServiceAllianceCommentProvider commentProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ServiceAllianceApplicationRecordProvider saapplicationRecordProvider;

	@Autowired
	private FlowCaseProvider flowCaseProvider;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

	@Autowired
	private PortalVersionProvider portalVersionProvider;

	@Autowired
	private ServiceAllianceRequestInfoSearcher saRequestInfoSearcher;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private FlowEventLogProvider flowEventLogProvider;

	@Autowired
	private FlowService flowService;

	@Autowired
	private FlowEvaluateItemProvider flowEvaluateItemProvider;

	@Autowired
	private ServiceAllianceProviderProvider allianceProvidProvider;

	@Autowired
	private AllianceExtraEventProvider allianceExtraEventsProvider;

	@Autowired
	private AllianceExtraEventAttachProvider allianceEventAttachmentProvider;

	@Autowired
	private AllianceOnlineServiceI allianceOnlineService;
	
	@Autowired
	private AllianceTagProvider allianceTagProvider;
	
	@Autowired
	private AllianceTagValProvider allianceTagValProvider;
	
	@Autowired
	AllianceOnlineServiceI onlineService;
	
	@Autowired
	PortalService portalService;
	
	@Autowired
	LaunchPadProvider launchPadProvider;


	@Autowired
	OrganizationService organizationService;
	@Autowired
	AllianceStandardService allianceStandardService;
	private void populateYellowPage(YellowPage yellowPage) {
		this.yellowPageProvider.populateYellowPagesAttachment(yellowPage);

		populateYellowPageUrl(yellowPage);
		populateYellowPageAttachements(yellowPage, yellowPage.getAttachments());

	}

	private void populateYellowPageUrl(YellowPage yp) {

		String posterUri = yp.getPosterUri();
		if (posterUri != null && posterUri.length() > 0) {
			try {
				String posterUrl = contentServerService.parserUri(yp.getPosterUri(), EntityType.USER.getCode(),
						UserContext.current().getUser().getId());
				yp.setPosterUrl(posterUrl);
			} catch (Exception e) {
				LOGGER.error("Failed to parse poster uri of YellowPage, YellowPage =" + yp, e);
			}
		}
	}

	private void populateYellowPageAttachements(YellowPage yellowPage, List<YellowPageAttachment> attachmentList) {

		if (attachmentList == null || attachmentList.size() == 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The YellowPage attachment list is empty, YellowPageId=" + yellowPage.getId());
			}
		} else {
			for (YellowPageAttachment attachment : attachmentList) {
				populateYellowPageAttachement(yellowPage, attachment);
			}
		}
	}

	private void populateYellowPageAttachement(YellowPage yellowPage, YellowPageAttachment attachment) {

		if (attachment == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The YellowPage attachment is null, YellowPageId=" + yellowPage.getId());
			}
		} else {
			String contentUri = attachment.getContentUri();
			if (contentUri != null && contentUri.length() > 0) {
				try {
					String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(),
							UserContext.current().getUser().getId());
					attachment.setContentUrl(url);
				} catch (Exception e) {
					LOGGER.error("Failed to parse attachment uri, YellowPageId=" + yellowPage.getId()
							+ ", attachmentId=" + attachment.getId(), e);
				}
			} else {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				}
			}
		}
	}

	@Override
	public YellowPageDTO getYellowPageDetail(GetYellowPageDetailCommand cmd) {
		YellowPageDTO response = null;
		if (cmd.getType() != null && cmd.getType().byteValue() > 10) {
			GetServiceAllianceEnterpriseDetailCommand command = ConvertHelper.convert(cmd,
					GetServiceAllianceEnterpriseDetailCommand.class);
			ServiceAllianceDTO sa = getServiceAllianceEnterpriseDetail(command);

			response = ConvertHelper.convert(sa, YellowPageDTO.class);

		} else {

			YellowPage yellowPage = this.yellowPageProvider.getYellowPageById(cmd.getId());

			populateYellowPage(yellowPage);

			if (cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())) {
				ServiceAlliance serviceAlliance = ConvertHelper.convert(yellowPage, ServiceAlliance.class);
				response = ConvertHelper.convert(serviceAlliance, YellowPageDTO.class);
			} else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())) {
				MakerZone makerZone = ConvertHelper.convert(yellowPage, MakerZone.class);
				response = ConvertHelper.convert(makerZone, YellowPageDTO.class);
			} else {
				response = ConvertHelper.convert(yellowPage, YellowPageDTO.class);
			}
		}
		return response;
	}

	@Override
	public YellowPageListResponse getYellowPageList(GetYellowPageListCommand cmd) {
		YellowPageListResponse response = new YellowPageListResponse();
		response.setYellowPages(new ArrayList<YellowPageDTO>());
		if (cmd.getType() != null && cmd.getType().byteValue() > 10) {
			GetServiceAllianceEnterpriseListCommand command = ConvertHelper.convert(cmd,
					GetServiceAllianceEnterpriseListCommand.class);
			command.setPageSize(AppConstants.PAGINATION_MAX_SIZE);
			command.setNextPageAnchor(cmd.getPageAnchor());
			if (!StringUtils.isEmpty(cmd.getServiceType())) {
				ServiceAllianceCategories category = yellowPageProvider
						.findCategoryByName(UserContext.getCurrentNamespaceId(), cmd.getServiceType());
				command.setCategoryId(category.getId());
			}
			ServiceAllianceListResponse res = this.getServiceAllianceEnterpriseList(command);
			List<ServiceAllianceDTO> dtos = res.getDtos();
			if (dtos != null && dtos.size() > 0) {
				for (ServiceAllianceDTO dto : dtos) {
					YellowPageDTO ypDto = ConvertHelper.convert(dto, YellowPageDTO.class);
					if (null != ypDto)
						response.getYellowPages().add(ypDto);
				}
			}

		} else if (cmd.getType() != null && cmd.getType().equals(YellowPageType.PARKENTSERVICEHOTLINE.getCode())) {
			GetHotlineListCommand cmd2 = ConvertHelper.convert(cmd, GetHotlineListCommand.class);
			cmd2.setServiceType(ServiceType.SERVICE_HOTLINE.getCode());
			GetHotlineListResponse resp2 = this.hotlineService.getHotlineList(cmd2);
			if (resp2.getHotlines() != null)
				resp2.getHotlines().forEach(r -> {
					YellowPageDTO dto = ConvertHelper.convert(r, YellowPageDTO.class);
					response.getYellowPages().add(dto);
				});
		} else {
			// 做兼容
			if (null != cmd.getCommunityId()) {
				cmd.setOwnerId(cmd.getCommunityId());
			} else if (null != cmd.getOwnerId()) {
				List<Community> communities = communityProvider
						.listCommunitiesByNamespaceId(cmd.getOwnerId().intValue());
				if (null != communities && 0 != communities.size()) {
					cmd.setOwnerId(communities.get(0).getId());
					cmd.setOwnerType("community");
				}
			}
			response.setYellowPages(new ArrayList<YellowPageDTO>());
			int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
			CrossShardListingLocator locator = new CrossShardListingLocator();
			locator.setAnchor(cmd.getPageAnchor());
			List<YellowPage> yellowPages = this.yellowPageProvider.queryYellowPages(locator, pageSize + 1,
					cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParentId(), cmd.getType(), cmd.getServiceType(),
					cmd.getKeywords());
			if (null == yellowPages || yellowPages.size() == 0)
				return response;

			for (YellowPage yellowPage : yellowPages) {
				populateYellowPage(yellowPage);
				if (cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())) {
					ServiceAlliance serviceAlliance = ConvertHelper.convert(yellowPage, ServiceAlliance.class);
					response.getYellowPages().add(ConvertHelper.convert(serviceAlliance, YellowPageDTO.class));
				}

				else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())) {
					MakerZone makerZone = ConvertHelper.convert(yellowPage, MakerZone.class);
					response.getYellowPages().add(ConvertHelper.convert(makerZone, YellowPageDTO.class));
				}

				else {
					response.getYellowPages().add(ConvertHelper.convert(yellowPage, YellowPageDTO.class));
				}
			}
		}
		return response;
	}

	@Override
	public void addYellowPage(AddYellowPageCommand cmd) {
		YellowPage yp = null;
		if (cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())) {
			ServiceAlliance serviceAlliance = ConvertHelper.convert(cmd, ServiceAlliance.class);
			yp = ConvertHelper.convert(serviceAlliance, YellowPage.class);
		} else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())) {
			MakerZone makerZone = ConvertHelper.convert(cmd, MakerZone.class);
			yp = ConvertHelper.convert(makerZone, YellowPage.class);
		} else {
			yp = ConvertHelper.convert(cmd, YellowPage.class);
		}
		this.yellowPageProvider.createYellowPage(yp);
		createYellowPageAttachments(cmd.getAttachments(), yp.getId());

	}

	private void createYellowPageAttachments(List<YellowPageAattchmentDTO> attachments, Long ownerId) {
		if (null == attachments)
			return;
		for (YellowPageAattchmentDTO dto : attachments) {
			if (null == dto.getContentUri())
				continue;
			YellowPageAttachment attachment = ConvertHelper.convert(dto, YellowPageAttachment.class);
			attachment.setOwnerId(ownerId);
			attachment.setContentType(PostContentType.IMAGE.getCode());
			this.yellowPageProvider.createYellowPageAttachments(attachment);
		}
	}

	@Override
	public void deleteYellowPage(DeleteYellowPageCommand cmd) {

		YellowPage yellowPage = this.yellowPageProvider.getYellowPageById(cmd.getId());
		if (null == yellowPage) {
			LOGGER.error("YellowPage already deleted , YellowPage Id =" + cmd.getId());
		}
		yellowPage.setStatus(YellowPageStatus.INACTIVE.getCode());
		if (null != yellowPage.getLatitude())
			yellowPage.setGeohash(GeoHashUtils.encode(yellowPage.getLatitude(), yellowPage.getLongitude()));
		this.yellowPageProvider.updateYellowPage(yellowPage);
	}

	@Override
	public void updateYellowPage(UpdateYellowPageCommand cmd) {
		YellowPage yp = null;
		if (cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())) {
			ServiceAlliance serviceAlliance = ConvertHelper.convert(cmd, ServiceAlliance.class);
			yp = ConvertHelper.convert(serviceAlliance, YellowPage.class);
		} else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())) {
			MakerZone makerZone = ConvertHelper.convert(cmd, MakerZone.class);
			yp = ConvertHelper.convert(makerZone, YellowPage.class);
		} else {
			yp = ConvertHelper.convert(cmd, YellowPage.class);
		}
		this.yellowPageProvider.updateYellowPage(yp);
		this.yellowPageProvider.deleteYellowPageAttachmentsByOwnerId(yp.getId());
		createYellowPageAttachments(cmd.getAttachments(), yp.getId());
	}

	@Override
	public YellowPageDTO getYellowPageTopic(GetYellowPageTopicCommand cmd) {

		YellowPageDTO response = null;
		if (cmd.getType() != null && cmd.getType().byteValue() > 10) {
			GetServiceAllianceCommand command = ConvertHelper.convert(cmd, GetServiceAllianceCommand.class);
			command.setType(cmd.getType().longValue());
			ServiceAllianceDTO sa = getServiceAllianceByScene(command);

			response = ConvertHelper.convert(sa, YellowPageDTO.class);

		} else {

			YellowPage yellowPage = this.yellowPageProvider.queryYellowPageTopic(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getType());
			if (null == yellowPage) {
				LOGGER.error(
						"can not find the topic community ID = " + cmd.getOwnerId() + "; and type = " + cmd.getType());
				return null;
			}
			populateYellowPage(yellowPage);
			if (cmd.getType().equals(YellowPageType.SERVICEALLIANCE.getCode())) {
				ServiceAlliance serviceAlliance = ConvertHelper.convert(yellowPage, ServiceAlliance.class);
				response = ConvertHelper.convert(serviceAlliance, YellowPageDTO.class);
			} else if (cmd.getType().equals(YellowPageType.MAKERZONE.getCode())) {
				MakerZone makerZone = ConvertHelper.convert(yellowPage, MakerZone.class);
				response = ConvertHelper.convert(makerZone, YellowPageDTO.class);
			} else {
				response = ConvertHelper.convert(yellowPage, YellowPageDTO.class);
			}
		}

		// 增加building内容
		if (null != response.getBuildingId()) {
			Building building = buildingProvider.findBuildingById(response.getBuildingId());
			response.setBuildingName(building.getName());
		}
		return response;
	}

	@Override
	public void updateServiceAllianceCategory(UpdateServiceAllianceCategoryCommand cmd) {
		Long type = cmd.getParentId();
		ServiceAllianceCategories parent = allianceStandardService.queryHomePageCategoryByAdmin(cmd.getOwnerType(),
				cmd.getOwnerId(), type);
		if (null == parent) {
			parent = allianceStandardService.createHomePageCategory(cmd.getOwnerType(), cmd.getOwnerId(), type);
		}

		// 没传时 取logourl
		if (cmd.getSelectedLogoUrl() == null) {
			cmd.setSelectedLogoUrl(cmd.getLogoUrl());
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		if (cmd.getNamespaceId() != null) {
			namespaceId = cmd.getNamespaceId();
		}
		ServiceAllianceCategories category = new ServiceAllianceCategories();
		if (cmd.getCategoryId() == null) {
			category.setName(cmd.getName());
			category.setOwnerId(cmd.getOwnerId());
			category.setOwnerType(cmd.getOwnerType());
			category.setNamespaceId(namespaceId);
			category.setDisplayMode(cmd.getDisplayMode());
			category.setDisplayDestination(cmd.getDisplayDestination());
			category.setParentId(parent.getId());
			category.setPath(parent.getName() + "/" + cmd.getName());
			category.setLogoUrl(cmd.getLogoUrl());
			category.setSelectedLogoUrl(cmd.getSelectedLogoUrl());
			category.setSkipType(cmd.getSkipType() == null ? (byte) 0 : cmd.getSkipType());
			category.setType(cmd.getParentId());
			yellowPageProvider.createCategory(category);
			return;
		}

		category = yellowPageProvider.findCategoryById(cmd.getCategoryId());
		if (null == category) {
			throwError(YellowPageServiceErrorCode.ERROR_CATEGORY_NOT_EXIST, "category not exist");
		}
		category.setName(cmd.getName());
		category.setPath(parent.getName() + "/" + cmd.getName());
		category.setLogoUrl(cmd.getLogoUrl());
		category.setDisplayMode(cmd.getDisplayMode());
		category.setDisplayDestination(cmd.getDisplayDestination());
		category.setSelectedLogoUrl(cmd.getSelectedLogoUrl());
		category.setSkipType(cmd.getSkipType() == null ? (byte) 0 : cmd.getSkipType());
		yellowPageProvider.updateCategory(category);
	}

	@Override
	public void deleteServiceAllianceCategory(DeleteServiceAllianceCategoryCommand cmd) {
		User user = UserContext.current().getUser();

		List<YellowPage> yps = yellowPageProvider.getYellowPagesByCategoryId(cmd.getCategoryId());

		if (yps != null && yps.size() > 0) {
			LOGGER.error("the category which id = " + cmd.getCategoryId() + " is in use!");
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_CATEGORY_IN_USE,
					localeStringService.getLocalizedString(String.valueOf(YellowPageServiceErrorCode.SCOPE),
							String.valueOf(YellowPageServiceErrorCode.ERROR_CATEGORY_IN_USE),
							UserContext.current().getUser().getLocale(), "category is in use!"));
		}

		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(cmd.getCategoryId());
		if (null == category || category.getStatus() == 0) {
			LOGGER.error("the category which id = " + cmd.getCategoryId() + " is already deleted!");
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_CATEGORY_ALREADY_DELETED,
					localeStringService.getLocalizedString(String.valueOf(YellowPageServiceErrorCode.SCOPE),
							String.valueOf(YellowPageServiceErrorCode.ERROR_CATEGORY_ALREADY_DELETED),
							UserContext.current().getUser().getLocale(), "category already deleted!"));
		}

		if (category != null && category.getParentId() == 0) {
			LOGGER.error("the category which parent id is 0!");
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_DELETE_ROOT_CATEGORY,
					localeStringService.getLocalizedString(String.valueOf(YellowPageServiceErrorCode.SCOPE),
							String.valueOf(YellowPageServiceErrorCode.ERROR_DELETE_ROOT_CATEGORY),
							UserContext.current().getUser().getLocale(), "cannot delete root category!"));
		}

		// set status inactive and add log
		category.setStatus((byte) 0);
		yellowPageProvider.updateCategory(category);

		AuditLog log = new AuditLog();
		log.setOperatorUid(user.getId());
		log.setOperationType("DELETE");
		log.setResourceType(EntityType.SACATEGORY.getCode());
		log.setResourceId(category.getId());
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		auditLogProvider.createAuditLog(log);
	}

	@Override
	public ServiceAllianceDTO getServiceAllianceEnterpriseDetail(GetServiceAllianceEnterpriseDetailCommand cmd) {
		// YellowPage yellowPage = verifyYellowPage(cmd.getId(),
		// cmd.getOwnerType(), cmd.getOwnerId());
		//
		// populateYellowPage(yellowPage);

		ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
		populateServiceAlliance(sa);

		ServiceAllianceDTO dto = null;

		// ServiceAlliance serviceAlliance = ConvertHelper.convert(yellowPage
		// ,ServiceAlliance.class);
		dto = ConvertHelper.convert(sa, ServiceAllianceDTO.class);
		if (dto.getJumpType() != null) {

			if (JumpType.TEMPLATE.equals(JumpType.fromCode(dto.getJumpType()))) {
				RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
				if (template != null) {
					dto.setTemplateName(template.getName());
					dto.setButtonTitle(template.getButtonTitle());
				}
			} else if (JumpType.FORM.equals(JumpType.fromCode(dto.getJumpType()))) {
				dto.setTemplateName(dto.getTemplateType());
				dto.setButtonTitle("我要申请");
				dto.setModuleUrl(buildFormModuleUrl(dto.getId()));
			}
		} else {
			// 兼容以前只有模板跳转时jumptype字段为null的情况
			if (dto.getTemplateType() != null) {
				RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
				if (template != null) {
					dto.setTemplateName(template.getName());
					dto.setButtonTitle(template.getButtonTitle());
				}
			}

		}

		if (!StringUtils.isEmpty(sa.getButtonTitle())) {
			dto.setButtonTitle(sa.getButtonTitle());
		}

		// 客户端/前端显示前需要检查表单，工作流状态是否有效
		if (JumpType.FORM.getCode().equals(dto.getJumpType())) {
			if (!checkFormModuleUrlValid(dto)) {
//				dto.setButtonTitle(null);
//				dto.setJumpType(JumpType.NONE.getCode());
//				dto.setModuleUrl(null);
                dto.setIsApprovalActive(TrueOrFalseFlag.FALSE.getCode());
            }
		}

		this.processDetailUrl(dto);
		// response.setDisplayName(serviceAlliance.getNickName());
		ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(dto.getOwnerType());
		if (belongType == ServiceAllianceBelongType.COMMUNITY) {
			Community community = communityProvider.findCommunityById(dto.getOwnerId());
			if (community != null) {
				dto.setNamespaceId(community.getNamespaceId());
			}
		} else {
			Organization organization = organizationProvider.findOrganizationById(dto.getOwnerId());
			if (organization != null) {
				dto.setNamespaceId(organization.getNamespaceId());
			}
		}
		// dto.setNamespaceId(UserContext.getCurrentNamespaceId());

		processServiceUrl(dto);
		processCommentCount(dto);
		processCommentToken(dto);
		return dto;
	}

	@Override
	public ServiceAllianceDTO getServiceAllianceByScene(GetServiceAllianceCommand cmd) {
		
		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(cmd.getOwnerType())) {
			throwError(YellowPageServiceErrorCode.ERROR_OWNER_TYPE_NOT_COMMUNITY, "ownerType must be community");
		}
		
		ServiceAllianceCategories homePageCa = allianceStandardService.queryHomePageCategoryByScene(cmd.getType(),
				cmd.getOwnerId());
		if (null == homePageCa) {
			LOGGER.error("can not find the homePage cmd = " + cmd.toString());
			return null;
		}
		
		//获取首页封面图
		ServiceAlliances sa = new ServiceAlliances();
		sa.setId(homePageCa.getId());
		this.yellowPageProvider.populateServiceAlliancesAttachment(sa);
		populateServiceAllianceAttachements(sa, sa.getCoverAttachments());
		ServiceAllianceDTO dto = ConvertHelper.convert(sa, ServiceAllianceDTO.class);
		dto.setDisplayMode(homePageCa.getDisplayMode());
		dto.setSkipType(homePageCa.getSkipType());
		return dto;
	}
	
	@Override
	public ServiceAllianceDTO getServiceAllianceByAdmin(GetServiceAllianceCommand cmd) {
		ServiceAllianceCategories homePageCa = allianceStandardService.queryHomePageCategoryByAdmin(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getType());
		if (null == homePageCa) {
			LOGGER.error("getServiceAllianceByAdmin can not find the homePage cmd = " + cmd.toString());
			return null;
		}

		// 获取首页封面图
		ServiceAlliances sa = new ServiceAlliances();
		sa.setId(homePageCa.getId());
		this.yellowPageProvider.populateServiceAlliancesAttachment(sa);
		populateServiceAllianceAttachements(sa, sa.getCoverAttachments());
		ServiceAllianceDTO dto = ConvertHelper.convert(sa, ServiceAllianceDTO.class);
		dto.setDisplayMode(homePageCa.getDisplayMode());
		dto.setSkipType(homePageCa.getSkipType());
		return dto;
	}

	@Override
	public ServiceAllianceListResponse getServiceAllianceEnterpriseList(GetServiceAllianceEnterpriseListCommand cmd) {
		if (cmd.getAppId() != null) {
			checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());
		}
		
		//检验项目id的正确性。
		if (!"community".equals(cmd.getOwnerType()) ) {
			throwError(YellowPageServiceErrorCode.ERROR_COMMUNITY_NOT_CHOSEN, "community not chosen");
		}

		cmd.setOwnerId(cmd.getOwnerId());
		cmd.setOwnerType(cmd.getOwnerType());

		ServiceAllianceListResponse response = new ServiceAllianceListResponse();
		response.setSkipType((byte) 0);

		ServiceAllianceCategories mainCag = allianceStandardService.queryHomePageCategoryByScene(cmd.getParentId(), cmd.getOwnerId());
		if (null != mainCag) {
			response.setSkipType(mainCag.getSkipType());
		}

		if (cmd.getCategoryId() != null) {
			ServiceAllianceCategories cag = yellowPageProvider.findCategoryById(cmd.getCategoryId());
			if (null != cag) {
				response.setSkipType(cag.getSkipType());
			}
		}
		response.setDtos(new ArrayList<ServiceAllianceDTO>());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getNextPageAnchor());

		List<ServiceAlliances> sas = null;
//		List<Long> childTagIds = buildSearchChildTagIds(cmd.getParentId(), cmd.getTagItems()); //根据输入的筛选item，转成实际的tagId
		List<Long> childTagIds = Arrays.asList(1L);
		final ServiceAllianceSourceRequestType sourceRequestType = ServiceAllianceSourceRequestType
				.fromCode(cmd.getSourceRequestType());
		// 如果为CLIENT，或者空值，认为是客户端
		if (ServiceAllianceSourceRequestType.CLIENT == sourceRequestType || sourceRequestType == null) {

			// 根据community和type获取所有项目id
			List<Long> authProjectIds = getProjectIdsByScene(cmd.getOwnerId(), cmd.getParentId());
			if (CollectionUtils.isEmpty(authProjectIds)) {
				return response;
			}

			sas = this.yellowPageProvider.queryServiceAllianceByScene(locator, pageSize + 1, cmd.getOwnerType(),
					cmd.getOwnerId(), authProjectIds, cmd.getParentId(), cmd.getCategoryId(),  childTagIds, cmd.getKeywords());
		} else {
			sas = this.yellowPageProvider.queryServiceAllianceAdmin(locator, pageSize + 1, cmd.getOwnerType(),
					cmd.getOwnerId(), cmd.getParentId(), cmd.getCategoryId(), childTagIds, cmd.getKeywords(), cmd.getDisplayFlag());
		}

		if (null == sas || sas.size() == 0)
			return response;

		if (sas.size() > pageSize) {
			sas.remove(sas.size() - 1);
			// modfiy by dengs,通过DEFAULT_ORDER排序了，锚点依据也变成DEFAULT_ORDER
			response.setNextPageAnchor(sas.get(sas.size() - 1).getDefaultOrder());
		}
		
		//对policydeclare做新排序
		sas = reSortServiceAllianceList(sas);

		for (ServiceAlliances sa : sas) {
			populateServiceAlliance(sa);
			if (null == sa.getServiceType() && null != sa.getCategoryId()) {
				ServiceAllianceCategories category = yellowPageProvider.findCategoryById(sa.getCategoryId());
				sa.setServiceType(category.getName());
			}
			ServiceAllianceDTO dto = ConvertHelper.convert(sa, ServiceAllianceDTO.class);
			if (dto.getJumpType() != null) {

				if (JumpType.TEMPLATE.equals(JumpType.fromCode(dto.getJumpType()))) {
					RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
					if (template != null) {
						dto.setTemplateName(template.getName());
						dto.setButtonTitle(template.getButtonTitle());
					}
				} else if (JumpType.FORM.equals(JumpType.fromCode(dto.getJumpType()))) {
					dto.setTemplateName(dto.getTemplateType());
					dto.setButtonTitle("我要申请");
					dto.setModuleUrl(buildFormModuleUrl(dto.getId()));
				}

			} else {
				// 兼容以前只有模板跳转时jumptype字段为null的情况
				if (dto.getTemplateType() != null) {
					RequestTemplates template = userActivityProvider.getCustomRequestTemplate(dto.getTemplateType());
					if (template != null) {
						dto.setTemplateName(template.getName());
						dto.setButtonTitle(template.getButtonTitle());
					}
				}

			}

			if (!StringUtils.isEmpty(sa.getButtonTitle())) {
				dto.setButtonTitle(sa.getButtonTitle());
			}

			// 客户端/前端显示前需要检查表单，工作流状态是否有效
			if ((ServiceAllianceSourceRequestType.CLIENT == sourceRequestType || sourceRequestType == null)
					&& JumpType.FORM.getCode().equals(dto.getJumpType())) {
				if (!checkFormModuleUrlValid(dto)) {
//					dto.setButtonTitle(null);
//					dto.setJumpType(JumpType.NONE.getCode());
//					dto.setModuleUrl(null);
                    dto.setIsApprovalActive(TrueOrFalseFlag.FALSE.getCode());
                }
			}

			processServiceUrl(dto);
			this.processDetailUrl(dto);
			this.processCommentToken(dto);
			response.getDtos().add(dto);

		}
		this.processCommentCount(sourceRequestType, cmd.getParentId(), cmd.getOwnerType(), cmd.getOwnerId(),
				response.getDtos());
		this.processRange(response.getDtos());
		
		return response;
	}

	private String buildFormModuleUrl(Long serviceId) {
		return "zl://form/create?sourceType=service_alliance&sourceId="+serviceId;
	}

	private boolean checkFormModuleUrlValid(ServiceAllianceDTO dto) {

		if (null == dto.getModuleUrl() || null == dto.getFormId()) {
			return false;
		}

		if (null != dto.getFlowId()) {
			FlowDTO flow = flowService.getFlowById(dto.getFlowId());
			if (null == flow || FlowStatusType.RUNNING.getCode() != flow.getStatus()) {
				return false;
			}
		}

		GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(dto.getFormId());
		if (null == form) {
			return false;
		}

		return true;
	}

	private List<Long> getProjectIdsByScene(Long ownerId, Long type) {
		if (null == type || type < 1) {
			return null;
		}

		//根据ownerId获取namespaceId
		Community community = communityProvider.findCommunityById(ownerId);
		if (null == community) {
			return null;
		}

		return organizationService.getProjectIdsByCommunityAndModuleApps(community.getNamespaceId(),
				ownerId, SERVICE_ALLIANCE_MODULE_ID, r -> {
					ServiceAllianceInstanceConfig config = (ServiceAllianceInstanceConfig) StringHelper
							.fromJsonString(r, ServiceAllianceInstanceConfig.class);
					return null == config ? false : type.equals(config.getType());
				});
	}

	/**
	* @Function: YellowPageServiceImpl.java
	* @Description: 对policydeclare做重新排序
	* 截止日期越靠近当天的越前面。
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月20日 下午4:53:28 
	*
	*/
	private List<ServiceAlliances> reSortServiceAllianceList(List<ServiceAlliances> sas) {

		// 1.没有截止时间的不处理
		if (null == sas.get(0).getEndTime()) {
			return sas;
		}

		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		List<ServiceAlliances> needToSort = new ArrayList<>(50);
		List<ServiceAlliances> noNeedSort = new ArrayList<>(50);
		for (ServiceAlliances sa : sas) {
			String nowDateStr = DateUtil.dateToStr(nowTime, "yyyy-MM-dd");
			String sADateStr = DateUtil.dateToStr(sa.getEndTime(), "yyyy-MM-dd");
			if (sADateStr.compareTo(nowDateStr) >= 0) {
				// 筛选出日期>=今天的数据
				needToSort.add(sa);
				continue;
			}
			noNeedSort.add(sa);
		}

		// 对需要排序的进行排序
		needToSort = needToSort.stream().sorted((x, y) -> {
			return x.getEndTime().compareTo(y.getEndTime());
		}).collect(Collectors.toList());

		List<ServiceAlliances> total = new ArrayList<>(100);
		if (!CollectionUtils.isEmpty(needToSort)) {
			total.addAll(needToSort);
		}
		if (!CollectionUtils.isEmpty(noNeedSort)) {
			total.addAll(noNeedSort);
		}
		return total;
	}
	

	/**
	 * 将前台传的筛选项转成tagId
	 * @param namespaceId
	 * @param type
	 * @param tagItems
	 * @return
	 * @author hmb
	 */
	private List<Long> buildSearchChildTagIds(Long type, List<TagSearchItem> tagItems) {
		if (CollectionUtils.isEmpty(tagItems)) {
			return null;
		}

		List<Long> childTagIds = new ArrayList<>(10);
		for (TagSearchItem item : tagItems) {
			if (!CollectionUtils.isEmpty(item.getChildTagIds())) {
				childTagIds.addAll(item.getChildTagIds().stream().filter(r->r != null).collect(Collectors.toList()));
				continue;
			}

			// 如果只传了parentId则找出所有的子筛选
			List<AllianceTag> childTags = allianceTagProvider
					.getAllianceChildTagList(UserContext.getCurrentNamespaceId(), type, item.getParentTagId());
			if (CollectionUtils.isEmpty(childTags)) {
				continue;
			}

			// 转成tagId
			childTagIds.addAll(childTags.stream().map(r->r.getId()).collect(Collectors.toList()));
		}

		return childTagIds;
	}

	// 根据服务联盟机构id，产生评论使用的token
	private void processCommentToken(ServiceAllianceDTO dto) {
		OwnerTokenDTO ownerTokenDto = new OwnerTokenDTO();
		ownerTokenDto.setId(dto.getId());
		ownerTokenDto.setType(OwnerType.SERVICEALLIANCE.getCode());
		String ownerTokenStr = WebTokenGenerator.getInstance().toWebToken(ownerTokenDto);
		dto.setCommentToken(ownerTokenStr);
	}

	// 查询服务联盟的机构评论的数量。
	private void processCommentCount(ServiceAllianceSourceRequestType sourceRequestType, Long type, String ownerType,
			Long ownerId, List<ServiceAllianceDTO> dtos) {
		boolean enableComment = true;
		ServiceAllianceCategories homePageCa = allianceStandardService.queryHomePageCategoryByScene(type, ownerId);
		if (homePageCa == null || null == homePageCa.getEnableComment()
				|| CommonStatus.INACTIVE == CommonStatus.fromCode(homePageCa.getEnableComment())) { // 当enableComment>0时，都算作开启
			enableComment = false;
		}
		
		List<Long> ownerIds = dtos.stream().map(r -> r.getId()).collect(Collectors.toList());
		Map<String, Integer> mapcounts = commentProvider.listServiceAllianceCommentCountByOwner(
				UserContext.getCurrentNamespaceId(), ServiceAllianceOwnerType.SERVICE_ALLIANCE.getCode(), ownerIds);
		for (ServiceAllianceDTO dto : dtos) {
			if (!enableComment) {
				dto.setCommentCount(null);
				continue;
			}
			String key = String.valueOf(dto.getId());
			if (mapcounts.get(key) != null) {
				dto.setCommentCount(mapcounts.get(key));
			} else {
				dto.setCommentCount(0);
			}
		}
	}
	
	private void processCommentCount(ServiceAllianceDTO dto) {
		ServiceAllianceCategories homePageCa = allianceStandardService.queryHomePageCategoryByScene(dto.getParentId(), dto.getOwnerId());
		if (homePageCa == null || null == homePageCa.getEnableComment()
				|| CommonStatus.INACTIVE == CommonStatus.fromCode(homePageCa.getEnableComment())) { // 当enableComment>0时，都算作开启
			dto.setCommentCount(null);
			return;
		}

		List<Long> ownerIds = Arrays.asList(dto.getId());
		Map<String, Integer> mapcounts = commentProvider.listServiceAllianceCommentCountByOwner(
				UserContext.getCurrentNamespaceId(), ServiceAllianceOwnerType.SERVICE_ALLIANCE.getCode(), ownerIds);
		String key = String.valueOf(dto.getId());
		if (mapcounts.get(key) != null) {
			dto.setCommentCount(mapcounts.get(key));
		} else {
			dto.setCommentCount(0);
		}
	}

	private void processRange(List<ServiceAllianceDTO> dtos) {
		for (ServiceAllianceDTO dto : dtos) {
			String range = dto.getRange();
			if (range != null && !range.equals("all")) {
				String[] communities = range.split(",");
				List<Long> communityIds = new ArrayList<>();
				for (int i = 0; i < communities.length; i++)
					communityIds.add(Long.valueOf(communities[i]));
				List<Community> communities2 = communityProvider.findCommunitiesByIds(communityIds);
				String rangeDisplay = "";
				for (Community co : communities2)
					rangeDisplay += co.getName() + ",";
				if (rangeDisplay.length() > 0)
					rangeDisplay = rangeDisplay.substring(0, rangeDisplay.length() - 1);
				dto.setRangeDisplay(rangeDisplay);
			}
			if (range != null && range.equals("all"))
				dto.setRangeDisplay("全部");
		}
	}

	private void processServiceUrl(ServiceAllianceDTO dto) {
		if (null != dto.getServiceUrl()) {
			try {
				String serviceUrl = dto.getServiceUrl();
				dto.setDisplayServiceUrl(dto.getServiceUrl());
				String routeUri = configurationProvider.getValue(ConfigConstants.APP_ROUTE_BROWSER_OUTER_URI, "");

				serviceUrl = String.format(routeUri, serviceUrl);
				int index = serviceUrl.indexOf("?");
				if (index != -1) {
					String prefix = serviceUrl.substring(0, index + 1);
					serviceUrl = serviceUrl.substring(index + 1, serviceUrl.length());
					serviceUrl = URLEncoder.encode(serviceUrl, "utf8");
					serviceUrl = prefix + serviceUrl;
				}

				dto.setServiceUrl(serviceUrl);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	private void processDetailUrl(ServiceAllianceDTO dto) {
		try {
			String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
			String detailUrl = configurationProvider.getValue(ConfigConstants.SERVICE_ALLIANCE_DETAIL_URL, "");
			String name = org.apache.commons.lang.StringUtils.trimToEmpty(dto.getName());

			String ownerType = dto.getOwnerType();
			ownerType = (ownerType == null) ? "" : ownerType;
			Long ownerId = dto.getOwnerId();
			ownerId = (ownerId == null) ? 0 : ownerId;
			detailUrl = String.format(detailUrl, dto.getId(), URLEncoder.encode(name, "UTF-8"), RandomUtils.nextInt(2),
					ownerType, ownerId);

			dto.setDetailUrl(homeUrl + detailUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateServiceAlliance(UpdateServiceAllianceCommand cmd) {

		dbProvider.execute(status -> {
			
			boolean needDeleteOldPic = true;
			ServiceAllianceCategories homePageCa = allianceStandardService.queryHomePageCategoryByAdmin(cmd.getOwnerType(),
					cmd.getOwnerId(), cmd.getType());
			if (null == homePageCa) {
				homePageCa = allianceStandardService.createHomePageCategory(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getType());
				needDeleteOldPic = false;
			}
			
			homePageCa.setDescription(cmd.getDescription());
			homePageCa.setSkipType(cmd.getSkipType() == null ? (byte)0 : cmd.getSkipType());
			homePageCa.setDisplayMode(cmd.getDisplayMode());
			yellowPageProvider.updateCategory(homePageCa);
			
			//添加首页样式的图片
			if (needDeleteOldPic) {
				this.yellowPageProvider.deleteServiceAllianceAttachmentsByOwnerId(homePageCa.getId()); // 删除旧的图片
			}

			// 创建多张封面图片 v3.4需求
			createServiceAllianceAttachments(cmd.getCoverAttachments(), homePageCa.getId(),
					ServiceAllianceAttachmentType.COVER_ATTACHMENT.getCode());
			
			return null;
		});
	}

	private ServiceAlliances verifyServiceAlliance(Long id, String ownerType, Long ownerId) {

		ServiceAlliances sa = this.yellowPageProvider.findServiceAllianceById(id, ownerType, ownerId);
		if (null == sa || YellowPageStatus.INACTIVE.equals(YellowPageStatus.fromCode(sa.getStatus()))) {
			LOGGER.error("ServiceAlliance already deleted , ServiceAlliance Id =" + id);
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_SERVICEALLIANCE_ALREADY_DELETED,
					localeStringService.getLocalizedString(String.valueOf(YellowPageServiceErrorCode.SCOPE),
							String.valueOf(YellowPageServiceErrorCode.ERROR_SERVICEALLIANCE_ALREADY_DELETED),
							UserContext.current().getUser().getLocale(), "ServiceAlliance already deleted!"));
		}

		return sa;
	}

	@Override
	public void deleteServiceAllianceEnterprise(DeleteServiceAllianceEnterpriseCommand cmd) {

		ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());

		sa.setStatus(YellowPageStatus.INACTIVE.getCode());
		this.yellowPageProvider.updateServiceAlliances(sa);
	}

	@Override
	public void updateServiceAllianceEnterprise(UpdateServiceAllianceEnterpriseCommand cmd) {

		// 校验权限
		checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());
		
		//检验项目id的正确性。
		if (null == cmd.getOwnerId() || cmd.getOwnerId() < 1) {
			throwError(YellowPageServiceErrorCode.ERROR_COMMUNITY_NOT_CHOSEN, "community not chosen");
		}

		ServiceAlliances serviceAlliance = ConvertHelper.convert(cmd, ServiceAlliances.class);

		if (null != serviceAlliance.getCategoryId()) {
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(serviceAlliance.getCategoryId());
			serviceAlliance.setServiceType(category.getName());
		}

		//设置属性为当前项目可见时，range参数有可能为空，这里设置成当前项目id
		if (StringUtils.isEmpty(cmd.getRange())) {
			serviceAlliance.setRange(cmd.getOwnerId() + "");
		}

		dbProvider.execute(r -> {

			if (cmd.getId() == null) {
				serviceAlliance.setCreatorUid(UserContext.current().getUser().getId());
				if (serviceAlliance.getLatitude() != null && serviceAlliance.getLongitude() != null) {
					serviceAlliance.setGeohash(
							GeoHashUtils.encode(serviceAlliance.getLatitude(), serviceAlliance.getLongitude()));
				}
				// 设置服务联盟显示在app端，by dengs,20170524.
				serviceAlliance.setDisplayFlag(DisplayFlagType.SHOW.getCode());

				 // 更新表单url
				updateServiceAllianceFormModuleUrl(serviceAlliance);

				// 更新时间
				updateServiceAllianceDate(serviceAlliance);
				this.yellowPageProvider.createServiceAlliances(serviceAlliance);
			} else {
				ServiceAlliances sa = verifyServiceAlliance(cmd.getId(), cmd.getOwnerType(), cmd.getOwnerId());
				if (serviceAlliance.getLatitude() != null && serviceAlliance.getLongitude() != null) {
					serviceAlliance.setGeohash(GeoHashUtils.encode(sa.getLatitude(), sa.getLongitude()));
				}

				serviceAlliance.setOwnerType(sa.getOwnerType());
				serviceAlliance.setOwnerId(sa.getOwnerId());
				serviceAlliance.setType(sa.getType());
				serviceAlliance.setCreateTime(sa.getCreateTime());
				serviceAlliance.setCreatorUid(sa.getCreatorUid());
				// by dengs,20170524 序号和是否在app端显示不能更新掉了。
				serviceAlliance.setDefaultOrder(sa.getDefaultOrder());
				serviceAlliance.setDisplayFlag(sa.getDisplayFlag());

				 // 更新表单url
				updateServiceAllianceFormModuleUrl(serviceAlliance);

				// 更新时间
				updateServiceAllianceDate(serviceAlliance);
				this.yellowPageProvider.updateServiceAlliances(serviceAlliance);
				this.yellowPageProvider.deleteServiceAllianceAttachmentsByOwnerId(serviceAlliance.getId());
			}

			// 创建封面图和文件
			createServiceAllianceAttachments(cmd.getAttachments(), serviceAlliance.getId(),
					ServiceAllianceAttachmentType.BANNER.getCode());
			createServiceAllianceAttachments(cmd.getFileAttachments(), serviceAlliance.getId(),
					ServiceAllianceAttachmentType.FILE_ATTACHMENT.getCode());

			// 更新筛选项
			updateServiceAllianceTagVals(cmd.getTagGroups(), serviceAlliance);

			// 更新客服记录
			updateServiceAllianceOnlineService(serviceAlliance);
			return null;
		});

	}

	/**
	 * 将客服存到关联表中
	 * @param serviceAlliance
	 */
	private void updateServiceAllianceOnlineService(ServiceAlliances serviceAlliance) {
		if (null == serviceAlliance.getOnlineServiceUid()) {
			return;
		}

		onlineService.updateAllianceOnlineService(serviceAlliance.getId(), serviceAlliance.getOnlineServiceUid(),
				serviceAlliance.getOnlineServiceUname());
	}

	/**
	 * 更新时间
	 * @param sa
	 */
	private void updateServiceAllianceDate(ServiceAlliances sa) {
		if (StringUtils.isEmpty(sa.getStartDate())) {
			return;
		}

		Date startDate = DateUtil.strToDate(sa.getStartDate(), "yyyy-MM-dd");
		Date endDate = DateUtil.strToDate(sa.getEndDate(), "yyyy-MM-dd");

		sa.setStartTime(new Timestamp(startDate.getTime()));
		sa.setEndTime(new Timestamp(endDate.getTime()));
	}

	/**
	 * 根据前端传来的筛选项进行添加
	 * @param tagGroups
	 * @param serviceAlliance
	 */

	private void updateServiceAllianceTagVals(List<AllianceTagGroupDTO> tagGroups, ServiceAlliances serviceAlliance) {
		// 首先删除该服务下的所有标签。
		allianceTagValProvider.deleteTagValByOwnerId(serviceAlliance.getId());

		// 如果group为空，直接返回。
		if (CollectionUtils.isEmpty(tagGroups)) {
			return;
		}

		// 添加所有的标签
		for (AllianceTagGroupDTO group : tagGroups) {
			if (CollectionUtils.isEmpty(group.getChildTags())) {
				continue;
			}

			for (AllianceTagDTO childDto : group.getChildTags()) {
				AllianceTagVal tagVal = new AllianceTagVal();
				tagVal.setOwnerId(serviceAlliance.getId());
				tagVal.setTagId(childDto.getId());
				tagVal.setTagParentId(childDto.getParentId());
				allianceTagValProvider.createTagVal(tagVal);
			}
		}
	}

	/**
	 * 用上表单时，需要修改
	 */
	private void updateServiceAllianceFormModuleUrl(ServiceAlliances sa) {

		if (!JumpType.FORM.getCode().equals(sa.getJumpType())) {
			return;
		}

		// 替换成 zl://form/create?sourceType=service_alliance&sourceId=6
		StringBuilder url = new StringBuilder();
		url.append("zl://form/create?")
		.append("sourceType=").append(YellowPageService.SERVICE_ALLIANCE_HANDLER_NAME)
		.append("&sourceId=").append(""+sa.getId());

		sa.setModuleUrl(url.toString());
	}

	final static Pattern pFindId = Pattern
			.compile("^zl://approval/create\\?approvalId=(-{0,1}[\\d]*).*&formId=([\\d]*).*$");

	private boolean replaceModuleUrl(ServiceAlliances serviceAlliance) {
		if (isSelfDefineForm(serviceAlliance.getModuleUrl())) {
			GeneralApproval approval = new GeneralApproval();
			Matcher matcher = pFindId.matcher(serviceAlliance.getModuleUrl());
			if (matcher.find()) {
				long approvalId = Long.valueOf(matcher.group(1));
				if (approvalId == -1L) {
					approval.setNamespaceId(UserContext.getCurrentNamespaceId());
					approval.setOwnerId(serviceAlliance.getOwnerId() == null ? -1 : serviceAlliance.getOwnerId());
					approval.setOwnerType(serviceAlliance.getOwnerType());
					approval.setModuleId(40500L);
					approval.setModuleType(FlowModuleType.SERVICE_ALLIANCE.getCode());
					approval.setFormOriginId(Long.valueOf(matcher.group(2)));
					approval.setFormVersion(0L);
					approval.setSupportType(GeneralApprovalSupportType.APP_AND_WEB.getCode());
					approval.setApprovalName("自定义表单_" + serviceAlliance.getId());
					approval.setProjectType(serviceAlliance.getOwnerType());
					approval.setProjectId(serviceAlliance.getOwnerId());
					approval.setStatus(GeneralApprovalStatus.DELETED.getCode());
					generalApprovalProvider.createGeneralApproval(approval);
					serviceAlliance.setModuleUrl(serviceAlliance.getModuleUrl().replaceFirst("approvalId=-1",
							"approvalId=" + approval.getId()));
				} else {
					approval = generalApprovalProvider.getGeneralApprovalById(approvalId);
					approval.setFormOriginId(Long.valueOf(matcher.group(2)));
					generalApprovalProvider.updateGeneralApproval(approval);
				}
				return true;
			} else {
				YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_SKIP_URL_FORMAT_ERROR, "skip url format error");
			}
		}
		return false;
	}

	private boolean isSelfDefineForm(String moduleUrl) {
		return moduleUrl != null && pFindId.matcher(moduleUrl).find();
	}


	private void populateServiceAlliance(ServiceAlliances sa) {
		this.yellowPageProvider.populateServiceAlliancesAttachment(sa);

		populateServiceAllianceUrl(sa);
		populateServiceAllianceDate(sa);
		populateServiceAllianceTagVals(sa);
		populateServiceAllianceAttachements(sa, sa.getCoverAttachments());
		populateServiceAllianceAttachements(sa, sa.getAttachments());
		populateServiceAllianceAttachements(sa, sa.getFileAttachments());

	}

	/**
	 * 获取当前服务的标签信息
	 * @param sa
	 */
	private void populateServiceAllianceTagVals(ServiceAlliances sa) {

		// 根据ownerId获取所有的tagVals
		List<AllianceTagVal> tagVals = allianceTagValProvider.listTagValsByOwnerId(sa.getId());
		if (CollectionUtils.isEmpty(tagVals)) {
			return;
		}

		// 获得parentTags
		List<AllianceTag> parentTags = allianceTagProvider.getAllianceParentTagList(null, null,
				UserContext.getCurrentNamespaceId(),sa.getOwnerType(), sa.getOwnerId(), sa.getParentId());
		if (CollectionUtils.isEmpty(parentTags)) {
			return;
		}

		List<AllianceTagGroupDTO> tagGroups = new ArrayList<>(10);
		List<AllianceTagDTO> childTags = null;

		for (AllianceTag parentTag : parentTags) {

			childTags = new ArrayList<>(10);
			for (AllianceTagVal tagVal : tagVals) {
				if (parentTag.getId().equals(tagVal.getTagParentId())) {
					childTags.add(tagVal.getTagDto());
				}
			}

			// 如果查到子筛选，则添加至返回参数中
			if (childTags.size() > 0) {
				AllianceTagGroupDTO group = new AllianceTagGroupDTO();
				group.setParentTag(ConvertHelper.convert(parentTag, AllianceTagDTO.class));
				group.setChildTags(childTags);
				tagGroups.add(group);
			}
		}

		if (tagGroups.size() == 0) {
			return;
		}

		sa.setTagGroups(tagGroups);
	}

	private void createServiceAllianceAttachments(List<ServiceAllianceAttachmentDTO> attachments, Long ownerId,
			Byte attachmentType) {
		if (null == attachments)
			return;

		byte index = 0;
		for (ServiceAllianceAttachmentDTO dto : attachments) {
			if (null == dto.getContentUri())
				continue;
			ServiceAllianceAttachment attachment = ConvertHelper.convert(dto, ServiceAllianceAttachment.class);
			attachment.setOwnerId(ownerId);
			// attachment.setContentType(PostContentType.IMAGE.getCode());
			attachment.setAttachmentType(attachmentType);
			attachment.setDefaultOrder(index++);
			attachment.setCreatorUid(UserContext.current().getUser().getId());
			attachment.setFileSize(dto.getFileSize());

			this.yellowPageProvider.createServiceAllianceAttachments(attachment);
		}
	}

	/**
	 * 配置开始和结束日期，用于 policydeclare 类型
	 */
	private void populateServiceAllianceDate(ServiceAlliances sa) {
		if (null == sa.getStartTime()) { // 如果没有时间，不用处理
			return;
		}

		sa.setStartDate(DateUtil.dateToStr(sa.getStartTime(), DateUtil.YMR_SLASH));
		sa.setEndDate(DateUtil.dateToStr(sa.getEndTime(), DateUtil.YMR_SLASH));
	}

	private void populateServiceAllianceUrl(ServiceAlliances sa) {

		String posterUri = sa.getPosterUri();
		if (posterUri != null && posterUri.length() > 0) {
			try {
				String posterUrl = contentServerService.parserUri(sa.getPosterUri(), EntityType.USER.getCode(),
						UserContext.current().getUser().getId());
				sa.setPosterUrl(posterUrl);
			} catch (Exception e) {
				LOGGER.error("Failed to parse poster uri of ServiceAlliances, ServiceAlliances =" + sa, e);
			}
		}
	}

	private void populateServiceAllianceAttachements(ServiceAlliances sa,
			List<ServiceAllianceAttachment> attachmentList) {

		if (attachmentList == null || attachmentList.size() == 0) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The ServiceAlliances attachment list is empty, ServiceAlliancesId=" + sa.getId());
			}
		} else {
			for (ServiceAllianceAttachment attachment : attachmentList) {
				populateServiceAllianceAttachement(sa, attachment);
			}
		}
	}

	private void populateServiceAllianceAttachement(ServiceAlliances sa, ServiceAllianceAttachment attachment) {

		if (attachment == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("The ServiceAlliances attachment is null, ServiceAlliancesId=" + sa.getId());
			}
		} else {
			String contentUri = attachment.getContentUri();
			if (contentUri != null && contentUri.length() > 0) {
				try {
					String url = contentServerService.parserUri(contentUri, EntityType.USER.getCode(),
							UserContext.current().getUser().getId());
					attachment.setContentUrl(url);
				} catch (Exception e) {
					LOGGER.error("Failed to parse attachment uri, ServiceAlliancesId=" + sa.getId() + ", attachmentId="
							+ attachment.getId(), e);
				}
			} else {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("The content uri is empty, attchmentId=" + attachment.getId());
				}
			}
		}
	}

	@Override
	public void addTarget(AddNotifyTargetCommand cmd) {

		// 校验权限
		checkPrivilege(PrivilegeType.INFO_NOTIFY, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());

		ServiceAllianceNotifyTargets isExist = this.yellowPageProvider.findNotifyTarget(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getCategoryId(), cmd.getContactType(), cmd.getContactToken());
		if (isExist != null) {
			if (ContactType.EMAIL.equals(ContactType.fromCode(cmd.getContactType()))) {
				throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
						YellowPageServiceErrorCode.ERROR_NOTIFY_EMAIL_EXIST, "邮箱已存在，请重新输入");
			}

			if (ContactType.MOBILE.equals(ContactType.fromCode(cmd.getContactType()))) {
				throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
						YellowPageServiceErrorCode.ERROR_NOTIFY_MOBILE_EXIST, "该手机号已添加");
			}

		}
		ServiceAllianceNotifyTargets target = ConvertHelper.convert(cmd, ServiceAllianceNotifyTargets.class);
		target.setNamespaceId(UserContext.getCurrentNamespaceId());

		this.yellowPageProvider.createNotifyTarget(target);

	}

	@Override
	public void deleteNotifyTarget(DeleteNotifyTargetCommand cmd) {

		// 权限限制
		checkPrivilege(PrivilegeType.INFO_NOTIFY, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());

		// ServiceAllianceNotifyTargets target =
		// verifyNotifyTarget(cmd.getOwnerType(), cmd.getOwnerId(),
		// cmd.getId());
		this.yellowPageProvider.deleteNotifyTarget(cmd.getId());
	}

	@Override
	public void setNotifyTargetStatus(SetNotifyTargetStatusCommand cmd) {
		ServiceAllianceNotifyTargets target = verifyNotifyTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
		target.setStatus(cmd.getStatus());
		this.yellowPageProvider.updateNotifyTarget(target);

	}

	private ServiceAllianceNotifyTargets verifyNotifyTarget(String ownerType, Long ownerId, Long id) {
		ServiceAllianceNotifyTargets target = this.yellowPageProvider.findNotifyTarget(ownerType, ownerId, id);
		if (target == null) {
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_NOTIFY_TARGET, "推送者不存在");
		}
		return target;
	}

	@Override
	public ListNotifyTargetsResponse listNotifyTargets(ListNotifyTargetsCommand cmd) {

		if (cmd.getAppId() != null) {
			checkPrivilege(PrivilegeType.INFO_NOTIFY, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());
		}
		ListNotifyTargetsResponse response = new ListNotifyTargetsResponse();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<ServiceAllianceNotifyTargets> targets = this.yellowPageProvider.listNotifyTargets(
				UserContext.getCurrentNamespaceId(), cmd.getContactType(), cmd.getCategoryId(), locator, pageSize + 1);

		if (targets != null && targets.size() > 0) {

			Long nextPageAnchor = null;
			if (targets.size() > pageSize) {
				targets.remove(targets.size() - 1);
				nextPageAnchor = targets.get(targets.size() - 1).getId();
			}

			List<NotifyTargetDTO> dtos = targets.stream().map((target) -> {
				NotifyTargetDTO dto = ConvertHelper.convert(target, NotifyTargetDTO.class);
				return dto;
			}).filter(r -> r != null).collect(Collectors.toList());

			response.setDtos(dtos);

			response.setNextPageAnchor(nextPageAnchor);
		}
		return response;
	}

	@Override
	public void verifyNotifyTarget(VerifyNotifyTargetCommand cmd) {

		ServiceAllianceNotifyTargets target = this.yellowPageProvider.findNotifyTarget(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getCategoryId(), ContactType.MOBILE.getCode(), cmd.getContactToken());
		if (target != null) {
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_NOTIFY_MOBILE_EXIST, "该手机号已添加");
		}

		UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(),
				cmd.getContactToken());
		if (identifier == null) {
			throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE,
					YellowPageServiceErrorCode.ERROR_NOTIFY_TARGET_NOT_REGISTER, "该手机号还未注册，请先注册");
		}
	}

	@Override
	public List<ServiceAllianceCategoryDTO> listServiceAllianceCategories(ListServiceAllianceCategoriesCommand cmd) {
		cmd.setPageAnchor(null);
		cmd.setPageSize(null);
		ListServiceAllianceCategoriesAdminResponse resp = listServiceAllianceCategoriesByScene(cmd);
		return resp.getDtos();
	}

	@Override
	public ListServiceAllianceCategoriesAdminResponse listServiceAllianceCategoriesByAdmin(
			ListServiceAllianceCategoriesCommand cmd) {
		
		List<Byte> displayDestination = new ArrayList<>();
		if (cmd.getDestination() != null) {
			displayDestination.add(cmd.getDestination());
			displayDestination.add(ServiceAllianceCategoryDisplayDestination.BOTH.getCode());
		}

		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		//先获取主样式
		Long type = cmd.getParentId();
		List<ServiceAllianceCategories> entityResultList = allianceStandardService.listChildCategoriesByAdmin(locator,
				cmd.getPageSize(), cmd.getOwnerType(), cmd.getOwnerId(), null, type);
		List<ServiceAllianceCategoryDTO> dtos = null;
		if (null != entityResultList) {
			dtos = entityResultList.stream().map(r -> {
				ServiceAllianceCategoryDTO dto = ConvertHelper.convert(r, ServiceAllianceCategoryDTO.class);
				String locale = UserContext.current().getUser().getLocale();
				String displayCategoryName = localeStringService.getLocalizedString(
						ServiceAllianceLocalStringCode.CATEGORY_DISPLAY_SCOPE, String.valueOf(dto.getDisplayMode()), locale,
						"");
				dto.setDisplayModeName(displayCategoryName);
				return dto;
			}).collect(Collectors.toList());
		}

		// 组织返回数据
		ListServiceAllianceCategoriesAdminResponse resp = new ListServiceAllianceCategoriesAdminResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}
	
	@Override
	public ListServiceAllianceCategoriesAdminResponse listServiceAllianceCategoriesByScene(
			ListServiceAllianceCategoriesCommand cmd) {
		
		List<Byte> displayDestination = new ArrayList<>();
		if (cmd.getDestination() != null) {
			displayDestination.add(cmd.getDestination());
			displayDestination.add(ServiceAllianceCategoryDisplayDestination.BOTH.getCode());
		}

		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		//先获取主样式
		Long type = cmd.getParentId();
		List<ServiceAllianceCategories> entityResultList = allianceStandardService.listChildCategoriesByScene(locator,
				cmd.getPageSize(), cmd.getOwnerType(), cmd.getOwnerId(), null, type);
		List<ServiceAllianceCategoryDTO> dtos = null;
		if (null != entityResultList) {
			dtos = entityResultList.stream().map(r -> {
				ServiceAllianceCategoryDTO dto = ConvertHelper.convert(r, ServiceAllianceCategoryDTO.class);
				String locale = UserContext.current().getUser().getLocale();
				String displayCategoryName = localeStringService.getLocalizedString(
						ServiceAllianceLocalStringCode.CATEGORY_DISPLAY_SCOPE, String.valueOf(dto.getDisplayMode()), locale,
						"");
				dto.setDisplayModeName(displayCategoryName);
				return dto;
			}).collect(Collectors.toList());
		}

		// 组织返回数据
		ListServiceAllianceCategoriesAdminResponse resp = new ListServiceAllianceCategoriesAdminResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}
	@Override
	public ServiceAllianceDisplayModeDTO getServiceAllianceDisplayMode(GetServiceAllianceDisplayModeCommand cmd) {
		ServiceAllianceDisplayModeDTO displayModeDTO = new ServiceAllianceDisplayModeDTO();
		displayModeDTO.setDisplayMode(ServiceAllianceCategoryDisplayMode.LIST.getCode());

		List<Byte> displayDestination = new ArrayList<>();
		if (cmd.getDestination() != null) {
			displayDestination.add(cmd.getDestination());
			displayDestination.add(ServiceAllianceCategoryDisplayDestination.BOTH.getCode());
		}

		ServiceAllianceCategories parentCategory = this.yellowPageProvider.findCategoryById(cmd.getParentId());
		if (parentCategory != null
				&& ServiceAllianceCategoryDisplayMode.fromCode(parentCategory.getDisplayMode()) != null) {
			displayModeDTO.setDisplayMode(parentCategory.getDisplayMode());
			// List<ServiceAllianceCategories> childCategories =
			// this.yellowPageProvider.listChildCategories(null, null,
			// UserContext.getCurrentNamespaceId(), parentCategory.getId(),
			// CategoryAdminStatus.ACTIVE, displayDestination);
			// if (childCategories != null && childCategories.size() > 0) {
			// displayModeDTO.setDisplayMode(childCategories.get(0).getDisplayMode());
			// }
		}
		return displayModeDTO;
	}

	@Override
	public List<JumpModuleDTO> listJumpModules(ListJumpModulesCommand cmd) {


		List<JumpModuleDTO> modules = new ArrayList<>(10);

		// 获取电商模块
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		List<JumpModuleDTO> bizJumpModules = yellowPageProvider.jumpModules(namespaceId, "BIZS");
		if (!CollectionUtils.isEmpty(bizJumpModules)) {
			modules.addAll(bizJumpModules);
		}

		// 获取非电商
		List<JumpModuleDTO> otherModules = yellowPageProvider.jumpModules(0, null);
		if (!CollectionUtils.isEmpty(otherModules)) {
			modules.addAll(otherModules);
		}

		// 过滤未配置的应用
		modules = reAdjustModules(namespaceId, cmd.getVersionId(), modules);

		return createTree(modules);
	}
	
	private List<JumpModuleDTO> getBizsModuleDto(JumpModuleDTO bisModuleDto) {


		Map<String, String> param = new HashMap<>();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		param.put("namespaceId", String.valueOf(namespaceId));
		List<JumpModuleDTO> bizModules = new ArrayList<>();


		String json = post(createRequestParam(param), "/zl-ec/rest/openapi/shop/queryShopInfoByNamespace");
		if (null != json) {
			ReserverEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<ReserverEntity<Object>>() {
			});
			if (null != entity) {
				Object obj = entity.getBody();
				if (null != obj) {
					List<BizEntity> bizs = JSONObject.parseObject(obj.toString(), new TypeReference<List<BizEntity>>() {
					});
					;
					for (BizEntity b : bizs) {
						JumpModuleDTO d = new JumpModuleDTO();
						// long id =
						// this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceJumpModule.class));
						// d.setId(id);
						d.setModuleName(b.getShopName());
						try {
							d.setModuleUrl(String.format("zl://browser/i?url=%s",
									URLEncoder.encode(b.getShopURL(), StandardCharsets.UTF_8.name())));
						} catch (UnsupportedEncodingException e) {

							LOGGER.error("encode failed getShopURL:" + b.getShopURL() + " name:" + StandardCharsets.UTF_8.name());

						}
						d.setNamespaceId(namespaceId);
						d.setParentId(bisModuleDto.getId());
						bizModules.add(d);

					}
				}
			}
		}
		

		return bizModules;

	}

	/**
	 * 根据实际数据，删除未配置模块url，并新增多应用url
	 * 
	 * @param dtos
	 */
	private List<JumpModuleDTO> reAdjustModules(Integer inputNamespaceId, Long inputVersionId,
			List<JumpModuleDTO> dtos) {
		// 1.判空
		if (CollectionUtils.isEmpty(dtos)) {
			return dtos;
		}

		// 2.校验入参
		Integer namespaceId = inputNamespaceId == null ? UserContext.getCurrentNamespaceId() : inputNamespaceId;
		Long versionId = inputVersionId;
		if (null == versionId) {
			// 如果传过来是空，就选最大版本
			PortalVersion maxBigVersion = portalVersionProvider.findMaxBigVersion(namespaceId);
			versionId = maxBigVersion.getId();
		}

		// 3.对每个传入的模块进行处理
		boolean hasDealBizs = false; 
		List<JumpModuleDTO> reAdjustJumpDtos = new ArrayList<>();
		for (JumpModuleDTO dto : dtos) {
			// 3.1 电商item特殊处理
			if ("BIZS".equals(dto.getModuleUrl())) {
				if (hasDealBizs) {
					//电商只处理一次
					continue;
				}
				hasDealBizs = true;
				List<JumpModuleDTO> bizMds = getBizsModuleDto(dto);
				if (!CollectionUtils.isEmpty(bizMds)) {
					reAdjustJumpDtos.addAll(bizMds);
				}

				continue;
			}

			// 3.2 获取该域空间下的模块
			List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, versionId,
					dto.getModuleId());
			if (CollectionUtils.isEmpty(apps)) {
				continue;

			}

			// 3.3根据实际数据生成应用数据，可能有多个
			List<JumpModuleDTO> reDtos = checkSpecificApp(apps, dto);
			if (!CollectionUtils.isEmpty(reDtos)) {
				reAdjustJumpDtos.addAll(reDtos);
			}
		}

		return reAdjustJumpDtos;
	}

	/**
	 * 检查多应用共用同一模块的情况，如物业报修/投诉建议 如有多应用，返回多个新增moduleDto
	 * 
	 * @param filterIdList
	 *            需要过滤的ID
	 * @param specificModule
	 *            需要检查的应用
	 */
	private List<JumpModuleDTO> checkSpecificApp(List<ServiceModuleApp> apps, JumpModuleDTO dto) {
		final long APARTMENT_SERVICE_MODULE_ID = 20100L; // 物业报修/投诉建议
		final long PARK_IN_MODULE_ID = 40100L; // 园区入驻
		final long ARTICLES_PASS_MODULE_ID = 49200L; // 物品放行
		List<JumpModuleDTO> dtos = new ArrayList<>(10);

		// 若未获取配置，直接返回dto
		String instanceConfig = dto.getInstanceConfig();
		if (StringUtils.isEmpty(instanceConfig)) {
			dtos.add(dto);
			return dtos;
		}

		// 物业报修/投诉建议
		if (APARTMENT_SERVICE_MODULE_ID == dto.getModuleId()) {
			return CheckApartmentServiceAppMatch(apps, dto);
		}

		// 园区入驻
		if (PARK_IN_MODULE_ID == dto.getModuleId()) {
			return CheckParkInAppMatch(apps, dto);
		}

		// 物品放行
		if (ARTICLES_PASS_MODULE_ID == dto.getModuleId()) {
			return CheckArticlesPassAppMatch(apps, dto);
		}

		dtos.add(dto);
		return dtos;
	}

	/**
	 * 检查是否匹配物业报修模块 物业报修："taskCategoryId":6 投诉建议："taskCategoryId":9
	 */
	private List<JumpModuleDTO> CheckApartmentServiceAppMatch(List<ServiceModuleApp> apps, JumpModuleDTO dto) {

		// 获取taskCategoryId
		final String TASK_CATEGORY_ID_KEY = "taskCategoryId";
		String jumpConfig = dto.getInstanceConfig();
		JSONObject json = JSONObject.parseObject(jumpConfig);
		Long jumpCheckId = json.getLong(TASK_CATEGORY_ID_KEY);
		if (null == jumpCheckId) {
			return null;
		}

		// 获取url相关配置
		String skipRoute = json.getString("skipRoute"); // 跳转路由，如zl://browser/i?url=
		String prefix = json.getString("prefix"); // 业务前缀
		String suffix = json.getString("suffix"); // 业务后缀

		// 对当前应用进行匹配
		for (ServiceModuleApp app : apps) {
			String appConfig = app.getInstanceConfig();
			if (StringUtils.isEmpty(appConfig)) {
				continue;
			}

			JSONObject appJson = JSONObject.parseObject(appConfig);
			Long taskCategoryId = appJson.getLong(TASK_CATEGORY_ID_KEY);
			String url = appJson.getString("url");
			if (null == taskCategoryId) {
				if (StringUtils.isEmpty(url)) {
					continue;
				}

				// 截取url的参数部分
				int index = url.indexOf("?");
				if (index < 0) {
					continue;
				}

				// 获取特定参数
				url = url.substring(index + 1);
				String[] items = url.split("&");
				String findStr = TASK_CATEGORY_ID_KEY + "=";
				for (String item : items) {
					if (item.startsWith(findStr)) {
						taskCategoryId = Long.parseLong(item.substring(findStr.length()));
					}
				}

				// 如果未找到，继续
				if (null == taskCategoryId) {
					continue;
				}
			}

			// 比较两者是否相同
			if (!jumpCheckId.equals(taskCategoryId)) {
				continue;
			}

			Map<String, String> normalParams = new HashMap<>(10);
			normalParams.put("taskCategoryId", taskCategoryId + "");
			normalParams.put("type", "user");
			normalParams.put("ns", app.getNamespaceId() + "");

			Map<String, String> encodeParams = new HashMap<>(10);
			encodeParams.put("displayName", app.getName() + "");

			// 生成url
			String finalModuleUrl = generateModuleUrl(skipRoute, prefix, normalParams, encodeParams, suffix);

			dto.setModuleUrl(finalModuleUrl.toString());
			List<JumpModuleDTO> dtos = new ArrayList<>(1);
			dtos.add(dto);
			return dtos;
		}

		return null;
	}

	/**
	 * 检查是否园区入驻
	 */
	private List<JumpModuleDTO> CheckParkInAppMatch(List<ServiceModuleApp> apps, JumpModuleDTO dto) {

		List<JumpModuleDTO> dtos = new ArrayList<>(10);

		JSONObject json = JSONObject.parseObject(dto.getInstanceConfig());
		String skipRoute = json.getString("skipRoute"); // 跳转路由，如zl://browser/i?url=

		for (ServiceModuleApp app : apps) {
			// 获取categoryId
			// Long categoryId = 1L;
			// String appConfig = app.getInstanceConfig();
			// if (!StringUtils.isEmpty(appConfig)) {
			// JSONObject appJson = JSONObject.parseObject(appConfig);
			// Long newCategoryId = appJson.getLong("categoryId");
			// if (null != newCategoryId) {
			// categoryId = newCategoryId;
			// }
			// }

			// IOS兼容旧版本的要求，参数需要传rentType=，希望后续可以去掉这个奇怪的东西
			Map<String, String> normalParams = new HashMap<>(10);
			normalParams.put("rentType", "");

			Map<String, String> encodeParams = new HashMap<>(10);
			encodeParams.put("displayName", app.getName() + "");

			// 生成url
			String finalModuleUrl = generateModuleUrl(skipRoute, null, normalParams, encodeParams, null);

			JumpModuleDTO newDto = new JumpModuleDTO();
			newDto.setModuleName(dto.getModuleName());
			newDto.setModuleUrl(finalModuleUrl);
			dtos.add(newDto);
		}

		return dtos;
	}

	/**
	 * 检查是否物品搬迁 这是一个h5链接，需要url
	 * http://beta.zuolin.com/goods-move/build/index.html?ns=1000000&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix
	 */
	private List<JumpModuleDTO> CheckArticlesPassAppMatch(List<ServiceModuleApp> apps, JumpModuleDTO dto) {
		// 获取配置
		JSONObject json = JSONObject.parseObject(dto.getInstanceConfig());
		String skipRoute = json.getString("skipRoute"); // 跳转路由，如zl://browser/i?url=
		String prefix = json.getString("prefix"); // 业务前缀
		String suffix = json.getString("suffix"); // 业务后缀

		List<JumpModuleDTO> dtos = new ArrayList<>(10);
		for (ServiceModuleApp app : apps) {

			Map<String, String> params = new HashMap<>(10);
			params.put("ns", app.getNamespaceId() + "");
			params.put("hideNavigationBar", "1");
			params.put("ehnavigatorstyle", "0");

			// 根据参数生成模块url
			String finalModuleUrl = generateModuleUrl(skipRoute, prefix, params, null, suffix);

			JumpModuleDTO newDto = new JumpModuleDTO();
			newDto.setModuleName(dto.getModuleName());
			newDto.setModuleUrl(finalModuleUrl);
			dtos.add(newDto);
		}

		return dtos;
	}

	/**
	 * @Function: YellowPageServiceImpl.java
	 * @Description: 设置最终的url
	 * @param skipRoute
	 *            跳转路由 如zl://browser/i?url=
	 * @param prefix
	 * @param normalParams
	 *            普通参数map
	 * @param needEncodeParams
	 *            需要编码的参数map
	 * @param suffix
	 *            后缀
	 * @param needEncode
	 *            最后的url是否需要编码
	 * 
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年5月30日 上午10:29:44
	 *
	 */
	private String generateModuleUrl(String skipRoute, String prefix, Map<String, String> normalParams,
			Map<String, String> needEncodeParams, String suffix) {

		StringBuilder moduleUrl = new StringBuilder();
		boolean needEncode = false;

		// 判断是否为H5链接
		if ("zl://browser/i?url=".equals(skipRoute)) {
			String homeUrl = configProvider.getValue(0, "home.url", "core.zuolin.com");
			needEncode = true;
			prefix = homeUrl + prefix;

			if (StringUtils.isEmpty(suffix)) {
				suffix = "#/home#sign_suffix"; // h5默认的结尾参数
			}
		}

		if (!StringUtils.isEmpty(prefix)) {
			moduleUrl.append(prefix);
		}

		boolean firstParam = true;
		// 普通的参数
		if (null != normalParams) {

			for (Map.Entry<String, String> entry : normalParams.entrySet()) {

				if (!firstParam) {
					moduleUrl.append("&");
				} else {
					moduleUrl.append("?");
					firstParam = false;
				}

				moduleUrl.append(entry.getKey() + "=" + entry.getValue());
			}
		}

		// 需要编码的参数
		if (null != needEncodeParams) {
			for (Map.Entry<String, String> entry : needEncodeParams.entrySet()) {

				if (!firstParam) {
					moduleUrl.append("&");
				} else {
					moduleUrl.append("?");
					firstParam = false;
				}

				String encodeValue = entry.getKey();
				try {
					// 失败时不返回异常
					encodeValue = URLEncoder.encode(entry.getValue(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("encode failed key:" + entry.getKey() + " value:" + entry.getValue());
				}

				moduleUrl.append(entry.getKey() + "=" + encodeValue);
			}
		}

		// 添加后缀
		if (!StringUtils.isEmpty(suffix)) {
			moduleUrl.append(suffix);
		}

		// 编码判断
		String encodeModuleUrl = moduleUrl.toString();
		if (needEncode) { // h5页面链接需要进行编码
			try {
				// 失败时不返回异常
				encodeModuleUrl = URLEncoder.encode(encodeModuleUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("encode failed encodeModuleUrl:" + encodeModuleUrl);
			}
		}

		return skipRoute + encodeModuleUrl;
	}

	private List<JumpModuleDTO> createTree(List<JumpModuleDTO> modules) {
		List<JumpModuleDTO> nodeList = new ArrayList<>();
		for (JumpModuleDTO node1 : modules) {
			boolean mark = false;
			for (JumpModuleDTO node2 : modules) {
				if (node1.getParentId() != null && node1.getParentId() != 0L
						&& node1.getParentId().equals(node2.getId())) {
					mark = true;
					if (node2.getChildren() == null)
						node2.setChildren(new ArrayList<JumpModuleDTO>());
					node2.getChildren().add(node1);
					break;
				}
			}
			if (!mark) {
				nodeList.add(node1);
			}
		}
		return nodeList;
	}

	@Override
	public ListAttachmentsResponse listAttachments(ListAttachmentsCommand cmd) {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
		if (cmd.getPageSize() == null) {
			int value = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
			cmd.setPageSize(value);
		}

		ListAttachmentsResponse response = new ListAttachmentsResponse();
		List<ServiceAllianceAttachment> attachments = yellowPageProvider.listAttachments(locator, cmd.getPageSize() + 1,
				cmd.getOwnerId());
		if (attachments != null && attachments.size() > 0) {
			if (attachments.size() > cmd.getPageSize()) {
				attachments.remove(attachments.size() - 1);
				response.setNextPageAnchor(attachments.get(attachments.size() - 1).getId());
			}

			List<AttachmentDTO> dtos = attachments.stream().map((r) -> {
				AttachmentDTO dto = ConvertHelper.convert(r, AttachmentDTO.class);
				String contentUrl = contentServerService.parserUri(dto.getContentUri(), EntityType.USER.getCode(),
						UserContext.current().getUser().getId());
				User creator = userProvider.findUserById(dto.getCreatorUid());
				if (creator != null) {
					dto.setCreatorName(creator.getNickName());
				}
				dto.setContentUrl(contentUrl);
				return dto;
			}).collect(Collectors.toList());
			response.setAttachments(dtos);
		}

		return response;
	}

	private String post(Map<String, String> param, String method) {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String serverUrl = configurationProvider.getValue("position.reserver.serverUrl", "");

		HttpPost httpPost = new HttpPost(serverUrl + method);
		CloseableHttpResponse response = null;

		String json = null;
		try {
			String p = StringHelper.toJsonString(param);
			StringEntity stringEntity = new StringEntity(p, StandardCharsets.UTF_8);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("content-type", "application/json");

			response = httpclient.execute(httpPost);

			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					json = EntityUtils.toString(entity, "utf8");
				}
			}
		} catch (IOException e) {
			LOGGER.error("Reserver request error, param={}", param, e);
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_QUERY_BIZ_MODULE_FAILED, "get biz module failed");
		} finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("Reserver close instream, response error, param={}", param, e);
				}
			}
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Data from business, param={}, json={}", param, json);

		return json;
	}

	private Map createRequestParam(Map<String, String> params) {
		Integer nonce = (int) (Math.random() * 1000);
		Long timestamp = System.currentTimeMillis();

		String appKey = configurationProvider.getValue("position.reserver.appKey", "");
		String secretKey = configurationProvider.getValue("position.reserver.secretKey", "");
		params.put("nonce", String.valueOf(nonce));
		params.put("timestamp", String.valueOf(timestamp));
		params.put("appKey", appKey);

		Map<String, String> mapForSignature = new HashMap<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			mapForSignature.put(entry.getKey(), entry.getValue());
		}

		String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
		try {
			params.put("signature", URLEncoder.encode(signature, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return params;
	}

	@Override
	public void updateServiceAllianceEnterpriseDisplayFlag(UpdateServiceAllianceEnterpriseDisplayFlagCommand cmd) {
		if (cmd.getId() == null) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_INPUT_PARAM_NOT_VALID, "param not valid");
		}
		DisplayFlagType flagType = DisplayFlagType.fromCode(cmd.getDisplayFlag());
		if (flagType != null) {
			ServiceAlliances serviceAlliance = yellowPageProvider.findServiceAllianceById(cmd.getId(), null, null);
			if (serviceAlliance == null) {
				YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_INPUT_PARAM_NOT_VALID, "param not valid");
			}
			cmd.setDisplayFlag(flagType.getCode());
		}
		yellowPageProvider.updateServiceAlliancesDisplayFlag(cmd.getId(), cmd.getDisplayFlag());
	}

	@Override
	public ServiceAllianceListResponse updateServiceAllianceEnterpriseDefaultOrder(
			UpdateServiceAllianceEnterpriseDefaultOrderCommand cmd) {
		List<ServiceAllianceDTO> values = cmd.getValues();
		if (values == null || values.size() < 2) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_INPUT_PARAM_NOT_VALID, "param not valid");
		}
		// 检查数据,并且查询原来的defaultorder，并且按照defaultorder升序生成serviceAlliancesList by
		// dengs,20170525
		List<ServiceAlliances> serviceAlliancesList = checkServiceAllianceEnterpriseOrder(values);
		List<ServiceAlliances> updateList = new ArrayList<ServiceAlliances>();

		for (int i = 0; i < serviceAlliancesList.size(); i++) {
			ServiceAlliances serviceAlliances = new ServiceAlliances();
			serviceAlliances.setId(values.get(i).getId());// 原始id
			serviceAlliances.setDefaultOrder(serviceAlliancesList.get(i).getDefaultOrder());// 排序后的顺序
			updateList.add(serviceAlliances);
		}

		yellowPageProvider.updateOrderServiceAllianceDefaultOrder(updateList);

		// 返回更新后的结果
		ServiceAllianceListResponse response = new ServiceAllianceListResponse();
		response.setDtos(updateList.stream().map(r -> ConvertHelper.convert(r, ServiceAllianceDTO.class))
				.collect(Collectors.toList()));
		return response;
	}

	/**
	 * 检查需要排序的服务联盟集合的id和defaultOrder
	 */
	// private Map<String, Long>
	// checkServiceAllianceEnterpriseOrder(List<ServiceAllianceDTO> values) {
	private List<ServiceAlliances> checkServiceAllianceEnterpriseOrder(List<ServiceAllianceDTO> values) {
		Map<String, Long> idOrderMap = new HashMap<String, Long>();

		List<ServiceAlliances> serviceAllianceList = yellowPageProvider.listServiceAllianceSortOrders(
				values.stream().map(value -> value.getId()).collect(Collectors.toList()));

		if (values.size() != serviceAllianceList.size()) {
			LOGGER.error("Uknown Ids = {}" , values);
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_INPUT_PARAM_NOT_VALID, "param not valid");
		}

		Collections.sort(serviceAllianceList, (s1, s2) -> {
			if (s1.getDefaultOrder() - s2.getDefaultOrder() == 0L) {
				LOGGER.error("repeated service alliance id = {}" , s1.getId());
				YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_PROVIDER_NOT_FOUND, "alliance provider not exist");
			}
			return s1.getDefaultOrder() > s2.getDefaultOrder() ? 1 : -1;
		});

		// for (ServiceAlliances serviceAlliances : serviceAllianceList) {
		// String key = String.valueOf(serviceAlliances.getId());
		// //检查前端传入的集合中，存在重复的服务联盟企业的情况。抛出异常。
		// if(idOrderMap.containsKey(key)){
		//
		// }
		// idOrderMap.put(key, serviceAlliances.getDefaultOrder());
		// }
		// return idOrderMap;
		return serviceAllianceList;
	}

	@Override
	public GetCategoryIdByEntryIdResponse getCategoryIdByEntryId(GetCategoryIdByEntryIdCommand cmd) {
		if (cmd.getEntryId() == null) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_PROVIDER_NOT_FOUND, "alliance provider not exist");
		}
		ServiceAllianceCategories category = yellowPageProvider
				.findCategoryByEntryId(UserContext.getCurrentNamespaceId(), cmd.getEntryId());
		GetCategoryIdByEntryIdResponse resp = new GetCategoryIdByEntryIdResponse();
		if (category != null) {
			resp.setCategoryId(category.getId());
		}
		return resp;
	}

	@Override
	public void syncOldForm() {
		List<ServiceAlliances> oldFormSAList = yellowPageProvider.findOldFormServiceAlliance();
		if (oldFormSAList != null && oldFormSAList.size() > 0) {
			Map<String, GeneralForm> formMap = generateFormMap();
			oldFormSAList.forEach(sa -> {
				if (JumpType.fromCode(sa.getIntegralTag1()) == JumpType.TEMPLATE) {
					sa.setIntegralTag3(JumpType.TEMPLATE.getCode());
					sa.setStringTag3(sa.getStringTag2());

					GeneralForm form = createForm(sa, formMap.get(sa.getStringTag2()));
					sa.setIntegralTag1(JumpType.FORM.getCode());
					sa.setModuleUrl("zl://approval/create?approvalId=-1&sourceId=" + sa.getId() + "&formId="
							+ form.getFormOriginId());
					// 替换moduleUrl,替换格式如此
					// zl://approval/create?approvalId=-1&sourceId=6&formId=1
					if (replaceModuleUrl(sa)) {
						this.yellowPageProvider.updateServiceAlliances(sa);
					}
				}
			});
		} else {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"未查询到使用老表单的机构，如需再次同步，请删除相关数据重试");
		}
	}

	private GeneralForm createForm(ServiceAlliances sa, GeneralForm generalForm) {
		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(sa.getParentId());
		GeneralForm form = generalFormProvider.getGeneralFormByTag1(category.getNamespaceId(),
				FlowModuleType.SERVICE_ALLIANCE.getCode(), sa.getParentId(), sa.getStringTag2());
		if (form == null) {
			generalForm.setNamespaceId(UserContext.getCurrentNamespaceId());
			generalForm.setModuleType(FlowModuleType.SERVICE_ALLIANCE.getCode());
			generalForm.setModuleId(sa.getParentId());
			generalForm.setStringTag1(sa.getStringTag2());
			generalForm.setOwnerType("EhOrganizations");
			generalForm.setOwnerId(sa.getOwnerId());
			this.generalFormProvider.createGeneralForm(generalForm);
			return generalForm;
		}
		return form;
	}

	private Map<String, GeneralForm> generateFormMap() {
		Map<String, GeneralForm> map = new HashMap<>();
		List<RequestTemplates> oldFormTemplateList = userActivityProvider.listCustomRequestTemplates();
		oldFormTemplateList.forEach(template -> {
			List<FieldDTO> fieldDTOList = analyzefields(template.getFieldsJson());
			List<GeneralFormFieldDTO> generalFormFieldDTOList = new ArrayList<GeneralFormFieldDTO>();
			addDefaultFormFieldDTO(generalFormFieldDTOList);
			generalFormFieldDTOList.addAll(
					fieldDTOList.stream().map(r -> convertFieldToGeneralFormDTO(r)).collect(Collectors.toList()));
			GeneralForm form = new GeneralForm();
			form.setTemplateType(GeneralFormTemplateType.DEFAULT_JSON.getCode());
			form.setStatus(GeneralFormStatus.CONFIG.getCode());
			form.setNamespaceId(UserContext.getCurrentNamespaceId());
			form.setFormVersion(0L);
			form.setTemplateText(JSON.toJSONString(generalFormFieldDTOList));
			form.setDeleteFlag(Byte.valueOf("1"));
			form.setModifyFlag(Byte.valueOf("1"));
			form.setFormAttribute(GeneralApprovalAttribute.CUSTOMIZE.getCode());
			form.setStringTag1(template.getTemplateType());
			form.setFormName(template.getName());
			// this.generalFormProvider.createGeneralForm(form);
			map.put(template.getTemplateType(), form);
		});
		return map;
	}

	// 同步接口同步时候才使用,这里就在方法内创建对象
	private void addDefaultFormFieldDTO(List<GeneralFormFieldDTO> generalFormFieldDTOList) {
		String defaultfieldstrings = "[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"联系电话\",\"fieldExtra\":\"{\\\"limitLength\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"INTEGER_TEXT\",\"renderType\":\"DEFAULT\",\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"企业\",\"fieldExtra\":\"{}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_ADDRESS\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋门牌\",\"fieldExtra\":\"{}\",\"fieldName\":\"USER_ADDRESS\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"}]";
		generalFormFieldDTOList
				.addAll(JSONObject.parseObject(defaultfieldstrings, new TypeReference<List<GeneralFormFieldDTO>>() {
				}));
	}

	private GeneralFormFieldDTO convertFieldToGeneralFormDTO(FieldDTO r) {
		GeneralFormFieldDTO dto = new GeneralFormFieldDTO();
		dto.setFieldDisplayName(r.getFieldDisplayName());
		dto.setFieldExtra("{}");
		dto.setFieldName(r.getFieldDisplayName());
		dto.setFieldType(convertFieldType(r.getFieldType(), r.getFieldContentType()));
		dto.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
		dto.setRequiredFlag(r.getRequiredFlag());
		dto.setValidatorType(convertVaildatorType(r.getFieldType()));
		dto.setVisibleType(GeneralFormDataVisibleType.EDITABLE.getCode());
		dto.setFieldDesc(r.getFieldDesc());
		return dto;
	}

	private String convertVaildatorType(String fieldType) {
		switch (fieldType) {
		case "string":
		case "number":
		case "decimal":
			return GeneralFormValidatorType.TEXT_LIMIT.getCode();
		case "blob":
			return GeneralFormValidatorType.IMAGE_COUNT_SIZE_LIMIT.getCode();
		}
		return null;
	}

	private String convertFieldType(String fieldType, String fieldContentType) {
		switch (fieldType) {
		case "string":
		case "number":
		case "decimal":
			return GeneralFormFieldType.SINGLE_LINE_TEXT.getCode();
		case "blob":
			if ("file".equals(fieldContentType))
				return GeneralFormFieldType.FILE.getCode();
			return GeneralFormFieldType.IMAGE.getCode();
		}
		return null;
	}

	private List<FieldDTO> analyzefields(String fieldsJson) {
		Gson gson = new Gson();
		FieldTemplateDTO fields = gson.fromJson(fieldsJson, new TypeToken<FieldTemplateDTO>() {
		}.getType());
		List<FieldDTO> dto = fields.getFields();
		return dto;
	}

	@Override
	public void syncServiceAllianceApplicationRecords() {
		if (saapplicationRecordProvider.listServiceAllianceApplicationRecord(0L, 1).size() > 0) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"已迁移过数据了！");
		}
		generateApplicationRecordsFromServiceAlliance();
		generateApplicationRecordsFromReservation();
		generateApplicationRecordsFromSettleRequestInfoSearcherDb();
		generateApplicationRecordsFromServiceAllianceApartment();
		generateApplicationRecordsFromServiceAllianceInvest();
		generateApplicationRecordsFromServiceAllianceFlowCases();
	}

	private void generateApplicationRecordsFromServiceAllianceFlowCases() {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		SearchFlowCaseCommand cmd = new SearchFlowCaseCommand();
		cmd.setFlowCaseSearchType(FlowCaseSearchType.APPLIER.getCode());
		cmd.setModuleId(ServiceAllianceFlowModuleListener.MODULE_ID);
		for (;;) {
			List<FlowCaseDetail> details = flowCaseProvider.listFlowCasesByModuleId(locator, 20, cmd);
			List<ServiceAllianceRequestInfo> list = saRequestInfoSearcher
					.generateUpdateServiceAllianceFlowCaseRequests(details);

			for (ServiceAllianceRequestInfo request : list) {
				ServiceAllianceApplicationRecord appRecord = ConvertHelper.convert(request,
						ServiceAllianceApplicationRecord.class);
				appRecord.setCreateDate(appRecord.getCreateTime());
				Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
				if (org != null) {
					appRecord.setCreatorOrganization(org.getName());
				}
				ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(),
						request.getOwnerType(), request.getOwnerId());
				if (sa != null) {
					appRecord.setServiceOrganization(sa.getName());
				}
				ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getType());
				appRecord.setNamespaceId(categories == null ? null : categories.getNamespaceId());
				appRecord.setServiceAllianceId(request.getServiceAllianceId());
				saapplicationRecordProvider.createServiceAllianceApplicationRecord(appRecord);
			}

			if (locator.getAnchor() == null) {
				break;
			}
		}

	}

	private void generateApplicationRecordsFromServiceAllianceInvest() {

		CrossShardListingLocator locator = new CrossShardListingLocator();
		for (;;) {
			List<ServiceAllianceInvestRequests> requests = yellowPageProvider.listInvestRequests(locator, 20);
			if (requests.size() > 0) {
				for (ServiceAllianceInvestRequests request : requests) {
					ServiceAllianceApplicationRecord appRecord = ConvertHelper.convert(request,
							ServiceAllianceApplicationRecord.class);
					appRecord.setJumpType(JumpType.TEMPLATE.getCode());
					appRecord.setTemplateType(CustomRequestConstants.INVEST_REQUEST_CUSTOM);
					appRecord.setCreateTime(request.getCreateTime());
					appRecord.setCreateDate(request.getCreateTime());
					appRecord.setCreatorUid(request.getCreatorUid());
					appRecord.setFlowCaseId(null);
					appRecord.setSecondCategoryId(request.getCategoryId());
					ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
					appRecord.setSecondCategoryName(categories == null ? null : categories.getName());
					appRecord.setNamespaceId(categories == null ? null : categories.getNamespaceId());
					appRecord.setServiceAllianceId(request.getServiceAllianceId());
					appRecord.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
					Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
					if (org != null) {
						appRecord.setCreatorOrganization(org.getName());
					}
					ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(),
							request.getOwnerType(), request.getOwnerId());
					if (sa != null) {
						appRecord.setServiceOrganization(sa.getName());
					}
					saapplicationRecordProvider.createServiceAllianceApplicationRecord(appRecord);
				}
			}
			if (locator.getAnchor() == null) {
				break;
			}
		}

	}

	private void generateApplicationRecordsFromServiceAllianceApartment() {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		for (;;) {
			List<ServiceAllianceApartmentRequests> requests = yellowPageProvider.listApartmentRequests(locator, 20);
			if (requests.size() > 0) {
				for (ServiceAllianceApartmentRequests request : requests) {
					ServiceAllianceApplicationRecord appRecord = ConvertHelper.convert(request,
							ServiceAllianceApplicationRecord.class);
					appRecord.setJumpType(JumpType.TEMPLATE.getCode());
					appRecord.setTemplateType(CustomRequestConstants.APARTMENT_REQUEST_CUSTOM);
					appRecord.setCreateTime(request.getCreateTime());
					appRecord.setCreateDate(request.getCreateTime());
					appRecord.setCreatorUid(request.getCreatorUid());
					appRecord.setFlowCaseId(null);
					appRecord.setSecondCategoryId(request.getCategoryId());
					ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
					appRecord.setSecondCategoryName(categories == null ? null : categories.getName());
					appRecord.setNamespaceId(categories == null ? null : categories.getNamespaceId());
					appRecord.setServiceAllianceId(request.getServiceAllianceId());
					appRecord.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
					Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
					if (org != null) {
						appRecord.setCreatorOrganization(org.getName());
					}
					ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(),
							request.getOwnerType(), request.getOwnerId());
					if (sa != null) {
						appRecord.setServiceOrganization(sa.getName());
					}
					saapplicationRecordProvider.createServiceAllianceApplicationRecord(appRecord);
				}
			}
			if (locator.getAnchor() == null) {
				break;
			}
		}
	}

	private void generateApplicationRecordsFromReservation() {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		for (;;) {
			List<ReservationRequests> requests = yellowPageProvider.listReservationRequests(locator, 20);
			if (requests.size() > 0) {
				for (ReservationRequests request : requests) {
					ServiceAllianceApplicationRecord appRecord = ConvertHelper.convert(request,
							ServiceAllianceApplicationRecord.class);
					appRecord.setJumpType(JumpType.TEMPLATE.getCode());
					appRecord.setTemplateType(CustomRequestConstants.RESERVE_REQUEST_CUSTOM);
					appRecord.setCreateTime(request.getCreateTime());
					appRecord.setCreateDate(request.getCreateTime());
					appRecord.setCreatorUid(request.getCreatorUid());
					appRecord.setFlowCaseId(null);
					appRecord.setSecondCategoryId(request.getCategoryId());
					ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
					appRecord.setSecondCategoryName(categories == null ? null : categories.getName());
					appRecord.setNamespaceId(categories == null ? null : categories.getNamespaceId());
					appRecord.setServiceAllianceId(request.getServiceAllianceId());
					appRecord.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
					Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
					if (org != null) {
						appRecord.setCreatorOrganization(org.getName());
					}
					ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(),
							request.getOwnerType(), request.getOwnerId());
					if (sa != null) {
						appRecord.setServiceOrganization(sa.getName());
					}
					saapplicationRecordProvider.createServiceAllianceApplicationRecord(appRecord);
				}
			}
			if (locator.getAnchor() == null) {
				break;
			}
		}
	}

	private void generateApplicationRecordsFromSettleRequestInfoSearcherDb() {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		for (;;) {
			List<SettleRequests> requests = yellowPageProvider.listSettleRequests(locator, 20);
			if (requests.size() > 0) {
				for (SettleRequests request : requests) {
					ServiceAllianceApplicationRecord appRecord = ConvertHelper.convert(request,
							ServiceAllianceApplicationRecord.class);
					appRecord.setJumpType(JumpType.TEMPLATE.getCode());
					appRecord.setTemplateType(CustomRequestConstants.SETTLE_REQUEST_CUSTOM);
					appRecord.setCreateTime(request.getCreateTime());
					appRecord.setCreateDate(request.getCreateTime());
					appRecord.setCreatorUid(request.getCreatorUid());
					appRecord.setFlowCaseId(null);
					appRecord.setSecondCategoryId(request.getCategoryId());
					ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
					appRecord.setSecondCategoryName(categories == null ? null : categories.getName());
					appRecord.setNamespaceId(categories == null ? null : categories.getNamespaceId());
					appRecord.setServiceAllianceId(request.getServiceAllianceId());
					appRecord.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
					Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
					if (org != null) {
						appRecord.setCreatorOrganization(org.getName());
					}
					ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(),
							request.getOwnerType(), request.getOwnerId());
					if (sa != null) {
						appRecord.setServiceOrganization(sa.getName());
					}
					saapplicationRecordProvider.createServiceAllianceApplicationRecord(appRecord);
				}
			}
			if (locator.getAnchor() == null) {
				break;
			}
		}
	}

	private void generateApplicationRecordsFromServiceAlliance() {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		for (;;) {
			List<ServiceAllianceRequests> requests = yellowPageProvider.listServiceAllianceRequests(locator, 20);
			if (requests.size() > 0) {
				for (ServiceAllianceRequests request : requests) {
					ServiceAllianceApplicationRecord appRecord = new ServiceAllianceApplicationRecord();
					appRecord.setJumpType(JumpType.TEMPLATE.getCode());
					appRecord.setTemplateType(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM);
					appRecord.setType(request.getType());
					appRecord.setOwnerType(request.getOwnerType());
					appRecord.setOwnerId(request.getOwnerId());
					appRecord.setCreatorName(request.getCreatorName());
					appRecord.setCreatorOrganizationId(request.getCreatorOrganizationId());
					appRecord.setCreatorMobile(request.getCreatorMobile());
					appRecord.setCreateTime(request.getCreateTime());
					appRecord.setCreateDate(request.getCreateTime());
					appRecord.setCreatorUid(request.getCreatorUid());
					appRecord.setFlowCaseId(null);
					appRecord.setSecondCategoryId(request.getCategoryId());
					ServiceAllianceCategories categories = yellowPageProvider.findCategoryById(request.getCategoryId());
					appRecord.setSecondCategoryName(categories == null ? null : categories.getName());
					appRecord.setNamespaceId(categories == null ? null : categories.getNamespaceId());
					appRecord.setServiceAllianceId(request.getServiceAllianceId());
					appRecord.setWorkflowStatus(ServiceAllianceWorkFlowStatus.NONE.getCode());
					Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
					if (org != null) {
						appRecord.setCreatorOrganization(org.getName());
					}
					ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(),
							request.getOwnerType(), request.getOwnerId());
					if (sa != null) {
						appRecord.setServiceOrganization(sa.getName());
					}

					saapplicationRecordProvider.createServiceAllianceApplicationRecord(appRecord);
				}
			}
			if (locator.getAnchor() == null) {
				break;
			}
		}
	}

	@Override
	public SearchRequestInfoResponse listSeviceAllianceAppRecordsByEnterpriseId(Long enterpriseId, Long pageAnchor,
			Integer pageSize) {
		if (pageAnchor == null) {
			pageAnchor = 0L;
		}
		pageSize = PaginationConfigHelper.getPageSize(configurationProvider, pageSize);
		List<ServiceAllianceApplicationRecord> lists = saapplicationRecordProvider
				.listServiceAllianceApplicationRecordByEnterpriseId(enterpriseId, pageAnchor, pageSize + 1);
		if (null == lists)
			return null;
		Long nextPageAnchor = null;
		if (lists != null && lists.size() > pageSize) {
			lists.remove(lists.size() - 1);
			nextPageAnchor = lists.get(lists.size() - 1).getId();
		}
		final Map<String, Long> categoryAppIdMap = new HashMap<>();
		SearchRequestInfoResponse response = new SearchRequestInfoResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setDtos(lists.stream().map((r) -> {
			RequestInfoDTO requestInfo = ConvertHelper.convert(r, RequestInfoDTO.class);
			if (requestInfo != null && r.getCreateTime() != null) {
				requestInfo.setCreateTime(r.getCreateTime().toLocalDateTime().format(dateSF));
				requestInfo.setAppId(getAppIdByCategory(r.getType(), categoryAppIdMap));
			}
			return requestInfo;
		}).collect(Collectors.toList()));
		return response;
	}

	private Long getAppIdByCategory(Long type, Map<String, Long> categoryAppIdMap) {
		if (categoryAppIdMap.get(String.valueOf(type)) == null) {
			int ns = UserContext.getCurrentNamespaceId();
			PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(ns);
			if (releaseVersion == null) {
				return null;
			}
			List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(ns,
					releaseVersion.getId(), 40500L, (byte) 33, String.valueOf(type), null);
			if (serviceModuleApps == null || serviceModuleApps.size() == 0) {
				return null;
			}
			categoryAppIdMap.put(String.valueOf(type), serviceModuleApps.get(0).getId());
		}
		return categoryAppIdMap.get(String.valueOf(type));
	}

	@Override
	/*
	 * 获取服务商信息
	 */
	public ListServiceAllianceProvidersResponse listServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd) {

		if (null != cmd.getAppId()) {
			checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());
		}

		if (cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.current().getNamespaceId());
		}

		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getAnchor());
		// 进行查询
		List<ServiceAllianceProviderDTO> dtos = listServiceAllianceProviders(cmd, locator);

		// 组装返回参数
		ListServiceAllianceProvidersResponse rsp = new ListServiceAllianceProvidersResponse();
		rsp.setProviders(dtos);
		rsp.setNextPageAnchor(locator.getAnchor());
		return rsp;
	}

	@Override
	public void exportServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd, HttpServletResponse response) {

		// 校验权限
		checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());

		// 获取记录
		List<ServiceAllianceProvid> providerList = allianceProvidProvider.listServiceAllianceProviders(
				new ListingLocator(), cmd.getPageSize(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getType(), cmd.getCategoryId(), cmd.getKeyword());
		// 判空
		if (CollectionUtils.isEmpty(providerList)) {
			return;
		}

		// 转换成DTO
		List<ServiceAllianceProviderDTO> dtos = new ArrayList<>(providerList.size());
		providerList.forEach(r -> {
			ServiceAllianceProviderDTO dto = ConvertHelper.convert(r, ServiceAllianceProviderDTO.class);

			// 设置服务商名称
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(r.getCategoryId());
			dto.setCategoryName(category == null ? null : category.getName());

			BigDecimal totalScore = new BigDecimal(r.getTotalScore() + "");
			BigDecimal scoreTimes = new BigDecimal(r.getScoreTimes() + "");

			// 设置评分
			if (0 == r.getScoreTimes()) {
				dto.setScore(0.0);
			} else {
				dto.setScore(totalScore.divide(scoreTimes, 1, RoundingMode.DOWN).doubleValue());
			}

			dtos.add(dto);
		});

		// 导出excel
		createProvidersExcel(dtos, response);
	}

	/**
	 * 创建excel
	 * 
	 * @param cmd
	 * @return
	 */
	private void createProvidersExcel(List<ServiceAllianceProviderDTO> dtos, HttpServletResponse response) {

		String fileName = "服务商列表";
		ExcelUtils excelUtils = new ExcelUtils(response, fileName, "服务商列表");
		List<String> propertyNames = new ArrayList<String>(
				Arrays.asList("id", "name", "categoryName", "mail", "contactName", "contactNumber", "score"));
		List<String> titleNames = new ArrayList<String>(
				Arrays.asList("序号", "服务商名称", "服务类型", "企业邮箱", "联系人", "联系电话", "评分"));
		List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 32, 32, 32, 16, 16, 10));

		for (int i = 0; i < dtos.size(); i++) {
			dtos.get(i).setId((long) (i + 1));
		}

		excelUtils.setNeedSequenceColumn(false);
		excelUtils.writeExcel(propertyNames, titleNames, titleSizes, dtos);
	}

	/*
	 * 获取服务商信息实际逻辑
	 * 
	 */
	private List<ServiceAllianceProviderDTO> listServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd,
			ListingLocator locator) {

		if (TrueOrFalseFlag.TRUE.getCode().equals(cmd.getIsByFlow())) {
			// 1.获取工作流

			// 必须要有工作流信息，且
			FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
			if (null == flowCase || !FlowOwnerType.GENERAL_APPROVAL.getCode().equals(flowCase.getOwnerType())) {
				return null;
			}

			// 获取aprroval
			GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(flowCase.getOwnerId());
			cmd.setType(approval.getModuleId());
		}

		// 获取记录
		List<ServiceAllianceProvid> providerList = allianceProvidProvider.listServiceAllianceProviders(locator,
				cmd.getPageSize(), cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getType(),
				cmd.getCategoryId(), cmd.getKeyword());
		// 判空
		if (CollectionUtils.isEmpty(providerList)) {
			return null;
		}

		// 转换成DTO
		List<ServiceAllianceProviderDTO> dtos = new ArrayList<>(providerList.size());
		providerList.forEach(r -> {
			ServiceAllianceProviderDTO dto = ConvertHelper.convert(r, ServiceAllianceProviderDTO.class);

			// 设置服务商名称
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(r.getCategoryId());
			dto.setCategoryName(category == null ? null : category.getName());

			BigDecimal totalScore = new BigDecimal(r.getTotalScore() + "");
			BigDecimal scoreTimes = new BigDecimal(r.getScoreTimes() + "");

			// 设置评分
			if (0 == r.getScoreTimes()) {
				dto.setScore(0.0);
			} else {
				dto.setScore(totalScore.divide(scoreTimes, 1, RoundingMode.DOWN).doubleValue());
			}

			dtos.add(dto);
		});

		return dtos;
	}

	@Override
	public void addServiceAllianceProvider(AddServiceAllianceProviderCommand cmd) {

		// 校验权限
		checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getOwnerId());

		// 获取参数
		ServiceAllianceProvid provider = ConvertHelper.convert(cmd, ServiceAllianceProvid.class);
		provider.setNamespaceId(
				cmd.getNamespaceId() == null ? UserContext.current().getNamespaceId() : cmd.getNamespaceId());
		allianceProvidProvider.createServiceAllianceProvid(provider);

		return;
	}

	@Override
	public void deleteServiceAllianceProvider(DeleteServiceAllianceProviderCommand cmd) {

		// 获取该记录
		ServiceAllianceProvid provider = allianceProvidProvider.findServiceAllianceProvidById(cmd.getId());
		if (provider == null) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_PROVIDER_NOT_FOUND, "alliance provider not exist");
		}

		// 校验权限
		checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), provider.getOwnerId());

		// 删除
		allianceProvidProvider.deleteServiceAllianceProvid(provider);

		return;

	}

	@Override
	public void updateServiceAllianceProvider(UpdateServiceAllianceProviderCommand cmd) {

		// 获取该记录
		ServiceAllianceProvid provider = allianceProvidProvider.findServiceAllianceProvidById(cmd.getId());
		if (provider == null) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_PROVIDER_NOT_FOUND, "alliance provider not exist");
			;
		}

		// 校验权限
		checkPrivilege(PrivilegeType.SERVICE_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), provider.getOwnerId());

		// 检查合法性

		// 参数都不能为空

		// 参数赋值
		provider = ConvertHelper.convert(cmd, ServiceAllianceProvid.class);
		provider.setCategoryId(cmd.getCategoryId());
		provider.setContactName(cmd.getContactName());
		provider.setContactNumber(cmd.getContactNumber());
		provider.setName(cmd.getName());
		provider.setMail(cmd.getMail());

		// 更新
		allianceProvidProvider.updateServiceAllianceProvid(provider);

		return;
	}

	@Override
	/*
	 * 工作流中新建事件
	 */
	public void applyExtraAllianceEvent(ApplyExtraAllianceEventCommand cmd) {

		// 查看模块是否开启服务商功能开关
		if (!IsOpenAllianceProviderFunction(cmd.getProviderId())) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_PROVIDER_FUNC_NOT_OPEN,
					"alliance provider function not open");
		}

		// 获取工作流信息
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
		if (null == flowCase) {
			throwError(YellowPageServiceErrorCode.ERROR_NEW_EVENT_FLOW_CASE_NOT_EXIST, "flow case not found");
		}

		// 进行事件保存以及工作流处理
		AllianceExtraEvent event = ConvertHelper.convert(cmd, AllianceExtraEvent.class);
		List<ExtraEventAttachmentDTO> uploads = cmd.getUploads();

		dbProvider.execute(status -> {

			event.setTime(new Timestamp(cmd.getTimeStamp()));
			allianceExtraEventsProvider.createAllianceExtraEvent(event);
			// 保存附件
			if (!CollectionUtils.isEmpty(uploads)) {
				for (ExtraEventAttachmentDTO dto : uploads) {
					if (StringUtils.isEmpty(dto.getFileUri())) {
						throwError(YellowPageServiceErrorCode.ERROR_NEW_EVENT_FILE_NOT_VALID, "file uri not valid");
					}

					AllianceExtraEventAttachment attch = new AllianceExtraEventAttachment();
					attch.setFileType(StringUtils.isEmpty(dto.getFileType()) ? "" : dto.getFileType());
					attch.setFileUri(dto.getFileUri());
					attch.setFileName(StringUtils.isEmpty(dto.getFileName()) ? System.currentTimeMillis() + ""
							: dto.getFileName());
					attch.setFileSize(dto.getFileSize());
					attch.setOwnerId(event.getId());
					allianceEventAttachmentProvider.createAllianceExtraEventAttach(attch);
				}
			}

			// 添加工作流日志
			FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
			buildExtraAllianceStepDTO(stepDTO, flowCase, event);
			flowService.processAutoStep(stepDTO);

			return null;
		});

		// 发送邮件给服务商
		sendNotifyToProviders(event, uploads);
	}

	/**
	 * 给服务商发提醒邮件
	 * 
	 * @param event
	 */
	private void sendNotifyToProviders(AllianceExtraEvent event, List<ExtraEventAttachmentDTO> uploads) {

		// 如果未传，或者开关未打开，直接返回
		if (null == event.getProviderId() || 0 == event.getEnableNotifyByEmail()) {
			return;
		}

		// 获取服务商信息
		ServiceAllianceProvid provider = allianceProvidProvider.findServiceAllianceProvidById(event.getProviderId());
		if (null == provider) {
			return;
		}

		// 获取服务类型
		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(provider.getCategoryId());
		if (null == category) {
			return;
		}

		// 生成body
		String body = buildProvidersNotifyEmailBody(provider, category, event);

		// 生成attachments
		List<File> files = null;
		;
		try {
			files = buildProvidersNotifyEmailAttachments(uploads);
		} catch (IOException e) {
			LOGGER.error("mail files save error:" + e);
			throwError(YellowPageServiceErrorCode.ERROR_MAIL_FILES_SAVE_ERROR, "mail files save error");
		}

		// 发送邮件
		YellowPageUtils.sendSingleMail(provider.getNamespaceId(), provider.getMail(), event.getTopic(), body, files);

	}

	/**
	 * 创建邮件body
	 * 
	 * @param provider
	 * @param event
	 * @return
	 */
	private String buildProvidersNotifyEmailBody(ServiceAllianceProvid provider, ServiceAllianceCategories category,
			AllianceExtraEvent event) {
		String endLine = "\r\n";
		/*
		 * $服务商公司名称$：
		 * 
		 * 你好，现有一条关于$服务名称$的服务跟踪事件，详情如下：
		 * 
		 * $事件主题事件主题事件主题事件主题事件主题事件主题事件主题$
		 * 
		 * 事件时间：2017-10-23 14:00
		 * 
		 * 事件地点：金融基地2栋7F2
		 * 
		 * 参与人员：小明、晓兰
		 * 
		 * 事件详情： xxxxxxxxxxxxxxxxxxxxxxxx
		 * 
		 * 创建时间：2017-10-24 13:00 创建人：小明（xxxxxxxxxxx）
		 */

		StringBuilder body = new StringBuilder();
		body.append(provider.getName() + "：");
		body.append(endLine);

		body.append("你好，现有一条关于").append(category.getName()).append("的服务跟踪事件，详情如下：");
		body.append(endLine);

		body.append(event.getTopic());
		body.append(endLine);

		body.append("事件时间：").append(Utils.longToString(event.getTime().getTime(), DateStyle.DATE_HOUR_MINUTE));
		body.append(endLine);

		body.append("事件地点：").append(event.getAddress());
		body.append(endLine);

		body.append("参与人员：").append(event.getMembers());
		body.append(endLine);

		body.append("事件详情：");
		body.append(endLine);

		body.append(event.getContent());
		body.append(endLine);

		body.append("创建时间：").append(Utils.longToString(event.getCreateTime().getTime(), DateStyle.DATE_HOUR_MINUTE));
		body.append(" ");

		TargetDTO userDto = organizationProvider.findUserContactByUserId( UserContext.getCurrentNamespaceId(), event.getCreateUid());
		String userName = userDto == null ? null : userDto.getTargetName();
		String userPhone = userDto == null ? null : userDto.getUserIdentifier();
		body.append("创建人：").append(userName).append("（").append(userPhone).append("）");
		body.append(endLine);

		return body.toString();
	}

	/**
	 * 创建邮件附件
	 * 
	 * @param provider
	 * @param event
	 * @return
	 * @throws IOException
	 */
	private List<File> buildProvidersNotifyEmailAttachments(List<ExtraEventAttachmentDTO> uploads) throws IOException {

		if (CollectionUtils.isEmpty(uploads)) {
			return null;
		}

		List<File> files = new ArrayList<>();

		for (ExtraEventAttachmentDTO upload : uploads) {

			// 获取实际url
			String fileUrl = StringUtils.isEmpty(upload.getFileUrl())
					? contentServerService.parserUri(upload.getFileUri()) : upload.getFileUrl();

			File file = FileUtils.getFileFormUrl(fileUrl, upload.getFileName(), null);
			if (null == file) {
				continue;
			}

			files.add(file);
		}

		return files;
	}

	@Override
	public GetExtraAllianceEventResponse getExtraAllianceEvent(GetExtraAllianceEventCommand cmd) {

		// 获取新建事件
		ExtraAllianceEventDTO dto = getExtraAllianceEvent(cmd.getEventId());

		// 组装返回数据
		GetExtraAllianceEventResponse rsp = new GetExtraAllianceEventResponse();
		rsp.setEvent(dto);

		return rsp;
	}

	/**
	 * 根据id获取新建事件
	 * 
	 * @param eventId
	 * @return
	 */
	private ExtraAllianceEventDTO getExtraAllianceEvent(Long eventId) {

		// 获取事件
		AllianceExtraEvent event = allianceExtraEventsProvider.findAllianceExtraEventById(eventId);
		if (null == event) {
			return null;
		}

		List<ExtraEventAttachmentDTO> uploads = new ArrayList<>(5);
		// 根据时间id获取附件列表
		List<AllianceExtraEventAttachment> attachs = allianceEventAttachmentProvider
				.listAttachmentsByEventId(event.getId());
		if (!CollectionUtils.isEmpty(attachs)) {
			attachs.forEach(r -> {
				ExtraEventAttachmentDTO dto = new ExtraEventAttachmentDTO();
				dto.setFileType(r.getFileType());
				dto.setFileUrl(contentServerService.parserUri(r.getFileUri()));
				dto.setFileName(r.getFileName());
				dto.setFileSize(r.getFileSize());
				uploads.add(dto);
			});
		}

		// 转化成dto
		ExtraAllianceEventDTO eventDto = ConvertHelper.convert(event, ExtraAllianceEventDTO.class);

		// 设置不一致的参数
		// 时间，附件，创建时间，人名，电话
		eventDto.setTimeStamp(event.getTime().getTime());
		eventDto.setUploads(uploads);
		eventDto.setCreateTimeStamp(event.getCreateTime().getTime());
		TargetDTO userDto = organizationProvider.findUserContactByUserId( UserContext.getCurrentNamespaceId(), event.getCreateUid());
		if (null != userDto) {
			eventDto.setCreateUserName(userDto.getTargetName());
			eventDto.setCreateUserToken(userDto.getUserIdentifier());
		}

		return eventDto;
	}


	/**
	 * 改变工作流的执行行为
	 * 
	 * @param stepDTO
	 * @param flowCase
	 * @param event
	 */
	private void buildExtraAllianceStepDTO(FlowAutoStepDTO stepDTO, FlowCase flowCase, AllianceExtraEvent event) {

		// 设置工作流基本信息
		buildExtraEventStepBasic(stepDTO, flowCase);

		// 新增跟踪日志
		buildExtraEventStepLog(stepDTO, flowCase, event);

		// 新增评价项
		buildExtraEventStepEvaluation(stepDTO, flowCase, event);
	}

	/**
	 * 设置工作流基本信息
	 * 
	 * @param stepDTO
	 * @param flowCase
	 */
	private void buildExtraEventStepBasic(FlowAutoStepDTO stepDTO, FlowCase flowCase) {
		stepDTO.setFlowCaseId(flowCase.getId());
		stepDTO.setFlowMainId(flowCase.getFlowMainId());
		stepDTO.setFlowVersion(flowCase.getFlowVersion());
		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
		stepDTO.setAutoStepType(FlowStepType.NO_STEP.getCode()); // 如果不想进行下一步就使用no_step
		stepDTO.setStepCount(flowCase.getStepCount());
	}

	/**
	 * 生成新建事件跟踪日志
	 * 
	 * @param flowCase
	 * @param userId
	 * @param userName
	 * @param topic
	 * @param enableRead
	 *            1-允许申请人看到该日志 0-不允许申请人看到该日志
	 * @return
	 */
	private void buildExtraEventStepLog(FlowAutoStepDTO stepDTO, FlowCase flowCase, AllianceExtraEvent event) {

		FlowEventLog log = new FlowEventLog();

		// 设置默认信息
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(flowCase.getFlowMainId());
		log.setFlowVersion(flowCase.getFlowVersion());
		log.setNamespaceId(flowCase.getNamespaceId());
		log.setFlowNodeId(flowCase.getCurrentNodeId());
		log.setFlowCaseId(flowCase.getId());
		log.setStepCount(flowCase.getStepCount());
		log.setLogType(FlowLogType.NODE_TRACKER.getCode());
		log.setButtonFiredStep(FlowStepType.NO_STEP.getCode());
		log.setSubjectId(0L);
		log.setParentId(0L);

		// 设置业务信息
		// 保存提交人信息
		User user = UserContext.current().getUser();
		if (null == user) {
			throwError(YellowPageServiceErrorCode.ERROR_NEW_EVENT_APPLIER_NOT_EXIST, "applier not exist");
		}
		log.setFlowUserId(user.getId());
		log.setFlowUserName(user.getNickName());
		log.setTrackerApplier(null == event.getEnableRead() ? 0L : event.getEnableRead().longValue()); // 申请人可以看到此条log，为0则看不到
		log.setTrackerProcessor(1L);// 处理人可以看到此条log，为0则看不到
		log.setLogContent(event.getTopic());
		log.setExtra(buildExtraAllianceEventUrlInfo(event)); // app端点击跳转链接

		// 保存到 stepDTO 中
		List<FlowEventLog> eventLogs = new ArrayList<>(1);
		eventLogs.add(log);
		stepDTO.setEventLogs(eventLogs);
	}

	/**
	 * 添加服务评价项
	 * 
	 * @return
	 */
	private void buildExtraEventStepEvaluation(FlowAutoStepDTO stepDTO, FlowCase flowCase, AllianceExtraEvent event) {

		// 判断该评价是否已存在，存在就不需要继续加了
		if (null == event.getProviderId() || IsCurrentEvaluationExist(event.getProviderId(), flowCase.getId())) {
			return;
		}

		FlowEvaluateItem item = new FlowEvaluateItem();
		item.setFlowCaseId(flowCase.getId());
		item.setFlowMainId(flowCase.getFlowMainId());
		item.setFlowVersion(flowCase.getFlowVersion());
		item.setNamespaceId(flowCase.getNamespaceId());
		item.setName(event.getProviderName());// 评价项名称
		item.setIntegralTag6(event.getProviderId()); // 这里把provider id
														// 放到IntegralTag6预留字段
		item.setStringTag6(Tables.EH_SERVICE_ALLIANCE_PROVIDERS.getClass().getSimpleName()); // events表字符串存起来
		item.setInputFlag(TrueOrFalseFlag.TRUE.getCode()); // 是否需要输入评价内容
		flowEvaluateItemProvider.createFlowEvaluateItem(item);
	}

	/**
	 * 组装每条新建日志的跳转地址
	 * 
	 * @param event
	 * @return
	 */
	private String buildExtraAllianceEventUrlInfo(AllianceExtraEvent event) {
		JSONObject json = new JSONObject();
		json.put("route", "zl://yellow-page/eventDetail?eventId=" + event.getId());
		json.put("eventId", event.getId());
		return json.toJSONString();
	}

	/**
	 * 当前是否开启了服务商功能
	 * 
	 * @return
	 */
	private boolean IsOpenAllianceProviderFunction(Long providerId) {
		if (null == providerId || providerId <= 0) {
			return true;
		}

		// 获取该服务商
		ServiceAllianceProvid provider = allianceProvidProvider.findServiceAllianceProvidById(providerId);
		if (null == provider) {
			return false;
		}

		// 获取type的配置
		ServiceAllianceCategories allianceConfig = allianceStandardService.queryHomePageCategoryByScene(provider.getType(), null);
		if (null == allianceConfig) {
			return false;
		}

		// 如果该值未配置，或配置为0，表示关闭
		if (null == allianceConfig.getEnableProvider() || (byte) 0 == allianceConfig.getEnableProvider()) {
			return false;
		}

		return true;
	}

	/**
	 * 查询传入的评价项名称是否存在。 评价项即服务商的名称
	 * 
	 * @param evaluationName
	 * @param flowCaseId
	 * @return
	 */
	private boolean IsCurrentEvaluationExist(Long providerId, Long flowCaseId) {

		List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowCase(flowCaseId);
		if (CollectionUtils.isEmpty(items)) {
			return false;
		}

		String className = Tables.EH_SERVICE_ALLIANCE_PROVIDERS.getClass().getSimpleName();

		for (FlowEvaluateItem item : items) {
			if (providerId.equals(item.getIntegralTag6()) && className.equals(item.getStringTag6())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 校验当前请求是否符合权限
	 * 
	 * @param privilegeType
	 * @param currentPMId
	 * @param appId
	 * @param checkOrgId
	 * @param checkCommunityId
	 */
	public void checkPrivilege(PrivilegeType privilegeType, Long currentPMId, Long appId, Long checkCommunityId) {
		if (configProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), currentPMId,
					privilegeType.getCode(), appId, null, checkCommunityId);
		}
	}

	/**
	 * @Function: YellowPageServiceImpl.java
	 * @Description: 将服务联盟的数据更新为community 主要涉及到service_alliance表
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年6月1日 下午2:19:10
	 *
	 */
//	@Override
	private String transferServiceToCommunity() {

		StringBuilder failed = new StringBuilder();
		StringBuilder returnString = new StringBuilder();

		// 进行数据迁移
		dbProvider.execute(r -> {

			int ignoreCnt = 0;
			int rangeNotAll = 0;
			int rangeIsAll = 0;

			String fixedOwnerType = ServiceAllianceBelongType.COMMUNITY.getCode();

			// 读取所有记录
			List<ServiceAlliances> saList = queryAllServiceAlliances(null);
			if (CollectionUtils.isEmpty(saList)) {
				return null;
			}

			for (ServiceAlliances sa : saList) {
				if (ServiceAllianceBelongType.COMMUNITY.getCode().equals(sa.getOwnerType()) || null == sa.getParentId()
						|| 0 == sa.getParentId()) {
					// 如果已经是community，或者parentId为0，不做修改
					ignoreCnt++;
					continue;
				}

				String range = sa.getRange();
				Long ownerId = null;
				if (!StringUtils.isEmpty(range) && !StringUtils.isEmpty(range = range.trim()) && !"all".equals(range)) {
					// 查看range，如果有范围，取第一个communityId
					String[] idStrArray = range.split(",");
					ownerId = Long.parseLong(idStrArray[0]);
					rangeNotAll++;
				} else {
					// 如果range为all或者为空（异常情况），则根据公司id去获取community
					OrganizationCommunityRequest rq = organizationProvider
							.getOrganizationCommunityRequestByOrganizationId(sa.getOwnerId());
					if (null == rq) {
						failed.append(sa.getId() + ",");
						continue;
					}

					ownerId = rq.getCommunityId();
					rangeIsAll++;
				}

				// 更新该记录
				sa.setOwnerType(fixedOwnerType);
				sa.setOwnerId(ownerId);
				yellowPageProvider.updateServiceAlliances(sa);
			}

			// 获取一些重要信息
			returnString.append("|ignore:" + ignoreCnt);
			returnString.append("|notAll:" + rangeNotAll);
			returnString.append("|All:" + rangeIsAll);
			returnString.append("|total:" + saList.size());

			return null;

		});

		if (failed.length() > 0) {
			int getLen = failed.length() > 1000 ? failed.length() : failed.length();
			returnString.append("|failed:" + failed.toString().substring(0, getLen));
		}

		return returnString.toString();
	}

	/**
	 * @Function: YellowPageServiceImpl.java
	 * @Description: 迁移数据用，获取所有的Alliance数据
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年6月1日 下午5:16:44
	 *
	 */
	private List<ServiceAlliances> queryAllServiceAlliances(Long parentId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhServiceAlliancesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCES);
		if (null != parentId) {
			query.addConditions(Tables.EH_SERVICE_ALLIANCES.PARENT_ID.eq(parentId));
		}
		return query.fetchInto(ServiceAlliances.class);
	}


	@Override
	public ListOnlineServicesResponse listOnlineServices(ListOnlineServicesCommand cmd) {

		List<TargetDTO> onlines = allianceOnlineService.listOnlineServiceByServiceAllianceId(cmd.getTargetId());

		// 封装成返回数据
		ListOnlineServicesResponse resp = new ListOnlineServicesResponse();
		resp.setOnlineServices(onlines);
		return resp;
	}

	@Override
	public void updateAllianceTag(UpdateAllianceTagCommand cmd) {
		
		AllianceTagGroupDTO group = cmd.getTagGroup();
		if (null == group || null == group.getParentTag()) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_TAG_NOT_VALID, "alliance parent tag is null");
			return;
		}

		if (null == cmd.getType() || cmd.getType() <= 0) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_TAG_TYPE_NOT_VALID, "alliance tag type not valid");
			return;
		}

		Integer namespaceId = null == cmd.getNamespaceId() ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		updateAllianceTag(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),cmd.getType(), group.getParentTag(), group.getChildTags()); // 否则是更新
	}

	/**
	 * 根据传来的头tag节点进行创建或更新
	 * 
	 * @param headTag
	 */
	@Override
	public void updateAllianceTag(Integer namespaceId, String ownerType, Long ownerId, Long type, AllianceTagDTO parentTagDto, List<AllianceTagDTO> childTagDtos) {
		
		AllianceTag headTag = ConvertHelper.convert(parentTagDto, AllianceTag.class);
		headTag.setNamespaceId(namespaceId);
		headTag.setType(type);
		headTag.setOwnerType(ownerType);
		headTag.setOwnerId(ownerId);

		dbProvider.execute(r -> {

			if (null == headTag.getId()) {
				allianceTagProvider.createAllianceTag(headTag);
			} else {
				allianceTagProvider.updateAllianceTag(headTag);
			}

			if (CollectionUtils.isEmpty(childTagDtos)) {
				return null;
			}

			byte index = 0;
			for (AllianceTagDTO childDto : childTagDtos) {
				AllianceTag childTag = ConvertHelper.convert(childDto, AllianceTag.class);
				childTag.setOwnerType(ownerType);
				childTag.setOwnerId(ownerId);
				childTag.setNamespaceId(namespaceId);
				childTag.setType(type);
				childTag.setParentId(headTag.getId());
				childTag.setDefaultOrder(index++);
				if (null == childTag.getId()) {
					allianceTagProvider.createAllianceTag(childTag);
				} else {
					allianceTagProvider.updateAllianceTag(childTag);
				}
			}
			
			return null;
		});
	}
	

	@Override
	public GetAllianceTagResponse getAllianceTagList(GetAllianceTagCommand cmd) {

		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		List<AllianceTagGroupDTO> groups = getAllianceTagList(locator, cmd.getPageSize(), namespaceId,
				cmd.getOwnerType(), cmd.getOwnerId(), cmd.getType());

		// 组装返回数据
		GetAllianceTagResponse resp = new GetAllianceTagResponse();
		resp.setPageAnchor(locator.getAnchor());
		resp.setGroups(groups);
		return resp;
	}

	@Override
	public List<AllianceTagGroupDTO> getAllianceTagList(ListingLocator locator, Integer pageSize, Integer namespaceId,
			String ownerType, Long ownerId, Long type) {

		List<AllianceTag> parentTags = allianceTagProvider.getAllianceParentTagList(locator, pageSize, namespaceId,
				ownerType, ownerId, type);
		if (CollectionUtils.isEmpty(parentTags)) {
			return  new ArrayList<>(10);
		}

		List<AllianceTagGroupDTO> groups = new ArrayList<>(10);
		for (AllianceTag parentTag : parentTags) {

			// 保存父节点
			AllianceTagGroupDTO group = new AllianceTagGroupDTO();
			group.setParentTag(ConvertHelper.convert(parentTag, AllianceTagDTO.class));

			// 保存子节点
			List<AllianceTag> childTags = allianceTagProvider.getAllianceChildTagList(parentTag.getNamespaceId(),
					parentTag.getType(), parentTag.getId());
			if (!CollectionUtils.isEmpty(childTags)) {
				// 转换成返回参数
				List<AllianceTagDTO> childDtos = childTags.stream()
						.map(r -> ConvertHelper.convert(r, AllianceTagDTO.class)).collect(Collectors.toList());
				group.setChildTags(childDtos);
			}

			// 添加结果
			groups.add(group);
		}

		return groups;
	}

	
	/**
	 * 根据服务联盟的type找到对应的 displayType
	 * @param type
	 * @return
	 */
	private  String getModuleDisplayTypeByAllianceType(Long type) {

		if (null == type) {
			return null;
		}

		ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();

		cmd.setModuleId(SERVICE_ALLIANCE_MODULE_ID);
		cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		// cmd.set

		// 获取当前所有module
		ListServiceModuleAppsResponse resp = portalService.listServiceModuleApps(cmd);
		if (null == resp) {
			return null;
		}

		// 比较出type
		List<ServiceModuleAppDTO> apps = resp.getServiceModuleApps();
		if (CollectionUtils.isEmpty(apps)) {
			return null;
		}

		// 返回displayType
		for (ServiceModuleAppDTO app : apps) {
			String instanceConfig = app.getInstanceConfig();
			ServiceAllianceInstanceConfig config = (ServiceAllianceInstanceConfig) StringHelper
					.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
			if (type.equals(config.getType())) {
				return config.getDisplayType();
			}
		}

		return null;
	}
	
	@Override
	public String transferPosterUriToAttachment() {
		// 读取所有记录
		List<ServiceAlliances> saList = queryAllServiceAlliances(null);
		if (CollectionUtils.isEmpty(saList)) {
			return null;
		}

		JSONObject json = new JSONObject();
		json.put("working", "working");
		
		//查看是否迁移过了。
		if (isAttachmentsTransfered()) {
			json.put("isTransfered", "true");
			return json.toJSONString();
		}
		
		dbProvider.execute(status -> {

			int emptyCnt = 0;
			int addCnt = 0;

			// 循环所有的serviceAlliance
			for (ServiceAlliances sa : saList) {
				if (StringUtils.isEmpty(sa.getPosterUri())) {
					emptyCnt++;
					continue;
				}

				addCnt++;

				// 只要有posterUri就插入到attachments中去
				ServiceAllianceAttachment attachment = new ServiceAllianceAttachment();
				attachment.setOwnerId(sa.getId());
				attachment.setContentUri(sa.getPosterUri());
				attachment.setCreatorUid(2L); // 用户id为2表示迁移图片
				attachment.setAttachmentType(ServiceAllianceAttachmentType.COVER_ATTACHMENT.getCode());
				yellowPageProvider.createServiceAllianceAttachments(attachment);
			}

			json.put("emptyCnt", emptyCnt);
			json.put("addCnt", addCnt);
			return null;
		});

		return json.toJSONString();
	}
	
	private boolean isAttachmentsTransfered() {
		
		com.everhomes.server.schema.tables.EhServiceAllianceAttachments table = Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS;
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceAllianceAttachmentsRecord> query = context.selectQuery(table);
		query.addConditions(table.CREATOR_UID.eq(2L));
		List<ServiceAllianceAttachment> list = query.fetchInto(ServiceAllianceAttachment.class);
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String transferLaunchPadItems() {
		List<LaunchPadItem> items = queryAlliancePadItems();
		if (CollectionUtils.isEmpty(items)) {
			return null;
		}
		
		JSONObject json = new JSONObject();
		json.put("working", "working");
		List<Long> empty = new ArrayList<Long>(100);
		List<Long> ignore = new ArrayList<Long>(100);
		Map<Long, String> failed = new HashMap<Long, String>(100);
		int success = 0;
		int total = 0;
		
		
		for (LaunchPadItem item : items) {
			
			total++;
			
			//对现网的标签页做忽略
			if("/secondhome".equals(item.getItemLocation()) && "Gallery".equals(item.getItemGroup())) {
				ignore.add(item.getId());
				continue;
			}
			
			String actionData = item.getActionData();
			if (StringUtils.isEmpty(actionData)) {
				empty.add(item.getId());
				continue;
			}
			
			ServiceAllianceActionData config = JSONObject.parseObject(actionData, ServiceAllianceActionData.class);
			if (null == config) {
				failed.put(item.getId(), actionData);
				continue;
			}
			
			JSONObject tmpJson = new JSONObject();
			tmpJson.put("url", buildRenderUrl(item.getNamespaceId(), config));
			item.setActionData(tmpJson.toJSONString());
			item.setActionType((byte)14);
			launchPadProvider.updateLaunchPadItem(item);
			success++;
		}
		
		json.put("empty", empty.toString());
		json.put("ignore", ignore.toString());
		json.put("failed", failed.toString());
		json.put("success", success);
		json.put("total", total);
		return json.toJSONString();
	}
	
	private String buildRenderUrl(Integer namespaceId, ServiceAllianceActionData config) {

		// 服务联盟v3.4 web化之后，直接设置为跳转链接即可
		// http://dev15.zuolin.com/service-alliance-web/build/index.html#/home/filterlist?displayType=filterlist&parentId=213729&enableComment=1#sign_suffix
		StringBuilder url = new StringBuilder();
		url.append("${home.url}/service-alliance-web/build/index.html#/home/" + config.getDisplayType());
		url.append("?displayType=" + config.getDisplayType());
		url.append("&parentId=" + config.getType());
		url.append("&enableComment=" + ((null == config.getEnableComment()) ? 0 : config.getEnableComment()));
		url.append("#sign_suffix");

		return url.toString();
	}
	
	
	private List<LaunchPadItem> queryAlliancePadItems() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadItems.class));
		SelectConditionStep<Record> query = context.select().from(Tables.EH_LAUNCH_PAD_ITEMS)
				.where(Tables.EH_LAUNCH_PAD_ITEMS.ACTION_TYPE.eq((byte) 33));
		return query.fetchInto(LaunchPadItem.class);
	}
	
	@Override
	public String transferTime(Long parentId) {
		// 读取所有记录
		List<ServiceAlliances> saList = queryAllServiceAlliances(parentId);
		if (CollectionUtils.isEmpty(saList)) {
			return null;
		}

		JSONObject json = new JSONObject();
		json.put("working", "working");
		
		dbProvider.execute(status -> {

			int emptyCnt = 0;
			int toChangCnt = 0;

			// 循环所有的serviceAlliance
			for (ServiceAlliances sa : saList) {
				if (StringUtils.isEmpty(sa.getEndTime())) {
					emptyCnt++;
					continue;
				}

				toChangCnt++;
				sa.setDefaultOrder(getDateDefaultOrder(sa.getEndTime()));
				yellowPageProvider.updateServiceAlliances(sa);
			}

			json.put("emptyCnt", emptyCnt);
			json.put("toChangCnt", toChangCnt);
			return null;
		});

		return json.toJSONString();
	}
	
	private Long getDateDefaultOrder(Timestamp endTime) {
		long timeMillis = System.currentTimeMillis();
		long ms = timeMillis % 1000; // 获得毫秒
		long ms1 = ms / 100;
		long ms2 = (ms % 100) / 10;
		long ms3 = (ms % 10);
		String timeHeadStr = DateUtil.dateToStr(endTime, "yyyyMMdd");
		String timeMidStr = DateUtil.dateToStr(new Timestamp(timeMillis), "HHmmss");
		return -Long.parseLong(timeHeadStr + timeMidStr + ms1 + ms2 + ms3);

	}

	@Override
	public void updateServiceTypeOrders(UpdateServiceTypeOrdersCommand cmd) {

		List<Long> idList = Arrays.asList(cmd.getUpId(), cmd.getLowId());
		Map<Long, Long> ret = yellowPageProvider.getServiceTypeOrders(idList);
		if (ret.isEmpty() || ret.size() < 2) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_SERVICE_TYPE_TO_UPDATE_NOT_FOUND,
					"service type not found");
		}

		Long upIdCurrentOrder = ret.get(cmd.getUpId());
		Long lowIdCurrentOrder = ret.get(cmd.getLowId());
		// 如果本身已经符合更新后的顺序，有可能是重复点击，直接返回
		if (upIdCurrentOrder < lowIdCurrentOrder) {
			return;
		}

		dbProvider.execute(r -> {
			yellowPageProvider.updateServiceTypeOrders(cmd.getUpId(), lowIdCurrentOrder);
			yellowPageProvider.updateServiceTypeOrders(cmd.getLowId(), upIdCurrentOrder);
			return null;
		});
	}

	private void checkQueryOwnerId(String ownerType, Long ownerId) {
		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType) || null == ownerId || ownerId < 1) {
			throwError(YellowPageServiceErrorCode.ERROR_COMMUNITY_NOT_CHOSEN, "community not chosen");
		}
	}

}
