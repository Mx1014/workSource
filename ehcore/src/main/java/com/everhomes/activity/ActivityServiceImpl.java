// @formatter:off
package com.everhomes.activity;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.archives.ArchivesUtil;
import com.everhomes.asset.PaymentConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community_form.CommunityFormProvider;
import com.everhomes.community_form.CommunityFormType;
import com.everhomes.community_form.CommunityGeneralForm;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.forum.Attachment;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.InteractSetting;
import com.everhomes.forum.Post;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.order.OrderService;
import com.everhomes.order.OrderUtil;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.poll.ProcessStatus;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.activity.ActivityAttachmentDTO;
import com.everhomes.rest.activity.ActivityCancelSignupCommand;
import com.everhomes.rest.activity.ActivityCancelType;
import com.everhomes.rest.activity.ActivityCategoryDTO;
import com.everhomes.rest.activity.ActivityChargeFlag;
import com.everhomes.rest.activity.ActivityCheckinCommand;
import com.everhomes.rest.activity.ActivityConfirmCommand;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityFormIdCommand;
import com.everhomes.rest.activity.ActivityGoodsDTO;
import com.everhomes.rest.activity.ActivityListCommand;
import com.everhomes.rest.activity.ActivityListResponse;
import com.everhomes.rest.activity.ActivityLocalStringCode;
import com.everhomes.rest.activity.ActivityMemberDTO;
import com.everhomes.rest.activity.ActivityNotificationTemplateCode;
import com.everhomes.rest.activity.ActivityPayeeDTO;
import com.everhomes.rest.activity.ActivityPayeeStatusType;
import com.everhomes.rest.activity.ActivityPostCommand;
import com.everhomes.rest.activity.ActivityRejectCommand;
import com.everhomes.rest.activity.ActivityRosterPayFlag;
import com.everhomes.rest.activity.ActivityRosterPayVersionFlag;
import com.everhomes.rest.activity.ActivityRosterSourceFlag;
import com.everhomes.rest.activity.ActivityRosterStatus;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.ActivityShareDetailResponse;
import com.everhomes.rest.activity.ActivitySignupCommand;
import com.everhomes.rest.activity.ActivitySignupFlag;
import com.everhomes.rest.activity.ActivityTimeResponse;
import com.everhomes.rest.activity.ActivityTokenDTO;
import com.everhomes.rest.activity.ActivityVideoDTO;
import com.everhomes.rest.activity.ActivityVideoRoomType;
import com.everhomes.rest.activity.ActivityWarningResponse;
import com.everhomes.rest.activity.CheckPayeeIsUsefulCommand;
import com.everhomes.rest.activity.CheckPayeeIsUsefulResponse;
import com.everhomes.rest.activity.CreateActivityAttachmentCommand;
import com.everhomes.rest.activity.CreateActivityGoodsCommand;
import com.everhomes.rest.activity.CreateOrUpdateActivityPayeeCommand;
import com.everhomes.rest.activity.CreateSignupOrderCommand;
import com.everhomes.rest.activity.CreateSignupOrderV2Command;
import com.everhomes.rest.activity.CreateWechatJsSignupOrderCommand;
import com.everhomes.rest.activity.DeleteActivityAttachmentCommand;
import com.everhomes.rest.activity.DeleteActivityGoodsCommand;
import com.everhomes.rest.activity.DeleteSignupInfoCommand;
import com.everhomes.rest.activity.DownloadActivityAttachmentCommand;
import com.everhomes.rest.activity.ExportActivityCommand;
import com.everhomes.rest.activity.ExportActivitySignupDTO;
import com.everhomes.rest.activity.ExportActivitySignupTemplateCommand;
import com.everhomes.rest.activity.ExportOrganizationCommand;
import com.everhomes.rest.activity.ExportSignupInfoCommand;
import com.everhomes.rest.activity.ExportTagCommand;
import com.everhomes.rest.activity.GeoLocation;
import com.everhomes.rest.activity.GetActivityAchievementCommand;
import com.everhomes.rest.activity.GetActivityAchievementResponse;
import com.everhomes.rest.activity.GetActivityDetailByIdCommand;
import com.everhomes.rest.activity.GetActivityDetailByIdResponse;
import com.everhomes.rest.activity.GetActivityGoodsCommand;
import com.everhomes.rest.activity.GetActivityPayeeCommand;
import com.everhomes.rest.activity.GetActivityPayeeDTO;
import com.everhomes.rest.activity.GetActivityTimeCommand;
import com.everhomes.rest.activity.GetActivityVideoInfoCommand;
import com.everhomes.rest.activity.GetActivityWarningCommand;
import com.everhomes.rest.activity.GetRosterOrderSettingCommand;
import com.everhomes.rest.activity.GetVideoCapabilityCommand;
import com.everhomes.rest.activity.ImportSignupErrorDTO;
import com.everhomes.rest.activity.ImportSignupInfoCommand;
import com.everhomes.rest.activity.ImportSignupInfoResponse;
import com.everhomes.rest.activity.ListActivitiesByCategoryIdCommand;
import com.everhomes.rest.activity.ListActivitiesByCategoryIdResponse;
import com.everhomes.rest.activity.ListActivitiesByLocationCommand;
import com.everhomes.rest.activity.ListActivitiesByNamespaceIdAndTagCommand;
import com.everhomes.rest.activity.ListActivitiesByTagCommand;
import com.everhomes.rest.activity.ListActivitiesCommand;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.activity.ListActivityAttachmentsCommand;
import com.everhomes.rest.activity.ListActivityAttachmentsResponse;
import com.everhomes.rest.activity.ListActivityCategoriesCommand;
import com.everhomes.rest.activity.ListActivityEntryCategoriesCommand;
import com.everhomes.rest.activity.ListActivityGoodsCommand;
import com.everhomes.rest.activity.ListActivityGoodsResponse;
import com.everhomes.rest.activity.ListActivityPayeeCommand;
import com.everhomes.rest.activity.ListNearByActivitiesCommand;
import com.everhomes.rest.activity.ListNearByActivitiesCommandV2;
import com.everhomes.rest.activity.ListOfficialActivityByNamespaceCommand;
import com.everhomes.rest.activity.ListOfficialActivityByNamespaceResponse;
import com.everhomes.rest.activity.ListOrgNearbyActivitiesCommand;
import com.everhomes.rest.activity.ListSignupInfoByOrganizationIdResponse;
import com.everhomes.rest.activity.ListSignupInfoCommand;
import com.everhomes.rest.activity.ListSignupInfoResponse;
import com.everhomes.rest.activity.ManualSignupCommand;
import com.everhomes.rest.activity.RosterOrderSettingDTO;
import com.everhomes.rest.activity.SetActivityAchievementCommand;
import com.everhomes.rest.activity.SetActivityTimeCommand;
import com.everhomes.rest.activity.SetActivityVideoInfoCommand;
import com.everhomes.rest.activity.SetActivityWarningCommand;
import com.everhomes.rest.activity.SetRosterOrderSettingCommand;
import com.everhomes.rest.activity.SignupErrorHandleType;
import com.everhomes.rest.activity.SignupInfoDTO;
import com.everhomes.rest.activity.StatisticsActivityCommand;
import com.everhomes.rest.activity.StatisticsActivityDTO;
import com.everhomes.rest.activity.StatisticsActivityResponse;
import com.everhomes.rest.activity.StatisticsOrderByFlag;
import com.everhomes.rest.activity.StatisticsOrganizationCommand;
import com.everhomes.rest.activity.StatisticsOrganizationDTO;
import com.everhomes.rest.activity.StatisticsOrganizationResponse;
import com.everhomes.rest.activity.StatisticsSummaryCommand;
import com.everhomes.rest.activity.StatisticsSummaryResponse;
import com.everhomes.rest.activity.StatisticsTagCommand;
import com.everhomes.rest.activity.StatisticsTagDTO;
import com.everhomes.rest.activity.StatisticsTagResponse;
import com.everhomes.rest.activity.UpdateActivityFormCommand;
import com.everhomes.rest.activity.UpdateActivityGoodsCommand;
import com.everhomes.rest.activity.UpdateSignupInfoCommand;
import com.everhomes.rest.activity.UserAuthFlag;
import com.everhomes.rest.activity.VertifyPersonByPhoneCommand;
import com.everhomes.rest.activity.VideoCallbackCommand;
import com.everhomes.rest.activity.VideoCapabilityResponse;
import com.everhomes.rest.activity.VideoManufacturerType;
import com.everhomes.rest.activity.VideoState;
import com.everhomes.rest.activity.VideoSupportType;
import com.everhomes.rest.activity.WechatSignupFlag;
import com.everhomes.rest.activity.YzbVideoDeviceChangeCommand;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.archives.ArchivesLocaleStringCode;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.common.ActivityDetailActionData;
import com.everhomes.rest.common.ActivityEnrollDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.community_form.CommunityFormErrorCode;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.rest.forum.ForumConstants;
import com.everhomes.rest.forum.ForumModuleType;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.InteractFlag;
import com.everhomes.rest.forum.ListActivityTopicByCategoryAndTagCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.PostCloneFlag;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostFavoriteFlag;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.forum.TopicPublishStatus;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.CreateRefundOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.GetPurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.BusinessOrderType;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.CreateRefundOrderCommand;
import com.everhomes.rest.promotion.order.GetPurchaseOrderCommand;
import com.everhomes.rest.promotion.order.OrderErrorCode;
import com.everhomes.rest.promotion.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.promotion.order.PurchaseOrderDTO;
import com.everhomes.rest.promotion.order.PurchaseOrderPaymentStatus;
import com.everhomes.rest.promotion.order.RefundOrderCommandResponse;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormFileValue;
import com.everhomes.rest.general_approval.GeneralFormFileValueDTO;
import com.everhomes.rest.general_approval.GeneralFormImageValue;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormSubFormValue;
import com.everhomes.rest.general_approval.GeneralFormSubFormValueDTO;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.ListGeneralFormsCommand;
import com.everhomes.rest.general_approval.PostApprovalFormFileDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileValue;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformValue;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;
import com.everhomes.rest.group.LeaveGroupCommand;
import com.everhomes.rest.group.RejectJoinGroupRequestCommand;
import com.everhomes.rest.group.RequestToJoinGroupCommand;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.CreateWechatJsPayOrderCmd;
import com.everhomes.rest.order.CreateWechatJsPayOrderResp;
import com.everhomes.rest.order.OrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PayServiceErrorCode;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.order.*;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.rest.promotion.ModulePromotionInfoDTO;
import com.everhomes.rest.promotion.ModulePromotionInfoType;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.PayZuolinRefundCommand;
import com.everhomes.rest.rentalv2.PayZuolinRefundResponse;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.ui.activity.ListActivityCategoryCommand;
import com.everhomes.rest.ui.activity.ListActivityCategoryReponse;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneCommand;
import com.everhomes.rest.ui.activity.ListActivityPromotionEntitiesBySceneReponse;
import com.everhomes.rest.ui.forum.SelectorBooleanFlag;
import com.everhomes.rest.ui.user.ActivityLocationScope;
import com.everhomes.rest.ui.user.GetVideoPermissionInfoCommand;
import com.everhomes.rest.ui.user.ListNearbyActivitiesBySceneCommand;
import com.everhomes.rest.ui.user.RequestVideoPermissionCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.ui.user.UserVideoPermissionDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserFavoriteDTO;
import com.everhomes.rest.user.UserFavoriteTargetType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sensitiveWord.SensitiveWordService;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityCategories;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.StatusChecker;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.VersionRange;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import net.greghaines.jesque.Job;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.everhomes.poll.ProcessStatus.NOTSTART;
import static com.everhomes.poll.ProcessStatus.UNDERWAY;
import static com.everhomes.util.RuntimeErrorException.errorWith;


@Component
public class ActivityServiceImpl implements ActivityService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);
    

//    private static final String SIGNUP_AUTO_COMMENT = "signup.auto.comment";
//
//    private static final String CHECKIN_AUTO_COMMENT = "checkin.auto.comment";
//    
//    private static final String CONFIRM_AUTO_COMMENT="confirm.auto.comment";
//    
//    private static final String CANCEL_AUTO_COMMENT="cancel.auto.comment";
//    
//    private static final String REJECT_AUTO_COMMENT="reject.auto.comment";
    private static final String DEFAULT_HOME_URL = "default.server.url";
    @Autowired
    private ForumService forumService;

	@Autowired
	JesqueClientFactory jesqueClientFactory;
	
    @Autowired
    private ActivityProivider activityProvider;
    
    @Autowired
    private WarningSettingProvider warningSettingProvider;

    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private FamilyProvider familyProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private ActivityVideoProvider activityVideoProvider;
    
    @Autowired
    private YzbDeviceProvider yzbDeviceProvider;
    
    @Autowired
    private YzbVideoService yzbVideoService;
    
    @Autowired
    private ScheduleProvider scheduleProvider;
    
    @Autowired
    private NamespacesProvider namespacesProvider;

    @Autowired
    private WorkerPoolFactory workerPoolFactory;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
	private OrderUtil commonOrderUtil;
    
	@Autowired
	private AppProvider appProvider; 
    
	@Autowired
	private OnlinePayService onlinePayService;
	
	@Autowired
	private RosterOrderSettingProvider rosterOrderSettingProvider;
	
	@Autowired
	private RosterPayTimeoutService rosterPayTimeoutService;

	@Autowired
    private PayService payServiceV2;

    @Value("${server.contextPath:}")
    private String contextPath;

	@Autowired
    private NamespaceProvider namespaceProvider;

    @Autowired
    private GeneralFormService generalFormService;

    @Autowired
    private GeneralFormValProvider generalFormValProvider;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private GeneralOrderService orderService;

    @Autowired
    private ActivitySignupFormHandler activitySignupFormHandler;
    @Autowired
    private TaskService taskService;

    @Autowired
    private CommunityFormProvider communityFormProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralApprovalService generalApprovalService;
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(WarnActivityBeginningAction.QUEUE_NAME);
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    @Override
    public void createPost(ActivityPostCommand cmd, Long postId, Long communityId) {
        User user = UserContext.current().getUser();
        Activity activity = ConvertHelper.convert(cmd, Activity.class);
        activity.setId(cmd.getId());
        activity.setPostId(postId);
        activity.setNamespaceId(0);
        activity.setCreatorUid(user.getId());
        activity.setGroupDiscriminator(EntityType.ACTIVITY.getCode());
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        activity.setNamespaceId(namespaceId);
        activity.setGuest(cmd.getGuest());

        // avoid nullpoint
        activity.setCheckinAttendeeCount(0);
        
        //if条件用于暂存或者立刻发布，不传默认2立刻发布  activity-3.0.0 增加暂存功能   add by yanjun 20170510 
        if(activity.getStatus() == null){
        	//status:状态，0-无效、1-草稿、2-已发布
            activity.setStatus((byte)2);
        }
        
        activity.setCheckinFamilyCount(0);
        activity.setConfirmAttendeeCount(0);
        activity.setConfirmFamilyCount(0);
        activity.setSignupAttendeeCount(0);
        activity.setSignupFamilyCount(0);
        activity.setSignupFlag(cmd.getCheckinFlag());
        if(cmd.getLongitude()!=null&&cmd.getLatitude()!=null){
            String geohash=GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            activity.setGeohash(geohash);
            }
        
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        //if date time format is not ok,return now
        Date convertStartTime = convert(cmd.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date convertEndTime = convert(cmd.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        Date signupEndTime = null;
        if (org.apache.commons.lang.StringUtils.isBlank(cmd.getSignupEndTime())) {
			signupEndTime = convertStartTime;
		}else {
			signupEndTime = convert(cmd.getSignupEndTime(), "yyyy-MM-dd HH:mm:ss");
		}
        long endTimeMs=  DateHelper.currentGMTTime().getTime();
        long startTimeMs = DateHelper.currentGMTTime().getTime();
        if(convertStartTime!=null){
            startTimeMs=convertStartTime.getTime();
        }
        if(convertEndTime!=null){
            endTimeMs=convertEndTime.getTime();
        }
        activity.setPosterUri(cmd.getPosterUri());
        activity.setStartTime(new Timestamp(startTimeMs));
        activity.setEndTime(new Timestamp(endTimeMs));
        activity.setSignupEndTime(new Timestamp(signupEndTime.getTime()));
        activity.setStartTimeMs(startTimeMs);
        activity.setEndTimeMs(endTimeMs);
        activity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        //added by janson
        if(cmd.getIsVideoSupport() == null) {
            cmd.setIsVideoSupport((byte)0);
        }
        activity.setIsVideoSupport(cmd.getIsVideoSupport());
        activity.setVideoUrl(cmd.getVideoUrl());
        activity.setVideoState(VideoState.UN_READY.getCode());
        
        //add by tt, 添加版本号，20161018
        activity.setVersion(UserContext.current().getVersion());
        
        //add by xiongying, 添加类型id， 20161117
        activity.setCategoryId(cmd.getCategoryId());
        activity.setContentCategoryId(cmd.getContentCategoryId());

        //add by liangyanlong, 增加企业ID，用户付款时，查询付款方.
        activity.setOrganizationId(cmd.getOrganizationId());

        activityProvider.createActity(activity);
        createScheduleForActivity(activity);
        
        ActivityRoster roster = new ActivityRoster();
        roster.setFamilyId(user.getAddressId());
        roster.setUid(user.getId());
        roster.setUuid(UUID.randomUUID().toString());
        roster.setActivityId(activity.getId());
        roster.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setConfirmFamilyId(user.getAddressId());
        roster.setAdultCount(0);
        roster.setChildCount(0);
        roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        roster.setCheckinFlag(CheckInStatus.CHECKIN.getCode());
        roster.setStatus(ActivityRosterStatus.NORMAL.getCode());
        
        // 添加活动报名时新增的姓名、职位等信息, add by tt, 20170228
        addAdditionalInfo(roster, user, activity);
        CommunityGeneralForm communityGeneralForm = this.communityFormProvider.findCommunityGeneralForm(communityId, CommunityFormType.ACTIVITY_SIGNUP);
        if (communityGeneralForm != null) {
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(communityGeneralForm.getFormOriginId());
            if (form != null) {
                roster.setFormId(form.getId());
                if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                    form.setStatus(GeneralFormStatus.RUNNING.getCode());
                    generalFormProvider.updateGeneralForm(form);
                }
            }
        }
        activityProvider.createActivityRoster(roster);
//        // 注册成功事件 add by jiarui
//        LocalEventBus.publish(event -> {
//            LocalEventContext localEventcontext = new LocalEventContext();
//            localEventcontext.setUid(UserContext.currentUserId());
//            localEventcontext.setNamespaceId(activity.getNamespaceId());
//            event.setContext(localEventcontext);
//            Map<String, Object> map = new HashMap<>();
//            map.put(EntityType.ACTIVITY_ROSTER.getCode(), roster);
//            map.put("categoryId", activity.getCategoryId());
//            event.setParams(map);
//            event.setEntityType(EntityType.ACTIVITY_ROSTER.getCode());
//            event.setEntityId(UserContext.currentUserId());
//            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft());
//        });

    }

    //活动报名
    @Override
    public ActivityDTO signup(ActivitySignupCommand cmd) {

    	//检查版本  add by yanjun 20170626
        //对接表单后，不能确认是微信端报名还是客户端报名。也不需要在判断版本 add by yanlong.liang 20180813
//    	checkPayVersion(cmd);
    	
    	//先删除已经过期未支付的活动 add by yanjun 20170417
    	this.cancelExpireRosters(cmd.getActivityId());

    	// 把锁放在查询语句的外面，update by tt, 20170210
        Tuple<ActivityDTO, Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+cmd.getActivityId()).enter(() -> {
            return (ActivityDTO) dbProvider.execute((status) -> {

                LOGGER.warn("------signup start ");

                long signupStatStartTime = System.currentTimeMillis();
                User user = UserContext.current().getUser();
                Activity activity = activityProvider.findActivityById(cmd.getActivityId());
                if (activity == null) {
                    LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
                    throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                            ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
                }
                LOGGER.info("signup start activityId: " + activity.getId() + " userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

                Post post = forumProvider.findPostById(activity.getPostId());
                if (post == null) {
                    LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
                    throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                            ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
                }

                //如果有正常的报名，则直接返回， ActivityDTO的处理方法有下面整合   add by yanjun 20170525
                ActivityRoster oldRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
                if (oldRoster != null) {
                    ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);

                    //提取成一个方法   add  by yanjun 20170525
                    populateActivityDto(dto, activity, oldRoster, post);

                    fixupVideoInfo(dto);//added by janson

                    //add by yanjun 20170512
                    dto.setUserRosterId(oldRoster.getId());

                    LOGGER.warn("------ already sign  signup end userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

                    return dto;
                }

                //检查是否超过报名人数限制, add by tt, 20161012
                // 因为使用新规则已报名=已确认。  如果活动不需要确认在报名时限制人数，如果活动需要确认则在确认时限制。     add by yanjun 20170503
                if (activity.getConfirmFlag() != null && activity.getConfirmFlag().intValue() == 0 && activity.getMaxQuantity() != null && activity.getSignupAttendeeCount() >= activity.getMaxQuantity().intValue()) {
                    throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                            ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
                            "beyond contraint quantity!");
                }

                // 添加报名截止时间检查，add by tt, 20170228
                Timestamp signupEndTime = getSignupEndTime(activity);
                if (System.currentTimeMillis() > signupEndTime.getTime()) {
                    throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                            ActivityServiceErrorCode.ERROR_BEYOND_ACTIVITY_SIGNUP_END_TIME,
                            "beyond activity signup end time!");
                }

                long rosterStatStartTime = System.currentTimeMillis();
                ActivityRoster roster = createRoster(cmd, user, activity);
                //去掉报名评论 by xiongying 20160615
                //            Post comment = new Post();
                //            comment.setParentPostId(post.getId());
                //            comment.setForumId(post.getForumId());
                //            comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                //            comment.setCreatorUid(user.getId());
                //            comment.setContentType(PostContentType.TEXT.getCode());
                ////            String template = configurationProvider.getValue(SIGNUP_AUTO_COMMENT, "");
                //            String template = localeStringService.getLocalizedString(
                //            		ActivityLocalStringCode.SCOPE,
                //                    String.valueOf(ActivityLocalStringCode.ACTIVITY_SIGNUP),
                //                    UserContext.current().getUser().getLocale(),
                //                    "");
                //
                //            if (!StringUtils.isEmpty(template)) {
                //                comment.setContent(template);
                //                forumProvider.createPost(comment);
                //            }
                if (activity.getGroupId() != null && activity.getGroupId() != 0) {
                    RequestToJoinGroupCommand joinCmd = new RequestToJoinGroupCommand();
                    joinCmd.setGroupId(activity.getGroupId());
                    joinCmd.setRequestText("request to join activity group");
                    try {
                        groupService.requestToJoinGroup(joinCmd);
                    } catch (Exception e) {
                        LOGGER.error("join to group failed", e);
                    }

                }

                //因为使用新规则已报名=已确认。  if条件     add by yanjun 20170503
                //1、signup：时不需要确认的话，立刻添加到已报名人数；2、conform：添加到已报名人数；3、reject：不处理；4、cancel：如果已确认则减，如果未确认则不处理
                if (activity.getConfirmFlag() == 0) {
                    int adult = cmd.getAdultCount() == null ? 0 : cmd.getAdultCount();
                    int child = cmd.getChildCount() == null ? 0 : cmd.getChildCount();
                    activity.setSignupAttendeeCount(activity.getSignupAttendeeCount() + adult + child);
                    if (user.getAddressId() != null) {
                        activity.setSignupFamilyCount(activity.getSignupFamilyCount() + 1);
                    }
                }

                //收费且不需要确认的报名下一步就是支付了，所以先生成订单。设置订单开始时间，过期时间，用于定时取消订单  add by yanjun 20170516
                if (activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode() && activity.getConfirmFlag() == 0) {
                    populateNewRosterOrder(roster, activity.getCategoryId());
                }

//	            activityProvider.createActivityRoster(roster);
                if (cmd.getValues() == null) {
                    UserIdentifier userIdentifier = this.userProvider.findUserIdentifiersOfUser(user.getId(),UserContext.getCurrentNamespaceId());
                    if (userIdentifier != null) {
                        roster.setPhone(userIdentifier.getIdentifierToken());
                    }
                    roster.setRealName(user.getNickName());
                }
                createActivityRoster(roster);

                if (cmd.getValues() != null) {
                    //报名对接表单，添加表单值数据
                    addGeneralFormValuesCommand addGeneralFormValuesCommand = new addGeneralFormValuesCommand();
                    ActivityRoster creator = this.activityProvider.findRosterByUidAndActivityId(activity.getId(), activity.getCreatorUid(), ActivityRosterStatus.NORMAL.getCode());
                    if (creator != null) {
                        GeneralForm form = this.generalFormProvider.findGeneralFormById(creator.getFormId());
                        if (form != null) {
                            addGeneralFormValuesCommand.setGeneralFormVersion(form.getFormVersion());
                        }
                    }
                    addGeneralFormValuesCommand.setGeneralFormId(cmd.getFormOriginId());
                    addGeneralFormValuesCommand.setSourceId(roster.getId());
                    addGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
                    addGeneralFormValuesCommand.setValues(cmd.getValues());
                    this.generalFormService.addGeneralFormValues(addGeneralFormValuesCommand);
                }

                //启动定时器，当时间超过设定时间时，取消订单。 条件与前面设置订单号、订单开始时间一致。放在此处是因为定时器需要rosterId   add by yanjun 20170516
                if (activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode() && activity.getConfirmFlag() == 0) {
                    rosterPayTimeoutService.pushTimeout(roster);
                }

                Activity temp = activityProvider.findActivityById(activity.getId());

                LOGGER.warn("***************************************************** tempcount: " + temp.getSignupAttendeeCount() + "activitycount: " + activity.getSignupAttendeeCount());

                activityProvider.updateActivity(activity);


                Activity temp1 = activityProvider.findActivityById(activity.getId());

                LOGGER.warn("***************************************************** tempcount: " + temp1.getSignupAttendeeCount());

//	            return status;
                ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);

                //提取成一个方法   add  by yanjun 20170525
                populateActivityDto(dto, activity, roster, post);

                fixupVideoInfo(dto);//added by janson

                //add by yanjun 20170512
                dto.setUserRosterId(roster.getId());

                //add by jiarui  20180716
                syncToPotentialCustomer(temp1,roster);

                //Send message to creator
                Map<String, String> map = new HashMap<String, String>();
                map.put("userName", user.getNickName());
                map.put("postName", activity.getSubject());


                if (activity.getConfirmFlag() == 0) {
                    //创建带链接跳转的消息头    add by yanjun 20170513

                    ActivityDetailActionData actionData = new ActivityDetailActionData();
                    actionData.setForumId(post.getForumId());
                    actionData.setTopicId(post.getId());
                    String url = RouterBuilder.build(Router.ACTIVITY_DETAIL, actionData);

                    Map<String, String> meta = createActivityRouterMeta(url, null);

                    sendMessageCode(activity.getCreatorUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_TO_CREATOR, meta);

                } else {
                    //创建带链接跳转的消息头    add by yanjun 20170513

                    ActivityEnrollDetailActionData actionData = new ActivityEnrollDetailActionData();
                    actionData.setActivityId(activity.getId());
                    String url = RouterBuilder.build(Router.ACTIVITY_ENROLL_DETAIL, actionData);

                    String subject = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
                            String.valueOf(ActivityLocalStringCode.ACTIVITY_TO_CONFIRM),
                            UserContext.current().getUser().getLocale(),
                            "Activity Wait to Confirm");

                    Map<String, String> meta = createActivityRouterMeta(url, subject);

                    sendMessageCode(activity.getCreatorUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_TO_CREATOR_CONFIRM, meta);
                }
                Map<String, String> rosterMap = new HashMap<String, String>();
                rosterMap.put("postName", activity.getSubject());
                sendMessageCode(user.getId(), user.getLocale(), rosterMap, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_SUCCESS, null);
                long signupStatEndTime = System.currentTimeMillis();
                LOGGER.debug("Signup success, totalElapse={}, rosterElapse={}, cmd={}", (signupStatEndTime - signupStatStartTime),
                        (signupStatEndTime - rosterStatStartTime), cmd);

                return dto;
            });
        });

        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhActivities.class.getSimpleName());
            event.setEntityId(tuple.first().getActivityId());
            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ENTER.dft());
        });

        //活动报名对接积分 add by yanjun 20171211
        // activitySignPoints(cmd.getActivityId());
    	return tuple.first();
	 }

    private void syncToPotentialCustomer(Activity activity,ActivityRoster roster) {
        // 同步线索客户 add by jiarui
        LocalEventBus.publish(event -> {
            LocalEventContext localEventcontext = new LocalEventContext();
            localEventcontext.setUid(UserContext.currentUserId());
            localEventcontext.setNamespaceId(activity.getNamespaceId());
            event.setContext(localEventcontext);
            Map<String, Object> map = new HashMap<>();
            map.put(EntityType.ACTIVITY_ROSTER.getCode(), roster);
            map.put("categoryId", activity.getCategoryId());
            event.setParams(map);
            event.setEntityType(EntityType.ACTIVITY_ROSTER.getCode());
            event.setEntityId(UserContext.currentUserId());
            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft());
        });
    }


	/*private void activitySignPoints(Long activityId){
		Activity activityTemp = activityProvider.findActivityById(activityId);
		if(activityTemp == null){
			return;
		}

		Post postTemp = forumProvider.findPostById(activityTemp.getPostId());
		if(postTemp == null){
			return;
		}

		Long  userId = UserContext.currentUserId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		LocalEventBus.publish(event -> {
			LocalEventContext context = new LocalEventContext();
			context.setUid(userId);
			context.setNamespaceId(namespaceId);
			event.setContext(context);

			event.setEntityType(EhForumPosts.class.getSimpleName());
			event.setEntityId(postTemp.getId());
			event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ENTER.dft());
		});
	}*/

	 private void checkPayVersion(ActivitySignupCommand cmd){
		 Activity activity = activityProvider.findActivityById(cmd.getActivityId());
		 String version = UserContext.current().getVersion();

		 if(activity.getChargeFlag() == null || activity.getChargeFlag().byteValue() == ActivityChargeFlag.UNCHARGE.getCode() ){
		 	return;
		 }

		 // 来自微信的请求支持支付报名   edit by yanjun 20170713
		 if(cmd.getSignupSourceFlag() != null && cmd.getSignupSourceFlag().byteValue() == ActivityRosterSourceFlag.WECHAT.getCode()){
			 return;
		 }

		 LOGGER.info("UserContext current getVersion , version = {}", version);

		 if(version == null){
			 throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
					 ActivityServiceErrorCode.ERROR_VERSION_NOT_SUPPORT_PAY,
					 "Please update your App");
		 }

		 VersionRange versionRange = new VersionRange("["+version+","+version+")");
		 VersionRange versionRangeMin = new VersionRange("[4.5.4,4.5.4)");

		 if(((int)versionRange.getUpperBound()) < ((int)versionRangeMin.getUpperBound())){
			 throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
					 ActivityServiceErrorCode.ERROR_VERSION_NOT_SUPPORT_PAY,
					 "Please update your App");
		 }
	 }
    
    /**
     * 由signup方法提取处理，填充各种信息
     * @param dto
     * @param activity
     * @param roster
     * @param post
     */
    private void populateActivityDto(ActivityDTO dto, Activity activity, ActivityRoster roster, Post post){
    	dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
    }
    
    /**
     * 填充新订单信息，订单id、支付状态、时间等
     * @param roster
     */
    private void populateNewRosterOrder(ActivityRoster roster, Long categoryId){
    	Long orderNo = this.onlinePayService.createBillId(DateHelper
				.currentGMTTime().getTime());
    	roster.setPayFlag(ActivityRosterPayFlag.UNPAY.getCode());
    	
    	GetRosterOrderSettingCommand settingCmd = new GetRosterOrderSettingCommand();
    	settingCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
    	settingCmd.setCategoryId(categoryId);
    	RosterOrderSettingDTO dto = this.getRosterOrderSetting(settingCmd);
    	
    	Long nowTime = DateHelper.currentGMTTime().getTime();
    	roster.setOrderStartTime(new Timestamp(nowTime));
    	roster.setOrderExpireTime(new Timestamp(nowTime + dto.getTime()));
    }
    
    /**
     * 删除应过期的活动
     * @param activityId
     */
    private void cancelExpireRosters(Long activityId){
    	long startTime = System.currentTimeMillis();
    	Activity activity = activityProvider.findActivityById(activityId);
    	if(activity == null || activity.getChargeFlag() == null || activity.getChargeFlag().byteValue() != ActivityChargeFlag.CHARGE.getCode()){
    		LOGGER.warn("No need to cancel expire rosters, activityId={}, activity={}, count ={}", activityId, activity, activity.getSignupAttendeeCount());
    		return;
    	}
    	
    	List<ActivityRoster> listRoster = activityProvider.findExpireRostersByActivityId(activityId);
    	if(listRoster != null){
    		for(int i=0; i< listRoster.size(); i++){
    			ActivityCancelSignupCommand cancelCmd = new ActivityCancelSignupCommand();
        		cancelCmd.setActivityId(listRoster.get(i).getActivityId());
        		cancelCmd.setUserId(listRoster.get(i).getUid());
        		cancelCmd.setCancelType(ActivityCancelType.EXPIRE_AUTO.getCode());
        		this.cancelSignup(cancelCmd);
    		}
    		
    	}
    	long endTime = System.currentTimeMillis();
    	LOGGER.debug("Cancel expire rosters, activityId={}, elapse={}", activityId, (endTime - startTime));
    }

	@Override
	public CommonOrderDTO createSignupOrder(CreateSignupOrderCommand cmd) {
//		ActivityRoster roster = activityProvider.findRosterById(cmd.getActivityRosterId());
		
		ActivityRoster roster  = activityProvider.findRosterByUidAndActivityId(cmd.getActivityId(), UserContext.current().getUser().getId(), ActivityRosterStatus.NORMAL.getCode());
		if(roster == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
					"no roster.");
		}
		Activity activity = activityProvider.findActivityById(roster.getActivityId());
		if(activity == null){
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
					"no activity.");
		}
		
		//设置过期时间
		GetActivityTimeCommand timeCmd = new GetActivityTimeCommand();
		timeCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		ActivityTimeResponse  timeResponse = this.getActivityTime(timeCmd);
		Map<String, Long> bodyMap = new HashMap<String, Long>();
		Long expiredTime = roster.getOrderStartTime().getTime() + timeResponse.getOrderTime();
		bodyMap.put("expiredTime", expiredTime);
		
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		String temple = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE, 
				String.valueOf(ActivityLocalStringCode.ACTIVITY_PAY_FEE), 
				UserContext.current().getUser().getLocale(), 
				"activity roster pay");
		
		orderCmd.setBody(bodyMap.toString());
		orderCmd.setOrderNo(roster.getOrderNo().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.ACTIVITYSIGNUPORDER.getPycode());
		orderCmd.setSubject(temple);
		orderCmd.setTotalFee(activity.getChargePrice());
		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail.",e);
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_CONVERT_TO_COMMON_ORDER_FAIL,
					"convertToCommonOrder is fail.");
		}
		
		return dto;
	}
    //更换使用新的支付V3
//	@Override
//	public PreOrderDTO createSignupOrderV2(CreateSignupOrderV2Command cmd) {
//
//
//		ActivityRoster roster  = activityProvider.findRosterByUidAndActivityId(cmd.getActivityId(), UserContext.current().getUser().getId(), ActivityRosterStatus.NORMAL.getCode());
//		if(roster == null){
//			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
//					"no roster.");
//		}
//		Activity activity = activityProvider.findActivityById(roster.getActivityId());
//		if(activity == null){
//			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
//					"no activity.");
//		}
//
//		PreOrderCommand preOrderCommand = new PreOrderCommand();
//
//		preOrderCommand.setOrderType(OrderType.OrderTypeEnum.ACTIVITYSIGNUPORDER.getPycode());
//		preOrderCommand.setOrderId(roster.getOrderNo());
//		Long amount = payService.changePayAmount(activity.getChargePrice());
//		preOrderCommand.setAmount(amount);
//
//		preOrderCommand.setPayerId(roster.getUid());
//		preOrderCommand.setNamespaceId(activity.getNamespaceId());
//
//		GetActivityTimeCommand timeCmd = new GetActivityTimeCommand();
//		timeCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
//		ActivityTimeResponse  timeResponse = this.getActivityTime(timeCmd);
//		Long expiredTime = roster.getOrderStartTime().getTime() + timeResponse.getOrderTime();
//
//
//		preOrderCommand.setExpiration(expiredTime);
//
//
//		preOrderCommand.setClientAppName(cmd.getClientAppName());
//
//		//微信公众号支付，重新设置ClientName，设置支付方式和参数
//		if(cmd.getPaymentType() != null && cmd.getPaymentType().intValue() == PaymentType.WECHAT_JS_PAY.getCode()){
//
//			if(preOrderCommand.getClientAppName() == null){
//				Integer namespaceId = UserContext.getCurrentNamespaceId();
//				preOrderCommand.setClientAppName("wechat_" + namespaceId);
//			}
//			preOrderCommand.setPaymentType(PaymentType.WECHAT_JS_PAY.getCode());
//			PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
//			paymentParamsDTO.setPayType("no_credit");
//			User user = UserContext.current().getUser();
//			paymentParamsDTO.setAcct(user.getNamespaceUserToken());
//		}
//
//
//		PreOrderDTO callBack = payService.createPreOrder(preOrderCommand);
//
//		return callBack;
//	}

//    @Override
//    public PreOrderDTO createSignupOrderV3(CreateSignupOrderV2Command cmd) {
//        ActivityRoster roster  = activityProvider.findRosterByUidAndActivityId(cmd.getActivityId(), UserContext.current().getUser().getId(), ActivityRosterStatus.NORMAL.getCode());
//        if(roster == null){
//            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
//                    "no roster.");
//        }
//        if (roster.getPayOrderId() != null) {
//            activityProvider.deleteRoster(roster);
//            roster.setId(null);
//            roster.setPayOrderId(null);
//            Long orderNo = this.onlinePayService.createBillId(DateHelper
//                    .currentGMTTime().getTime());
//            roster.setOrderNo(orderNo);
//            activityProvider.createActivityRoster(roster);
//        }
//        Activity activity = activityProvider.findActivityById(roster.getActivityId());
//        if(activity == null){
//            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
//                    "no activity.");
//        }
//
//        CreateOrderCommand createOrderCommand = new CreateOrderCommand();
//        setPreOrder(cmd,createOrderCommand,roster,activity);
//
//        PreOrderDTO callback = new PreOrderDTO();
//        OrderCommandResponse response = this.createPreOrder(createOrderCommand);
//        callback = ConvertHelper.convert(response, PreOrderDTO.class);
//        callback.setExpiredIntervalTime(response.getExpirationMillis());
//
//        //组装支付方式
//        List<PayMethodDTO> list = new ArrayList<>();
//        String format = "{\"getOrderInfoUrl\":\"%s\"}";
//        for (com.everhomes.pay.order.PayMethodDTO p : response.getPaymentMethods()) {
//            PayMethodDTO payMethodDTO = new PayMethodDTO();//支付方式
//            payMethodDTO.setPaymentName(p.getPaymentName());
//            payMethodDTO.setExtendInfo(String.format(format, response.getOrderPaymentStatusQueryUrl()));
//            String paymentLogo = contentServerService.parserUri(p.getPaymentLogo());
//            payMethodDTO.setPaymentLogo(paymentLogo);
//            payMethodDTO.setPaymentType(p.getPaymentType());
//            PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
//            com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = p.getPaymentParams();
//            if(bizPaymentParamsDTO != null) {
//                paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
//            }
//            payMethodDTO.setPaymentParams(paymentParamsDTO);
//            list.add(payMethodDTO);
//        }
//        callback.setPayMethod(list);
//        roster.setPayOrderId(callback.getOrderId());
//        activityProvider.updateRoster(roster);
//        return callback;
//    }

    private PurchaseOrderCommandResponse createCommonSignupOrder(CreateSignupOrderV2Command cmd) {
        //校验参数
        ActivityRoster roster = checkRoster(cmd);
        Activity activity = checkActivity(roster);

        //收款方
        ActivityBizPayee activityBizPayee = checkPaymentPayeeId(activity);

        //组装订单数据
        CreatePurchaseOrderCommand createPurchaseOrderCommand = preparePaymentSignupOrder(cmd, roster, activity, activityBizPayee);

        // 发送下单请求
        return sendCreatePreOrderRequest(createPurchaseOrderCommand);
    }

    @Override
    public PreOrderDTO createUniteSignupOrder(CreateSignupOrderV2Command cmd) {
        PurchaseOrderCommandResponse response = createCommonSignupOrder(cmd);

        afterSignupOrderCreated(cmd,response);
        // 返回给客户端支付的报文
        return populatePreOrderDto(response);
    }

    private ActivityRoster checkRoster(CreateSignupOrderV2Command cmd){
        ActivityRoster roster  = activityProvider.findRosterByUidAndActivityId(cmd.getActivityId(), UserContext.current().getUser().getId(), ActivityRosterStatus.NORMAL.getCode());
        if(roster == null){
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
                    "no roster.");
        }
        return roster;
    }
    private Activity checkActivity(ActivityRoster roster) {
        Activity activity = activityProvider.findActivityById(roster.getActivityId());
        if(activity == null){
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                    "no activity.");
        }
        return activity;
    }
    private ActivityBizPayee checkPaymentPayeeId(Activity activity){
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(activity.getCategoryId(), activity.getNamespaceId());
        if (activityCategories == null) {
            LOGGER.error("activityCategories cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "activityCategories cannot be null.");
        }
        ActivityBizPayee activityBizPayee = this.activityProvider.getActivityPayee(activityCategories.getId(),activity.getNamespaceId());
        if (activityBizPayee == null) {
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                    "no payee.");
        }
        return activityBizPayee;
    }

    private CreatePurchaseOrderCommand preparePaymentSignupOrder(CreateSignupOrderV2Command cmd, ActivityRoster roster, Activity activity, ActivityBizPayee activityBizPayee) {
        CreatePurchaseOrderCommand createPurchaseOrderCommand = new CreatePurchaseOrderCommand();

        BigDecimal amout = activity.getChargePrice();
        if(amout == null){
            createPurchaseOrderCommand.setAmount(new BigDecimal(0).longValue());
        }
        createPurchaseOrderCommand.setAmount(amout.multiply(new BigDecimal(100)).longValue());

        createPurchaseOrderCommand.setAccountCode(generateAccountCode());
        createPurchaseOrderCommand.setClientAppName(cmd.getClientAppName());

        createPurchaseOrderCommand.setBusinessOrderType(generateBusinessOrderType(cmd));
        createPurchaseOrderCommand.setBusinessPayerType(BusinessPayerType.USER.getCode());
        createPurchaseOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
        createPurchaseOrderCommand.setBusinessPayerParams(getBusinessPayerParams(cmd));

        createPurchaseOrderCommand.setPaymentPayeeId(activityBizPayee.getBizPayeeId());
        createPurchaseOrderCommand.setPaymentPayeeType(activityBizPayee.getBizPayeeType());

        createPurchaseOrderCommand.setExtendInfo(getExtendInfo(activity));
        createPurchaseOrderCommand.setPaymentParams(getPaymentParams(cmd));
        createPurchaseOrderCommand.setExpirationMillis(getExpirationMillis(roster));
        createPurchaseOrderCommand.setCallbackUrl(getPayCallbackUrl());
        createPurchaseOrderCommand.setGoodName("活动报名");
        createPurchaseOrderCommand.setGoodsDescription(null);
        createPurchaseOrderCommand.setIndustryName(null);
        createPurchaseOrderCommand.setIndustryCode(null);
        createPurchaseOrderCommand.setSourceType(SourceType.MOBILE.getCode());
        createPurchaseOrderCommand.setOrderRemark1("活动报名");
        createPurchaseOrderCommand.setOrderRemark3(null);
        createPurchaseOrderCommand.setOrderRemark4(null);
        createPurchaseOrderCommand.setOrderRemark5(null);
        String systemId = configurationProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
        createPurchaseOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        return createPurchaseOrderCommand;
    }

    private PurchaseOrderCommandResponse sendCreatePreOrderRequest(CreatePurchaseOrderCommand createOrderCommand) {
        CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
        if(!checkOrderRestResponseIsSuccess(createOrderResp)) {
            String scope = OrderErrorCode.SCOPE;
            int code = OrderErrorCode.ERROR_CREATE_ORDER_FAILED;
            String description = "Failed to create order";
            if(createOrderResp != null) {
                code = (createOrderResp.getErrorCode() == null) ? code : createOrderResp.getErrorCode()  ;
                scope = (createOrderResp.getErrorScope() == null) ? scope : createOrderResp.getErrorScope();
                description = (createOrderResp.getErrorDescription() == null) ? description : createOrderResp.getErrorDescription();
            }
            throw RuntimeErrorException.errorWith(scope, code, description);
        }

        PurchaseOrderCommandResponse orderCommandResponse = createOrderResp.getResponse();

        return orderCommandResponse;
    }

    private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
        if(response != null && response.getErrorCode() != null
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }
    private String generateAccountCode(){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        return "NS"+namespaceId;
    }

    private String generateBusinessOrderType(CreateSignupOrderV2Command cmd){
        if (cmd.getPaymentType() != null) {
            return BusinessOrderType.ACTIVITYSIGNUPORDERWECHAT.getCode();
        }else {
            return BusinessOrderType.ACTIVITYSIGNUPORDER.getCode();
        }
    }

    private String getBusinessPayerParams(CreateSignupOrderV2Command cmd) {
        OwnerType businessPayerType = OwnerType.USER;

        // 如果参数中的付款方ID不正确，则使用当前用户ID
        Long businessPayerId = UserContext.currentUserId();

        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, UserContext.getCurrentNamespaceId());
        String buyerPhone = null;
        if(buyerIdentifier != null) {
            buyerPhone = buyerIdentifier.getIdentifierToken();
        }
        // 找不到手机号则默认一个
        if(buyerPhone == null || buyerPhone.trim().length() == 0) {
            buyerPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("businessPayerPhone", buyerPhone);
        return StringHelper.toJsonString(map);
    }

    private String getExtendInfo(Activity activity) {
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(activity.getCategoryId(), activity.getNamespaceId());
        return "应用名称："+activityCategories.getName()+"；活动名称："+activity.getSubject();
    }

    private Map<String, String> getPaymentParams(CreateSignupOrderV2Command cmd) {
        Map<String, String> map = null;

        PaymentType paymentType = PaymentType.fromCode(cmd.getPaymentType());
        if (paymentType == PaymentType.WECHAT_JS_ORG_PAY) {
            Long businessPayerId = UserContext.currentUserId();
            User payer = userProvider.findUserById(businessPayerId);
            if(payer != null) {
                map = new HashMap<String, String>();
                map.put("acct", payer.getNamespaceUserToken());
            } else {
                LOGGER.error("User not found, userId={}, activityId={}", businessPayerId, cmd.getActivityId());
            }
        }

        return map;
    }

    private Long getExpirationMillis(ActivityRoster roster) {
        GetActivityTimeCommand timeCmd = new GetActivityTimeCommand();
        timeCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        ActivityTimeResponse  timeResponse = this.getActivityTime(timeCmd);
        Long expiredTime = roster.getOrderStartTime().getTime() + timeResponse.getOrderTime();
        return expiredTime;
    }

    private String getPayCallbackUrl() {
        String configKey = "pay.v2.callback.url.activity";
        String backUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), configKey, "");
        if(backUrl == null || backUrl.trim().length() == 0) {
            LOGGER.error("Payment callback url empty, configKey={}", configKey);
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAY_CALLBACK_URL_EMPTY,
                    "Payment callback url empty");
        }

        if(!backUrl.toLowerCase().startsWith("http")) {
            String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
            backUrl = homeUrl + contextPath + backUrl;
        }
        return backUrl;
    }

    private PreOrderDTO populatePreOrderDto(PurchaseOrderCommandResponse orderResponse){
        OrderCommandResponse orderCommandResponse = orderResponse.getPayResponse();
        PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
        List<PayMethodDTO> payMethods = new ArrayList<>();//业务系统自己的支付方式格式
        List<com.everhomes.pay.order.PayMethodDTO> bizPayMethods = orderCommandResponse.getPaymentMethods();//支付系统传回来的支付方式
        String format = "{\"getOrderInfoUrl\":\"%s\"}";
        for(com.everhomes.pay.order.PayMethodDTO bizPayMethod : bizPayMethods) {
            PayMethodDTO payMethodDTO = new PayMethodDTO();//支付方式
            payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
            payMethodDTO.setExtendInfo(String.format(format, orderCommandResponse.getOrderPaymentStatusQueryUrl()));
            String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
            payMethodDTO.setPaymentLogo(paymentLogo);
            payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
            PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
            com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
            if(bizPaymentParamsDTO != null) {
                paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
            }
            payMethodDTO.setPaymentParams(paymentParamsDTO);
            payMethods.add(payMethodDTO);
        }
        dto.setPayMethod(payMethods);
        dto.setExpiredIntervalTime(orderCommandResponse.getExpirationMillis());
        if(orderResponse.getAmount() != null) {
            dto.setAmount(orderResponse.getAmount().longValue());
        }
        dto.setOrderId(orderResponse.getOrderId());
        return dto;
    }


    private void afterSignupOrderCreated(CreateSignupOrderV2Command cmd, PurchaseOrderCommandResponse response){
        ActivityRoster roster  = activityProvider.findRosterByUidAndActivityId(cmd.getActivityId(), UserContext.current().getUser().getId(), ActivityRosterStatus.NORMAL.getCode());
        roster.setPayOrderId(response.getOrderId());
        roster.setOrderNo(response.getBusinessOrderNumber());
        activityProvider.updateRoster(roster);
    }

    private void setPreOrder(CreateSignupOrderV2Command cmd, CreateOrderCommand createOrderCommand, ActivityRoster roster, Activity activity) {
        createOrderCommand.setAccountCode("NS"+UserContext.getCurrentNamespaceId().toString());
        createOrderCommand.setBizOrderNum("activity"+roster.getOrderNo().toString());

        BigDecimal amout = activity.getChargePrice();
        if(amout == null){
            createOrderCommand.setAmount(0L);
        }
        createOrderCommand.setAmount(amout.multiply(new BigDecimal(100)).longValue());

        //付款方账号
        Long payerId = UserContext.currentUserId();
        PayUserDTO payUserDTO = checkAndCreatePaymentUser(payerId,UserContext.getCurrentNamespaceId());
        createOrderCommand.setPayerUserId(payUserDTO.getId());
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(activity.getCategoryId(), activity.getNamespaceId());
        if (activityCategories == null) {
            LOGGER.error("activityCategories cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "activityCategories cannot be null.");
        }
        ActivityBizPayee activityBizPayee = this.activityProvider.getActivityPayee(activityCategories.getId(),activity.getNamespaceId());
        if (activityBizPayee == null) {
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                    "no payee.");
        }
        //收款方账号
        createOrderCommand.setPayeeUserId(activityBizPayee.getBizPayeeId());
        GetActivityTimeCommand timeCmd = new GetActivityTimeCommand();
        timeCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        ActivityTimeResponse  timeResponse = this.getActivityTime(timeCmd);
        Long expiredTime = roster.getOrderStartTime().getTime() + timeResponse.getOrderTime();

        createOrderCommand.setOrderType(com.everhomes.pay.order.OrderType.PURCHACE.getCode());
        createOrderCommand.setExpirationMillis(expiredTime);
        createOrderCommand.setGoodsName("活动报名");
        createOrderCommand.setSourceType(1);
        createOrderCommand.setClientAppName(cmd.getClientAppName());
        createOrderCommand.setOrderRemark1("活动报名");
        createOrderCommand.setExtendInfo("应用名称："+activityCategories.getName()+"；活动名称："+activity.getSubject());
        createOrderCommand.setExtendInfo(activityCategories.getName());
        //微信公众号支付，重新设置ClientName，设置支付方式和参数
        if(cmd.getPaymentType() != null && cmd.getPaymentType().intValue() == PaymentType.WECHAT_JS_PAY.getCode()){
            createOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
            PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
            paymentParamsDTO.setPayType("no_credit");
            User user = UserContext.current().getUser();
            paymentParamsDTO.setAcct(user.getNamespaceUserToken());
            Map<String, String> flattenMap = new HashMap<>();
            StringHelper.toStringMap(null, paymentParamsDTO, flattenMap);
            createOrderCommand.setPaymentParams(flattenMap);
            createOrderCommand.setCommitFlag(1);
        }

        String homeUrl = configurationProvider.getValue("home.url", "");
        String backUri = configurationProvider.getValue("pay.v2.callback.url.activity", "");
        String backUrl = homeUrl + contextPath + backUri;
        createOrderCommand.setBackUrl(backUrl);
    }

    private PayUserDTO checkAndCreatePaymentUser(Long payerId, Integer namespaceId){
        User userById = userProvider.findUserById(UserContext.currentUserId());
        UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), UserContext.getCurrentNamespaceId());
        String userIdenify = null;
        if(userIdentifier != null) {
            userIdenify = userIdentifier.getIdentifierToken();
        }
        PayUserDTO payUserDTO = new PayUserDTO();
        //根据支付帐号ID列表查询帐号信息
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkAndCreatePaymentUser request={}", payerId);
        }
        String payerid = OwnerType.USER.getCode()+payerId;
        List<PayUserDTO> payUserDTOs = payServiceV2.getPayUserList(payerid);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkAndCreatePaymentUser response={}", payUserDTOs);
        }
        if(payUserDTOs == null || payUserDTOs.size() == 0){
            //创建个人账号
            payUserDTO = payServiceV2.createPersonalPayUserIfAbsent(payerId.toString(), "NS"+namespaceId.toString());
            if(payUserDTO==null){
                throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_CREATE_USER_ACCOUNT,
                        "创建个人付款账户失败");
            }
            String s = payServiceV2.bandPhone(payUserDTO.getId(), userIdenify);//绑定手机号
        }else {
            payUserDTO = payUserDTOs.get(0);
        }
        return payUserDTO;
    }

    private OrderCommandResponse createPreOrder(CreateOrderCommand cmd) {
        CreateOrderRestResponse createOrderRestResponse = new CreateOrderRestResponse();
        if (cmd.getPaymentType() != null && PaymentType.WECHAT_JS_ORG_PAY.getCode() == cmd.getPaymentType()) {
            createOrderRestResponse = this.payServiceV2.createCustomOrder(cmd);
        }else {
            createOrderRestResponse = this.payServiceV2.createPurchaseOrder(cmd);
        }
        PreOrderDTO callback = new PreOrderDTO();
        if (createOrderRestResponse.getErrorCode() != 200) {
            LOGGER.error("create order fail");
            throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
                    "create order fail");
        }
        OrderCommandResponse response = createOrderRestResponse.getResponse();

        return response;
    }

    @Override
	public CreateWechatJsPayOrderResp createWechatJsSignupOrder(CreateWechatJsSignupOrderCommand cmd) {
        CreateSignupOrderV2Command createSignupOrderV2Command = new CreateSignupOrderV2Command();
        createSignupOrderV2Command.setActivityId(cmd.getActivityId());
        createSignupOrderV2Command.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());

        PurchaseOrderCommandResponse response = this.createCommonSignupOrder(createSignupOrderV2Command);

        CreateWechatJsPayOrderResp callback = ConvertHelper.convert(response.getPayResponse(),CreateWechatJsPayOrderResp.class);
        callback.setPayNo(response.getOrderId().toString());
        afterSignupOrderCreated(createSignupOrderV2Command, response);
        return callback;
	}


	private CreateWechatJsPayOrderCmd newWechatOrderCmd(Activity activity, ActivityRoster roster){

		//调用统一处理订单接口，返回统一订单格式
		CreateWechatJsPayOrderCmd orderCmd = new CreateWechatJsPayOrderCmd();
		String temple = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
				String.valueOf(ActivityLocalStringCode.ACTIVITY_PAY_FEE),
				UserContext.current().getUser().getLocale(),
				"activity roster pay");

		//与微信认证登录时候一致，查找当前域空间的公众号，没有就选择默认的。
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		orderCmd.setRealm("wechat_" + namespaceId);
		String appId = configurationProvider.getValue(namespaceId, "wx.offical.account.appid", "");

		//用于判断公众号是否是默认的，默认的话将realm设置为wechat_0
		String appId_default = configurationProvider.getValue("wx.offical.account.appid", "");
		if(appId != null && appId_default != null && appId.equals(appId_default)){
			orderCmd.setRealm("wechat_0");
		}


//		if(StringUtils.isEmpty(appId)){
//			orderCmd.setRealm("wechat_0");
//		}
		orderCmd.setOrderType(OrderType.OrderTypeEnum.ACTIVITYSIGNUPORDERWECHAT.getPycode());
		orderCmd.setOnlinePayStyleNo(VendorType.WEI_XIN.getStyleNo());
		orderCmd.setOrderNo(roster.getOrderNo().toString());
		orderCmd.setOrderAmount(activity.getChargePrice().toString());
		User user = UserContext.current().getUser();
		orderCmd.setUserId(user.getNamespaceUserToken());
		orderCmd.setSubject(temple);

		String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		orderCmd.setAppKey(appKey);
		orderCmd.setTimestamp(timestamp);
		orderCmd.setRandomNum(randomNum);

		App app = appProvider.findAppByKey(appKey);

		Map<String,String> map = new HashMap<String, String>();
		map.put("realm", orderCmd.getRealm());
		map.put("orderType",orderCmd.getOrderType());
		map.put("onlinePayStyleNo",orderCmd.getOnlinePayStyleNo());
		map.put("orderNo",orderCmd.getOrderNo());
		map.put("orderAmount",orderCmd.getOrderAmount());
		map.put("userId", orderCmd.getUserId());
		map.put("subject",orderCmd.getSubject());
		map.put("appKey",orderCmd.getAppKey());
		map.put("timestamp",orderCmd.getTimestamp() + "");
		map.put("randomNum",orderCmd.getRandomNum() + "");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		orderCmd.setSignature(URLEncoder.encode(signature));
		return orderCmd;
	}


    @Override
	public SignupInfoDTO manualSignup(ManualSignupCommand cmd) {
    	Activity outActivity = checkActivityExist(cmd.getActivityId());
    	ActivityRoster activityRoster = (ActivityRoster)this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
	        return (ActivityRoster)dbProvider.execute((status) -> {
		    	User user = UserContext.current().getUser();
		    	// 锁里面要重新查询活动
		        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
		        if (activity == null) {
		            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
		        }

				LOGGER.info("manualSignup start activityId: " + activity.getId() + " userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

		        //检查是否超过报名人数限制, add by tt, 20161012
		        if (activity.getMaxQuantity() != null && activity.getSignupAttendeeCount() >= activity.getMaxQuantity().intValue()) {
		        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
							"beyond contraint quantity!");
				}
		        
		        Post post = forumProvider.findPostById(activity.getPostId());
		        if (post == null) {
		            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
		        }
		        
		        ActivityRoster oldRoster = activityProvider.findRosterByPhoneAndActivityId(cmd.getActivityId(), cmd.getPhone(), ActivityRosterStatus.NORMAL.getCode());
		        if(oldRoster != null){
		        	LOGGER.error("Roster already exist. activityId={}, phone={}", cmd.getActivityId(), cmd.getPhone());
		            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                    ActivityServiceErrorCode.ERROR_ROSTER_ALREADY_EXIST, "Roster already exist. activityId=" + cmd.getActivityId() + " phone=" + cmd.getPhone());
		        }
		        
		        ActivityRoster roster = newRoster(cmd, user, activity);
		        
	            if (activity.getGroupId() != null && activity.getGroupId() != 0) {
	                RequestToJoinGroupCommand joinCmd = new RequestToJoinGroupCommand();
	                joinCmd.setGroupId(activity.getGroupId());
	                joinCmd.setRequestText("request to join activity group");
	                try{
	                    groupService.requestToJoinGroup(joinCmd);
	                }catch(Exception e){
	                    LOGGER.error("join to group failed",e);
	                }
	               
	            }
	            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()+1);
	            activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + 1);
	            
	            //createActivityRoster(roster);
	            activityProvider.createActivityRoster(roster);

	            if (cmd.getValues() != null) {
                    //报名对接表单，添加表单值数据
                    addGeneralFormValuesCommand addGeneralFormValuesCommand = new addGeneralFormValuesCommand();

                    ActivityRoster creator = this.activityProvider.findRosterByUidAndActivityId(activity.getId(), activity.getCreatorUid(), ActivityRosterStatus.NORMAL.getCode());
                    if (creator != null) {
                        GeneralForm form = this.generalFormProvider.findGeneralFormById(creator.getFormId());
                        if (form != null) {
                            addGeneralFormValuesCommand.setGeneralFormVersion(form.getFormVersion());
                        }
                    }
                    addGeneralFormValuesCommand.setGeneralFormId(cmd.getFormOriginId());
                    addGeneralFormValuesCommand.setSourceId(roster.getId());
                    addGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
                    addGeneralFormValuesCommand.setValues(cmd.getValues());
                    this.generalFormService.addGeneralFormValues(addGeneralFormValuesCommand);
                }

	            activityProvider.updateActivity(activity);
                // 注册成功事件 add by jiarui
                LocalEventBus.publish(event -> {
                    LocalEventContext localEventcontext = new LocalEventContext();
                    localEventcontext.setUid(UserContext.currentUserId());
                    localEventcontext.setNamespaceId(activity.getNamespaceId());
                    event.setContext(localEventcontext);
                    Map<String, Object> map = new HashMap<>();
                    map.put(EntityType.ACTIVITY_ROSTER.getCode(), roster);
                    map.put("categoryId", activity.getCategoryId());
                    event.setParams(map);
                    event.setEntityType(EntityType.ACTIVITY_ROSTER.getCode());
                    event.setEntityId(UserContext.currentUserId());
                    event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft());
                });

				LOGGER.info("manualSignup end activityId: " + activity.getId() + " userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

	            return roster;
	        });
        }).first();

        // 报名活动事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(activityRoster.getUid());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhActivities.class.getSimpleName());
            event.setEntityId(activityRoster.getActivityId());
            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ENTER.dft());
        });
		return convertActivityRoster(activityRoster, outActivity);
	}
    
    //如果原来已经报过(重复报名），则更新。
    private void createActivityRoster(ActivityRoster roster){
    	ActivityRoster oldRoster = activityProvider.findRosterByUidAndActivityId(roster.getActivityId(), roster.getUid(), ActivityRosterStatus.NORMAL.getCode());
        if(oldRoster != null){
        	activityProvider.deleteRoster(oldRoster);
        }
        activityProvider.createActivityRoster(roster);
        
    }

	private SignupInfoDTO convertActivityRoster(ActivityRoster activityRoster, Activity activity) {
		SignupInfoDTO signupInfoDTO = ConvertHelper.convert(activityRoster, SignupInfoDTO.class);
		if (activity != null && activityRoster.getUid().longValue() == activity.getCreatorUid().longValue()) {
			signupInfoDTO.setCreateFlag((byte)1);
		}else {
			signupInfoDTO.setCreateFlag((byte)0);
		}

        User user = userProvider.findUserById(activityRoster.getUid());
        if (user == null) {
            user = new User();
            user.setId(0L);
            user.setExecutiveTag((byte) 0);
        }
        signupInfoDTO.setNickName(user.getNickName());
        signupInfoDTO.setType(getAuthFlag(user));

        GetGeneralFormValuesCommand getGeneralFormValuesCommand = new GetGeneralFormValuesCommand();
		getGeneralFormValuesCommand.setSourceId(activityRoster.getId());
		getGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
		getGeneralFormValuesCommand.setOriginFieldFlag(NormalFlag.NEED.getCode());
		List<PostApprovalFormItem> values = this.generalFormService.getGeneralFormValues(getGeneralFormValuesCommand);
		if (values != null) {
            List<PostApprovalFormItem> results = new ArrayList<>();
            for(PostApprovalFormItem postApprovalFormItem : values) {
                GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(postApprovalFormItem.getFieldType());
                if (null != fieldType) {
                    switch (GeneralFormFieldType.fromCode(postApprovalFormItem.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX:
                            results.add(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()));
                            break;
                        case MULTI_LINE_TEXT:
                            results.add(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()));
                            break;
                        case IMAGE:
                            break;
                        case FILE:
                            break;
                        case INTEGER_TEXT:
                            results.add(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()));
                            break;
                        case SUBFORM:
                            break;
                    }
                }
                if (postApprovalFormItem.getFieldName().equals("USER_PHONE")) {
                    signupInfoDTO.setPhone(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()).getFieldValue());
                }
                if (StringUtils.isEmpty(signupInfoDTO.getNickName()) && postApprovalFormItem.getFieldName().equals("USER_NAME")) {
                    signupInfoDTO.setNickName(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()).getFieldValue());
                }
            }
            signupInfoDTO.setValues(results);
        }
		if(activityRoster.getCreateTime() != null){
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			signupInfoDTO.setSignupTime(f.format(activityRoster.getCreateTime()));
		}
		signupInfoDTO.setActivityName(activity.getSubject());
		return signupInfoDTO;
	}

	/**************process form item start***********************/
	//在OriginFiledFlag为Need时，value为{text:xxx}的格式，所有需要再手动转一次。
    //当OriginFiledFLag为NONEED是,获取到的value是正确的，但是fieldDisplayName被去掉了。
    private PostApprovalFormItem processCommonTextField(PostApprovalFormItem formVal, String jsonVal) {
        try {
            formVal.setFieldValue(JSON.parseObject(jsonVal, PostApprovalFormTextValue.class).getText());
        }catch (JSONException json) {
            LOGGER.info("value = {}", jsonVal);
            formVal.setFieldValue(jsonVal);
        }
        return formVal;
    }

    private PostApprovalFormItem processSubFormField(PostApprovalFormItem formVal, GeneralFormFieldDTO dto, String jsonVal) {
        //  取出子表单字段值
        PostApprovalFormSubformValue postSubFormValue = JSON.parseObject(jsonVal, PostApprovalFormSubformValue.class);
        List<GeneralFormSubFormValueDTO> subForms = new ArrayList<>();
        //  解析子表单的值
        for (PostApprovalFormSubformItemValue itemValue : postSubFormValue.getForms()) {
            subForms.add(processSubFormItemField(dto.getFieldExtra(), itemValue));
        }
        GeneralFormSubFormValue subFormValue = new GeneralFormSubFormValue();
        subFormValue.setSubForms(subForms);
        formVal.setFieldValue(subFormValue.toString());
        return formVal;
    }

    private GeneralFormSubFormValueDTO processSubFormItemField(String extraJson, PostApprovalFormSubformItemValue value) {
        //  1.取出子表单字段初始内容
        //  2.将子表单中的值解析
        //  3.将得到的Value放入原有的Extra类中，并组装放入fieldValue中
        GeneralFormSubFormValueDTO result = JSON.parseObject(extraJson, GeneralFormSubFormValueDTO.class);
        Map<String, String> fieldMap = new HashMap<>();

        for (PostApprovalFormItem formVal : value.getValues()) {
            switch (GeneralFormFieldType.fromCode(formVal.getFieldType())) {
                case SINGLE_LINE_TEXT:
                case NUMBER_TEXT:
                case DATE:
                case DROP_BOX:
                    processCommonTextField(formVal, formVal.getFieldValue());
                    break;
                case MULTI_LINE_TEXT:
                    processCommonTextField(formVal, formVal.getFieldValue());
                    break;
                case IMAGE:
                    break;
                case FILE:
                    break;
                case INTEGER_TEXT:
                    processCommonTextField(formVal, formVal.getFieldValue());
                    break;
                case SUBFORM:
                    break;
            }
            fieldMap.put(formVal.getFieldName(), formVal.getFieldValue());
        }
        for (GeneralFormFieldDTO dto : result.getFormFields())
            dto.setFieldValue(fieldMap.get(dto.getFieldName()));
        return result;
    }
    /**************process form item end***********************/

	private Byte getAuthFlag(User user) {
		if (user.getId().longValue() == 0L) {
			return UserAuthFlag.NOT_REGISTER.getCode();
		}
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(user.getId());
		for (OrganizationMember organizationMember : members) {
			if (OrganizationMemberStatus.fromCode(organizationMember.getStatus()) == OrganizationMemberStatus.ACTIVE) {
				return UserAuthFlag.AUTH.getCode();
			}
		}
		return UserAuthFlag.NOT_AUTH.getCode();
	}

	private ActivityRoster newRoster(ManualSignupCommand cmd, User createUser, Activity activity) {

		// 这里是的getUserFromPhone有点小问题的，id为0的用户在我们系统中是存在的，可能会有某些地方显示异常。
		// 尝试过将uid设置成null，导致了app查询报名用户和后台导出报名出现了nullpointexception，还有报名、确认、取消、签到、支付、退款、活动详情及报名统计多个接口等没测试。
		// 素我无能为力，暂时保持原样。
		User user = getUserFromPhone(cmd.getPhone());
		ActivityRoster roster = new ActivityRoster();
		roster.setUuid(UUID.randomUUID().toString());
		roster.setActivityId(activity.getId());
		roster.setUid(user.getId());
        roster.setAdultCount(1);
        roster.setChildCount(0);
        roster.setCheckinFlag(CheckInStatus.UN_CHECKIN.getCode());
        roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        roster.setConfirmUid(createUser.getId());
        roster.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setLotteryFlag((byte) 0);
        roster.setPhone(cmd.getPhone());
        roster.setRealName(user.getNickName());
        roster.setGender(user.getGender());
        roster.setCommunityName(cmd.getCommunityName());
        roster.setSourceFlag(ActivityRosterSourceFlag.BACKEND_ADD.getCode());
        roster.setStatus(ActivityRosterStatus.NORMAL.getCode());
        if (cmd.getFormId() != null) {
            roster.setFormId(cmd.getFormId());
        }
        return roster;
	}

	private User getUserFromPhone(String phone) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
		if (userIdentifier != null) {
			User user = userProvider.findUserById(userIdentifier.getOwnerUid());
			if (user != null) {
				return user;
			}
		}
		User user = new User();
		user.setId(0L);
		user.setExecutiveTag((byte) 0);
		return user;
	}

    private User getUserFromPhoneWithNamespaceId(String phone, Integer namespaceId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
        if (userIdentifier != null) {
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            if (user != null) {
                return user;
            }
        }
        User user = new User();
        user.setId(0L);
        user.setExecutiveTag((byte) 0);
        return user;
    }

	@Override
	public SignupInfoDTO updateSignupInfo(UpdateSignupInfoCommand cmd) {
		ActivityRoster activityRoster = (ActivityRoster)this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY_ROSTER.getCode()+cmd.getId()).enter(()-> {
	        return (ActivityRoster)dbProvider.execute((status) -> {
	        	ActivityRoster roster = activityProvider.findRosterById(cmd.getId());
	        	if (cmd.getCheckinFlag().byteValue() != roster.getCheckinFlag().byteValue()) {
	        		updateActivityCheckin(roster, cmd.getCheckinFlag());
	        		roster.setCheckinFlag(cmd.getCheckinFlag());
	        		roster.setCheckinUid(UserContext.current().getUser().getId());
	    		}
	        	activityProvider.updateRoster(roster);
                List<GeneralFormVal> list = this.generalFormValProvider.queryGeneralFormVals(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP,roster.getId());
                if (CollectionUtils.isEmpty(list)) {
                    LOGGER.error("GeneralFormVal is null.");
                    throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "GeneralFormVal is null.");
                }
                Long formOriginId = list.get(0).getFormOriginId();
                this.generalFormValProvider.deleteGeneralFormVals(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP,roster.getId());
                addGeneralFormValuesCommand addGeneralFormValuesCommand = new addGeneralFormValuesCommand();
                addGeneralFormValuesCommand.setGeneralFormId(formOriginId);
                addGeneralFormValuesCommand.setGeneralFormVersion(list.get(0).getFormVersion());
                addGeneralFormValuesCommand.setSourceId(roster.getId());
                addGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
                addGeneralFormValuesCommand.setValues(cmd.getValues());
                this.generalFormService.addGeneralFormValues(addGeneralFormValuesCommand);
	        	return roster;
	        });
		}).first();
		return convertActivityRoster(activityRoster, null);
	}

	private void updateActivityCheckin(ActivityRoster roster, Byte toCheckinFlag) {
		// 签到的话活动表对应字段+1
		if (CheckInStatus.fromCode(roster.getCheckinFlag()) == CheckInStatus.UN_CHECKIN && CheckInStatus.fromCode(toCheckinFlag) == CheckInStatus.CHECKIN) {
			this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
				Activity activity = activityProvider.findActivityById(roster.getActivityId());
				activity.setCheckinAttendeeCount(activity.getCheckinAttendeeCount()
		                + (roster.getAdultCount() + roster.getChildCount()));
		        if (roster.getFamilyId() != null) {
		        	activity.setCheckinFamilyCount(activity.getCheckinFamilyCount() + 1);
		        }
		        activityProvider.updateActivity(activity);
				return null;
			});
			//取消签到的话活动表对应字段-1
		}else if (CheckInStatus.fromCode(roster.getCheckinFlag()) == CheckInStatus.CHECKIN && CheckInStatus.fromCode(toCheckinFlag) == CheckInStatus.UN_CHECKIN) {
			this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()+roster.getActivityId()).enter(()-> {
				int result = 0;
				Activity activity = activityProvider.findActivityById(roster.getActivityId());
				activity.setCheckinAttendeeCount((result = activity.getCheckinAttendeeCount() - (roster.getAdultCount() + roster.getChildCount())) < 0 ? 0 : result);
		        if (roster.getFamilyId() != null){
		        	activity.setCheckinFamilyCount((result = activity.getCheckinFamilyCount() - 1) < 0 ? 0 : result);
		        }
		        activityProvider.updateActivity(activity);
				return null;
			});
		}
	}

	@Override
	public ImportSignupInfoResponse importSignupInfo(ImportSignupInfoCommand cmd, MultipartFile[] files) {
		ImportSignupInfoResponse result = new ImportSignupInfoResponse();
		this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
			User user = UserContext.current().getUser();
			Activity activity = checkActivityExist(cmd.getActivityId());
			List<List<PostApprovalFormItem>> values = new ArrayList<>();
            GetTemplateBySourceIdCommand getTemplateBySourceIdCommand = ConvertHelper.convert(cmd, GetTemplateBySourceIdCommand.class);
            getTemplateBySourceIdCommand.setOwnerId(activity.getId());
            GeneralFormDTO form = this.generalFormService.getTemplateBySourceId(getTemplateBySourceIdCommand);
            List<ActivityRoster> rosters = getRostersFromExcel(files[0], result, activity.getId(),values, form);
//			List<ActivityRoster> rosters = filterExistRoster(cmd.getActivityId(), rostersTemp);

			//检查是否超过报名人数限制, add by tt, 20161012
	        if (activity.getMaxQuantity() != null && activity.getSignupAttendeeCount().intValue() + rosters.size() > activity.getMaxQuantity().intValue()) {
	        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                    ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
						"beyond contraint quantity!");
			}

			dbProvider.execute(s->{
			    for (int i=0;i < rosters.size(); i++) {
                    ActivityRoster r = rosters.get(i);
                    ActivityRoster oldRoster = activityProvider.findRosterByPhoneAndActivityId(activity.getId(), r.getPhone(), ActivityRosterStatus.NORMAL.getCode());
                    if(oldRoster != null){
                        if (form != null) {
                            //表单数据更新时，先删除旧的数据，在增加新的数据.
                            this.generalFormValProvider.deleteGeneralFormVals(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP,oldRoster.getId());

                            addGeneralFormValuesCommand addGeneralFormValuesCommand = new addGeneralFormValuesCommand();
                            addGeneralFormValuesCommand.setGeneralFormId(form.getFormOriginId());
                            addGeneralFormValuesCommand.setSourceId(oldRoster.getId());
                            addGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
                            addGeneralFormValuesCommand.setValues(values.get(i));
                            addGeneralFormValuesCommand.setGeneralFormVersion(form.getFormVersion());
                            this.generalFormService.addGeneralFormValues(addGeneralFormValuesCommand);
                        }
                        activityProvider.updateRoster(oldRoster);
                    }else {
                        r.setConfirmUid(user.getId());
                        r.setActivityId(cmd.getActivityId());
                        r.setStatus(ActivityRosterStatus.NORMAL.getCode());
                        activityProvider.createActivityRoster(r);
                        if (form != null) {
                            //报名对接表单，添加表单值数据
                            addGeneralFormValuesCommand addGeneralFormValuesCommand = new addGeneralFormValuesCommand();
                            addGeneralFormValuesCommand.setGeneralFormId(form.getFormOriginId());
                            addGeneralFormValuesCommand.setSourceId(r.getId());
                            addGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
                            addGeneralFormValuesCommand.setValues(values.get(i));
                            addGeneralFormValuesCommand.setGeneralFormVersion(form.getFormVersion());

                            this.generalFormService.addGeneralFormValues(addGeneralFormValuesCommand);
                        }
                        // 报名活动事件
                        LocalEventBus.publish(event -> {
                            LocalEventContext context = new LocalEventContext();
                            context.setUid(r.getUid());
                            context.setNamespaceId(UserContext.getCurrentNamespaceId());
                            event.setContext(context);

                            event.setEntityType(EhActivities.class.getSimpleName());
                            event.setEntityId(activity.getId());
                            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ENTER.dft());
                        });
					// 注册成功事件 add by jiarui
                        LocalEventBus.publish(event -> {
                            LocalEventContext localEventcontext = new LocalEventContext();
                            localEventcontext.setUid(UserContext.currentUserId());
                            localEventcontext.setNamespaceId(activity.getNamespaceId());
                            event.setContext(localEventcontext);
                            Map<String, Object> map = new HashMap<>();
                            map.put(EntityType.ACTIVITY_ROSTER.getCode(), r);
                            map.put("categoryId", activity.getCategoryId());
                            event.setParams(map);
                            event.setEntityType(EntityType.ACTIVITY_ROSTER.getCode());
                            event.setEntityId(UserContext.currentUserId());
                            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft());
                        });}
				}
				activity.setSignupAttendeeCount(activity.getSignupAttendeeCount() + rosters.size() - result.getUpdate());
	            activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + rosters.size() - result.getUpdate());
	            activityProvider.updateActivity(activity);

				return null;
			});

			LOGGER.info("importSignupInfo end activityId: " + activity.getId() + " userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

			return null;
		});

		return result;
	}

	//有新的信息，则填充新的信息
	private void getNewForImport(ActivityRoster oldRoster, ActivityRoster importRoster){

    	//真实姓名
    	if(!StringUtils.isEmpty(importRoster.getRealName())){
    		oldRoster.setRealName(importRoster.getRealName());
		}

		//性别
		if(importRoster.getGender() != null && importRoster.getGender().byteValue() != UserGender.UNDISCLOSURED.getCode()){
			oldRoster.setGender(importRoster.getGender());
		}

		//园区
		if(!StringUtils.isEmpty(importRoster.getCommunityName())){
			oldRoster.setCommunityName(importRoster.getCommunityName());
		}

		//公司
		if(!StringUtils.isEmpty(importRoster.getOrganizationName())){
			oldRoster.setOrganizationName(importRoster.getOrganizationName());
			oldRoster.setOrganizationId(importRoster.getOrganizationId());
		}

		//职位
		if(!StringUtils.isEmpty(importRoster.getPosition())){
			oldRoster.setPosition(importRoster.getPosition());
		}

		//是否高管
		if(importRoster.getLeaderFlag() != null){
			oldRoster.setLeaderFlag(importRoster.getLeaderFlag());
		}

		//邮件
		if(!StringUtils.isEmpty(importRoster.getEmail())){
			oldRoster.setEmail(importRoster.getEmail());
		}

	}

	private List<ActivityRoster> getRostersFromExcel(MultipartFile file, ImportSignupInfoResponse result, Long activityId, List<List<PostApprovalFormItem>> values, GeneralFormDTO form) {
		@SuppressWarnings("rawtypes")
		List<ImportSignupErrorDTO> errorLists = new ArrayList<>();
		ArrayList rows = processorExcel(file);
		List<ActivityRoster> rosters = new ArrayList<>();
		//此处添加陈宫失败数，因为过着这个方法后就拿不到总数和失败数了。 add by yajun 20170827
		result.setTotal(0);
		result.setFail(0);
		result.setSuccess(0);
		result.setUpdate(0);
		if(rows == null || rows.size() < 3){
			return rosters;
		}

        RowResult titleRow = (RowResult)rows.get(1);
		Integer size = titleRow.getCells().size();
		//Excel模板从第三行开始 edit by yanjun 20170829
		for(int i=2, len=rows.size(); i<len; i++) {

			//总数加一
			result.setTotal(result.getTotal() + 1);

			RowResult row = (RowResult) rows.get(i);
			//检验Excel的数据   add by yanjun 20170815
			ImportSignupErrorDTO rosterError = checkExcelRoster(rosters, row, activityId);
			if(rosterError != null){
				rosterError.setRowNum(i);

				//失败或者更新加一
				if(rosterError.getHandleType() == null || rosterError.getHandleType().byteValue() == SignupErrorHandleType.SKIP.getCode()){
					result.setFail(result.getFail() + 1);
					errorLists.add(rosterError);
					continue;
				}else {
					result.setUpdate(result.getUpdate() + 1);
				}

			}

			//成功加一
			result.setSuccess(result.getSuccess() + 1);

			// 这里是的getUserFromPhone有点小问题的，id为0的用户在我们系统中是存在的，可能会有某些地方显示异常。
			// 尝试过将uid设置成null，导致了app查询报名用户和后台导出报名出现了nullpointexception，还有报名、确认、取消、签到、支付、退款、活动详情及报名统计多个接口等没测试。
			// 素我无能为力，暂时保持原样。
            List<ActivityRoster> rosterList = this.activityProvider.listRosters(activityId,ActivityRosterStatus.NORMAL);
            String phone = row.getA();
            if (!CollectionUtils.isEmpty(rosterList)) {
                if (rosterList.get(0).getFormId() != null) {
                    phone = row.getB();
                }
            }
			User user = getUserFromPhone(phone.trim());
			ActivityRoster roster = new ActivityRoster();
			roster.setUuid(UUID.randomUUID().toString());
			roster.setUid(user.getId());
	        roster.setAdultCount(1);
	        roster.setChildCount(0);
	        roster.setCheckinFlag(CheckInStatus.UN_CHECKIN.getCode());
	        roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
	        roster.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	        roster.setLotteryFlag((byte) 0);
            roster.setSourceFlag(ActivityRosterSourceFlag.BACKEND_ADD.getCode());
            roster.setRealName(user.getNickName());
            roster.setPhone(phone.trim());
            if (form != null) {
                List<PostApprovalFormItem> postApprovalFormItems = new ArrayList<>();
                LOGGER.info("cell length = {}", size);
                for (int j=0;j<size;j++) {
                    PostApprovalFormItem postApprovalFormItem = ConvertHelper.convert(form.getFormFields().get(j),PostApprovalFormItem.class);
                    String value = row.getCells().get(ArchivesUtil.GetExcelLetter(j + 1)) != null ? row.getCells().get(ArchivesUtil.GetExcelLetter(j + 1)) : "";
                    LOGGER.info("value = {}",value);
                    postApprovalFormItem.setFieldValue(getStrTrim(value));
                    postApprovalFormItems.add(postApprovalFormItem);
                }
                values.add(postApprovalFormItems);
            }

            rosters.add(roster);
		}

		//保存错误信息
		if(errorLists != null && errorLists.size() > 0){


			CsFileLocationDTO fileLocationDTO = writeLogToExcel(file, errorLists);
			result.setFileLocation(fileLocationDTO);
//			Long jobId = addActivityRosterErrorLog(errorLists);
//			result.setJobId(jobId);
		}

		return rosters;
	}

	private CsFileLocationDTO writeLogToExcel(MultipartFile file, List<ImportSignupErrorDTO> errorLists){
		try {
			InputStream is = file.getInputStream();


			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheetAt(0);

			int errorCellColumn = sheet.getRow(1).getLastCellNum() + 1;

			sheet.setColumnWidth(errorCellColumn, 5000);
			boolean shiftFlag;
			for(int i = sheet.getLastRowNum(); i>=2; i--){
				shiftFlag = true;
				for(int j=0; j<errorLists.size(); j++){
					if(i == errorLists.get(j).getRowNum()){
						shiftFlag = false;
						Row row = sheet.getRow(i);
						row.getCell(errorCellColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(errorLists.get(j).getDescription());
						break;
					}
				}

				if(shiftFlag){
					removeRow(sheet, i);
				}
			}

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			workbook.write(bos);

			InputStream ins = new ByteArrayInputStream(os.toByteArray());
			String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
			String name = "importErrorLog_" + String.valueOf(System.currentTimeMillis()) + ".xls";
			UploadCsFileResponse re = contentServerService.uploadFileToContentServer(ins, name, token);
			if(re.getErrorCode() == 0){
				return re.getResponse();
			}
		}catch (Exception ex){
			LOGGER.error("Write log to Excel Error");
		}
		return null;
	}

	private void removeRow(XSSFSheet sheet, int rowIndex) {
		int lastRowNum = sheet.getLastRowNum();
		if (rowIndex >= 0 && rowIndex < lastRowNum)
			sheet.shiftRows(rowIndex + 1, lastRowNum, -1);//将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
		if (rowIndex == lastRowNum) {
			XSSFRow removingRow = sheet.getRow(rowIndex);
			if (removingRow != null)
				sheet.removeRow(removingRow);
		}
	}

//	private Long addActivityRosterErrorLog(List<ActivityRosterError> list){
//    	Long jobId = System.currentTimeMillis();
//    	Long uid = UserContext.currentUserId();
//    	list.forEach(r -> {
//    		r.setJobId(jobId);
//    		r.setCreateUid(uid);
//			activityProvider.createActivityRosterError(r);
//		});
//
//    	return jobId;
//	}

	private ImportSignupErrorDTO checkExcelRoster(List<ActivityRoster> newRosters, RowResult row, Long activityId){

		ImportSignupErrorDTO rosterError = new ImportSignupErrorDTO();
		String  locale = UserContext.current().getUser().getLocale();
		String scope = ActivityLocalStringCode.SCOPE;

		List<ActivityRoster> rosters = this.activityProvider.listRosters(activityId,ActivityRosterStatus.NORMAL);
		String phone = row.getA();
		if (!CollectionUtils.isEmpty(rosters)) {
		    if (rosters.get(0).getFormId() != null) {
		        phone = row.getB();
            }
        }
		String errorString = "";
		//手机不能为空
		if (org.apache.commons.lang.StringUtils.isBlank(phone)) {
			String code = String.valueOf(ActivityLocalStringCode.ACTIVITY_PHONE_EMPTY);
			errorString = localeStringService.getLocalizedString(scope, code, locale, "The phone number is empty");
			rosterError.setDescription(errorString);
			rosterError.setHandleType(SignupErrorHandleType.SKIP.getCode());
			return rosterError;
		}
		//手机格式简单检验
		if (phone == null || phone.trim().length() != 11 || !phone.trim().startsWith("1")) {
			String code = String.valueOf(ActivityLocalStringCode.ACTIVITY_INVALID_PHONE);
			errorString = localeStringService.getLocalizedString(scope, code, locale, "Invalid phone number");
			rosterError.setDescription(errorString);
			rosterError.setHandleType(SignupErrorHandleType.SKIP.getCode());
			return rosterError;
		}

		//检查Excel内是否存在重复
		for(int i=0; i< newRosters.size(); i++){
			if(newRosters.get(i).getPhone().equals(phone)){
				String code = String.valueOf(ActivityLocalStringCode.ACTIVITY_REPEAT_ROSTER_IN_EXCEL);
				errorString = localeStringService.getLocalizedString(scope, code, locale, "Repeat roster in this Excel, checked with phone");
				rosterError.setDescription(errorString);
				rosterError.setHandleType(SignupErrorHandleType.SKIP.getCode());
				return rosterError;
			}
		}

		//检查是否已经报过名
		ActivityRoster oldRoster = activityProvider.findRosterByPhoneAndActivityId(activityId, phone, ActivityRosterStatus.NORMAL.getCode());
		if(oldRoster != null){
			String code = String.valueOf(ActivityLocalStringCode.ACTIVITY_REPEAT_ALREADY_EXISTS_UPDATE);
			errorString = localeStringService.getLocalizedString(scope, code, locale, "The roster already exists, update now");
			rosterError.setDescription(errorString);
			rosterError.setHandleType(SignupErrorHandleType.UPDATE.getCode());
			return rosterError;
		}

    	return null;
	}
	
//	private List<ActivityRoster> filterExistRoster(Long activityId, List<ActivityRoster> rosters){
//		List<ActivityRoster> newRosters = new ArrayList<ActivityRoster>();
//		if(rosters == null){
//			return newRosters;
//		}
//
//		//筛选重复数据，1、数据库不能有重复的，2、自己不能有重复的。
//		for(int i= 0; i< rosters.size(); i++){
//			ActivityRoster oldRoster = activityProvider.findRosterByPhoneAndActivityId(activityId, rosters.get(i).getPhone(), ActivityRosterStatus.NORMAL.getCode());
//			if(oldRoster != null){
//				continue;
//			}
//
//			boolean oldFlag = false;
//			for(int j =0; j<newRosters.size(); j++){
//				if(rosters.get(i).getPhone().equals(newRosters.get(j).getPhone())){
//					oldFlag = true;
//					break;
//				}
//			}
//
//			if(oldFlag){
//				continue;
//			}
//			newRosters.add(rosters.get(i));
//		}
//		return newRosters;
//	}
	
	/**
	 * 防止nullPointException
	 * @param str
	 * @return
	 */
	private String getStrTrim(String str){
		if(str == null){
			return null;
		}else{
			return str.trim();
		}
	}

	private Byte getLeaderFlag(String leaderFlag) {
		if ("是".equals(leaderFlag)) {
			return TrueOrFalseFlag.TRUE.getCode();
		}
		return TrueOrFalseFlag.FALSE.getCode();
	}

	private Byte getGender(String gender) {
		if ("男".equals(gender)) {
			return UserGender.MALE.getCode();
		}else if ("女".equals(gender)) {
			return UserGender.FEMALE.getCode();
		}
		return UserGender.UNDISCLOSURED.getCode();
	}

	@SuppressWarnings("rawtypes")
	private ArrayList processorExcel(MultipartFile file) {
		try {
            return PropMrgOwnerHandler.processorExcel(file.getInputStream());
        } catch (IOException e) {
			LOGGER.error("Process excel error.", e);
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Process excel error.");
        }
	}
	
	private Activity checkActivityExist(Long activityId) {
		Activity activity = activityProvider.findActivityById(activityId);
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", activityId);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + activityId);
        }
        return activity;
	}

	@Override
	public ListSignupInfoResponse listSignupInfo(ListSignupInfoCommand cmd) {
		Activity activity = checkActivityExist(cmd.getActivityId());
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		Integer pageOffset = 1;
        if (cmd.getPageOffset() != null){
        	pageOffset = cmd.getPageOffset();
        }
        Integer offset =  (int) ((pageOffset - 1 ) * pageSize);
        
        //默认排除创建者，当取全部的时候加上创建者   add by yanju 20170519
        Long excludeUserId  = activity.getCreatorUid();
        if(cmd.getStatus() == null){
        	excludeUserId = null;
        }
        
		List<ActivityRoster> rosters = activityProvider.listActivityRoster(cmd.getActivityId(), excludeUserId, cmd.getStatus(), cmd.getCancelStatus(), offset, pageSize+1);
		Integer nextPageOffset = null;
		if (rosters.size() > pageSize) {
			rosters.remove(rosters.size()-1);
			nextPageOffset = pageOffset + 1;
		}
		
		Integer unConfirmCount = activityProvider.countActivityRoster(cmd.getActivityId(), new Integer(ConfirmStatus.UN_CONFIRMED.getCode()));
		
		return new ListSignupInfoResponse(nextPageOffset, unConfirmCount, rosters.stream().map(r->convertActivityRoster(r,activity)).collect(Collectors.toList()));
	}

    @Override
    public ListSignupInfoByOrganizationIdResponse listSignupInfoByOrganizationId(Long organizationId, Integer namespaceId, Long pageAnchor, int pageSize) {
        ListSignupInfoByOrganizationIdResponse response = new ListSignupInfoByOrganizationIdResponse();
	    List<ActivityRoster> list = this.activityProvider.listActivityRosterByOrganizationId(organizationId, namespaceId, pageAnchor, pageSize);
	    if (list.size() > pageSize){
            ActivityRoster remove = list.remove(list.size() -1);
            response.setNextAnchor(remove.getId());
        }
        List<SignupInfoDTO> signupInfoDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
	        for (ActivityRoster activityRoster : list) {
                Activity activity = checkActivityExist(activityRoster.getActivityId());
                SignupInfoDTO signupInfoDTO = convertActivityRoster(activityRoster, activity);
                signupInfoDTOList.add(signupInfoDTO);
            }
        }
        response.setSignupInfoDTOs(signupInfoDTOList);
        return response;
    }

    @Override
	public void exportSignupInfo(ExportSignupInfoCommand cmd, HttpServletResponse response) {
		Activity activity = checkActivityExist(cmd.getActivityId());
		List<ActivityRoster> rosters = new ArrayList<ActivityRoster>();
		List<ActivityRoster> rostersConfirms = activityProvider.listActivityRoster(cmd.getActivityId(), null, null, 1, 0, 100000);
		List<ActivityRoster> rostersRejects = activityProvider.listActivityRoster(cmd.getActivityId(), null, null, 2, 0, 100000);
		List<ActivityRoster> rostersUnConfirms = activityProvider.listActivityRoster(cmd.getActivityId(), null, null, 0, 0, 100000);
		
		rosters.addAll(rostersConfirms);
		rosters.addAll(rostersRejects);
		rosters.addAll(rostersUnConfirms);
		
		if (rosters.size() > 0) {
			List<SignupInfoDTO> signupInfoDTOs = rosters.stream().map(r->convertActivityRosterForExcel(r, activity)).collect(Collectors.toList());
			String fileName = String.format("报名信息_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "报名信息");
			List<String> propertyNames = new ArrayList<String>(Arrays.asList("order", "phone", "nickName", "realName", "genderText", "organizationName", "position", "leaderFlagText", "email",
					"signupTime", "sourceFlagText", "signupStatusText"));
			List<String> titleNames = new ArrayList<String>(Arrays.asList("序号", "手机号", "用户昵称", "真实姓名", "性别", "公司", "职位", "是否高管", "邮箱", "报名时间", "报名来源", "报名状态"));
			List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(10, 20, 20, 20, 10, 20, 20, 10, 20, 20, 20, 20));
			
//			if (ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
//				propertyNames.add("confirmFlagText");
//				titleNames.add("报名确认");
//				titleSizes.add(20);
//			}

			if(activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()){
				propertyNames.add("payAmount");
				titleNames.add("已付金额");
				titleSizes.add(10);

				propertyNames.add("refundAmount");
				titleNames.add("已退金额");
				titleSizes.add(10);
			}


			if (CheckInStatus.fromCode(activity.getSignupFlag()) == CheckInStatus.CHECKIN) {
				propertyNames.add("checkinFlagText");
				titleNames.add("是否签到");
				titleSizes.add(10);
			}
			
			if(signupInfoDTOs.size() > 0){
				signupInfoDTOs.get(0).setOrder("创建者");
			}
			for(int i=1; i<signupInfoDTOs.size(); i++){
				signupInfoDTOs.get(i).setOrder(String.valueOf(i));
			}
			excelUtils.setNeedSequenceColumn(false);
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, signupInfoDTOs);
		}else {
			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_NO_ROSTER, "no roster in this activity");
		}
	}
	
	private SignupInfoDTO convertActivityRosterForExcel(ActivityRoster roster, Activity activity) {
		SignupInfoDTO signupInfoDTO = convertActivityRoster(roster, activity);
		signupInfoDTO.setTypeText(UserAuthFlag.fromCode(signupInfoDTO.getType())==null?UserAuthFlag.NOT_REGISTER.getText():UserAuthFlag.fromCode(signupInfoDTO.getType()).getText());
		signupInfoDTO.setSourceFlagText(ActivityRosterSourceFlag.fromCode(signupInfoDTO.getSourceFlag()).getText());
		//signupInfoDTO.setConfirmFlagText(ConfirmStatus.fromCode(signupInfoDTO.getConfirmFlag())==null?ConfirmStatus.UN_CONFIRMED.getText():ConfirmStatus.fromCode(signupInfoDTO.getConfirmFlag()).getText());
		String configFlagText = "";
		if(ConfirmStatus.CONFIRMED.getCode().equals(signupInfoDTO.getConfirmFlag())){
			configFlagText = ConfirmStatus.CONFIRMED.getText();
		}else if(ConfirmStatus.REJECT.getCode().equals(signupInfoDTO.getConfirmFlag())){
			configFlagText = ConfirmStatus.REJECT.getText();
		}else{
			configFlagText = ConfirmStatus.UN_CONFIRMED.getText();
		}
		signupInfoDTO.setConfirmFlagText(configFlagText);
		
		signupInfoDTO.setCheckinFlagText(CheckInStatus.fromCode(signupInfoDTO.getCheckinFlag())==null?CheckInStatus.UN_CHECKIN.getText():CheckInStatus.fromCode(signupInfoDTO.getCheckinFlag()).getText());

		// 增加 报名状态信息
		if(roster.getStatus() == null ){
			signupInfoDTO.setSignupStatusText("未知");
		}else if(roster.getStatus() == ActivityRosterStatus.CANCEL.getCode()){
			signupInfoDTO.setSignupStatusText("已取消");
		}else if(roster.getStatus() == ActivityRosterStatus.REJECT.getCode()){
			signupInfoDTO.setSignupStatusText("已驳回");
		}else if(roster.getStatus() == ActivityRosterStatus.NORMAL.getCode()){
			if(roster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode()){
				signupInfoDTO.setSignupStatusText("已确认");
			}else if(roster.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode()){
				signupInfoDTO.setSignupStatusText("待确认");
			}
		}

		return signupInfoDTO;
	}

	@Override
	public void deleteSignupInfo(DeleteSignupInfoCommand cmd) {
        Tuple<ActivityRoster, Boolean> tuple = coordinationProvider.getNamedLock(
                CoordinationLocks.UPDATE_ACTIVITY_ROSTER.getCode() + cmd.getId()).enter(() -> {
            return (ActivityRoster) dbProvider.execute((status) -> {
                ActivityRoster roster = activityProvider.findRosterById(cmd.getId());
                activityProvider.deleteRoster(roster);
                updateActivityWhenDeleteRoster(roster);
                return roster;
            });
        });

        // 取消报名事件
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(tuple.first().getUid());
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhActivities.class.getSimpleName());
            event.setEntityId(tuple.first().getActivityId());
            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ENTER_CANCEL.dft());
        });
	}

	private void updateActivityWhenDeleteRoster(ActivityRoster roster) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
			int total = roster.getAdultCount() + roster.getChildCount();
			int result = 0;
			Activity activity = activityProvider.findActivityById(roster.getActivityId());
			
			if (ConfirmStatus.fromCode(roster.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
				//因为使用新规则已报名=已确认 。  add by yanjun 20170503  start
	            //1、signup：不需要确认的话，立刻添加到已报名人数；2、conform：添加到已报名人数；3、reject：不处理；4、cancel、delete：如果已确认则减，如果未确认则不处理
				//此句由if外搬到if内
				activity.setSignupAttendeeCount(activity.getSignupAttendeeCount() - total);
				//因为使用新规则已报名=已确认。  add by yanjun 20170503   end
				activity.setConfirmAttendeeCount((result = activity.getConfirmAttendeeCount() - total) < 0 ? 0 : result);
				if (roster.getFamilyId() != null) {
					activity.setConfirmFamilyCount((result = activity.getConfirmFamilyCount() -1) < 0 ? 0 : result);
				}
			}
			if (CheckInStatus.fromCode(roster.getCheckinFlag()) == CheckInStatus.CHECKIN) {
				activity.setCheckinAttendeeCount((result = activity.getCheckinAttendeeCount() - total) < 0 ? 0 : result);
				if (roster.getFamilyId() != null) {
					activity.setCheckinFamilyCount((result = activity.getCheckinFamilyCount() - 1) < 0 ? 0 : result);
				}
			}
			activityProvider.updateActivity(activity);
			return null;
		});
	}

	private void sendMessageCode(Long uid, String locale, Map<String, String> map, int code, Map<String, String> meta) {
    	
        String scope = ActivityNotificationTemplateCode.SCOPE;
        
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        
        
        sendMessageToUser(uid, notifyTextForOther, meta);
    }
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    private ActivityRoster createRoster(ActivitySignupCommand cmd, User user, Activity activity) {
        ActivityRoster roster = ConvertHelper.convert(cmd, ActivityRoster.class);
        roster.setFamilyId(user.getAddressId());
        roster.setUid(user.getId());
        roster.setUuid(UUID.randomUUID().toString());
        roster.setActivityId(activity.getId());
        roster.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        roster.setConfirmFamilyId(user.getAddressId());
        roster.setCommunityName(cmd.getCommunityName());
        if(ConfirmStatus.UN_CONFIRMED == ConfirmStatus.fromCode(activity.getConfirmFlag())){
        	roster.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
        }
        roster.setStatus(ActivityRosterStatus.NORMAL.getCode());
        if (cmd.getFormId() != null) {
            roster.setFormId(cmd.getFormId());
        }
        // 添加活动报名时新增的姓名、职位等信息, add by tt, 20170228
        addAdditionalInfo(roster, user, activity);

		// 增加来自微信端报名的来源   edit by yanjun 20170720
		if(cmd.getSignupSourceFlag() != null && cmd.getSignupSourceFlag().byteValue() == ActivityRosterSourceFlag.WECHAT.getCode()){
			roster.setSourceFlag(ActivityRosterSourceFlag.WECHAT.getCode());
		}
        
        return roster;
    }

    private void addAdditionalInfo(ActivityRoster roster, User user, Activity activity) {
    	// 活动3.0.0页面可能会传来一些数据，优先选择页面传来的数据。 if条件 add by yanjun 20170502
    	SignupInfoDTO signupInfoDTO = verifyPerson(activity.getNamespaceId(), user);
    	if(roster.getPhone() == null){
    		roster.setPhone(signupInfoDTO.getPhone());
    	}
    	roster.setGender(user.getGender());
    	//不需要补充以下数据,通过表单传入.
//    	roster.setCommunityName(signupInfoDTO.getCommunityName());

    	//产品沟通不默认设置公司和职位，因为小区场景默认是没有公司和职位的  add by yanjun 20180515
//    	if(roster.getOrganizationName() == null){
//    		roster.setOrganizationName(signupInfoDTO.getOrganizationName());
//    	}
//    	if(roster.getPosition() == null){
//    		roster.setPosition(signupInfoDTO.getPosition());
//    	}
//    	if(roster.getEmail() == null){
//    		roster.setEmail(signupInfoDTO.getEmail());
//    	}
//    	roster.setLeaderFlag(signupInfoDTO.getLeaderFlag());
    	roster.setSourceFlag(ActivityRosterSourceFlag.SELF.getCode());
	}
    
    private SignupInfoDTO verifyPerson(Integer namespaceId, User user) {
    	SignupInfoDTO signupInfoDTO = new SignupInfoDTO();
    	List<UserIdentifier> userIdentifiers = userProvider.listUserIdentifiersOfUser(user.getId());
    	if (userIdentifiers != null && userIdentifiers.size() > 0) {
    		signupInfoDTO.setPhone(userIdentifiers.get(0).getIdentifierToken());
		}
    	signupInfoDTO.setNickName(user.getNickName());

    	return signupInfoDTO;
    }

	@Override
	public SignupInfoDTO vertifyPersonByPhone(VertifyPersonByPhoneCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getPhone());
		if (identifier != null) {
			User user = userProvider.findUserById(identifier.getOwnerUid());
			if (user != null) {
				return verifyPerson(namespaceId, user);
			}
		}
		return null;
	}

	private OrganizationMember findAnyOrganizationMember(Integer namespaceId, Long userId) {
		OrganizationMember organizationMember = organizationProvider.findAnyOrganizationMemberByNamespaceIdAndUserId(namespaceId, userId, OrganizationGroupType.JOB_POSITION.getCode());
		if (organizationMember == null) {
			organizationMember = organizationProvider.findAnyOrganizationMemberByNamespaceIdAndUserId(namespaceId, userId, OrganizationGroupType.ENTERPRISE.getCode());
		}
		return organizationMember;
	}

	@Override
    public ActivityDTO cancelSignup(ActivityCancelSignupCommand cmd) {

		ActivityDTO resDto =  (ActivityDTO)this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
	        return (ActivityDTO)dbProvider.execute((status) -> {
				LOGGER.warn("------- cancelSignup start ");
	        	long cancelStartTime = System.currentTimeMillis();
	        	//cmd中用户Id，该字段当前仅用于定时取消订单时无法从UserContext.current中获取用户
	        	User user = null;
	        	if(cmd.getUserId() != null){
	        		user = userProvider.findUserById(cmd.getUserId());
	        	}else{
	        		user = UserContext.current().getUser();
	        	}
	             Activity activity = activityProvider.findActivityById(cmd.getActivityId());
	             if (activity == null) {
	                 LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
	                 throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                         ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
	             }

				LOGGER.info("cancelSignup start activityId: " + activity.getId() + " userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

	             //手动取消 要检查过期时间  add by yanjun 20170519
	             if(cmd.getCancelType() == null || cmd.getCancelType().byteValue()== ActivityCancelType.HAND.getCode()){
	            	 if(activity.getSignupEndTime() != null && activity.getSignupEndTime().getTime() < DateHelper.currentGMTTime().getTime()){
	            		 LOGGER.error("handle activity error, Can not cancel cause after signupEndTime. id={}", cmd.getActivityId());
		                 throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
		                         ActivityServiceErrorCode.ERROR_CANCEL_BEYOND_SIGNUP_TIME, "Fail, Cause after signupEndTime");
	            	 }
	             }
	             
	             Post post = forumProvider.findPostById(activity.getPostId());
	             if (post == null) {
	                 LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
	                 throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
	                         ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
	             }
	             
	             //如果有退款，先退款再取消订单
	             this.signupOrderRefund(activity, user.getId());
	             
	             activityProvider.cancelSignup(activity, user.getId(), getFamilyId());
	             if (activity.getGroupId() != null) {
	                 LeaveGroupCommand leaveCmd = new LeaveGroupCommand();
	                 leaveCmd.setGroupId(activity.getGroupId());
	                 //remove from group or not
	                // groupService.leaveGroup(leaveCmd);
	             }
//	             Post p = createPost(user.getId(), post, null, "");
////	             p.setContent(configurationProvider.getValue(CANCEL_AUTO_COMMENT, ""));
//	             p.setContent(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
//	                         String.valueOf(ActivityLocalStringCode.ACTIVITY_CANCEL), UserContext.current().getUser().getLocale(), ""));
//	             forumProvider.createPost(p);
	             ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
                 ActivityRoster cancelRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.CANCEL.getCode());
                 if (cancelRoster != null && cancelRoster.getPayFlag() != null && cancelRoster.getPayFlag().equals(ActivityRosterPayFlag.REFUND.getCode())) {
                     dto.setUserPayFlag(ActivityRosterPayFlag.REFUND.getCode());
                 }
	             dto.setActivityId(activity.getId());
	             dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
	             dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
	             dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
	             dto.setEnrollUserCount(activity.getSignupAttendeeCount());
	             dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
	             dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
	             dto.setProcessStatus(getStatus(activity).getCode());
	             dto.setFamilyId(activity.getCreatorFamilyId());
                 ActivityRoster creator = activityProvider.findRosterByUidAndActivityId(activity.getId(), activity.getCreatorUid(), ActivityRosterStatus.NORMAL.getCode());
                 if (creator.getFormId() == null) {
                     dto.setSignupFormFlag(TrueOrFalseFlag.FALSE.getCode());
                  }else {
                        dto.setSignupFormFlag(TrueOrFalseFlag.TRUE.getCode());
                 }
	             dto.setGroupId(activity.getGroupId());
	             dto.setStartTime(activity.getStartTime().toString());
	             dto.setStopTime(activity.getEndTime().toString());
	             dto.setSignupEndTime(getSignupEndTime(activity).toString());
	             dto.setForumId(post.getForumId());
	             dto.setUserActivityStatus(ActivityStatus.UN_SIGNUP.getCode());
	             dto.setPosterUrl(getActivityPosterUrl(activity));
	             fixupVideoInfo(dto);//added by janson
	             
	             //Send message to creator
	             Map<String, String> map = new HashMap<String, String>();
	             map.put("userName", user.getNickName());
	             map.put("postName", activity.getSubject());
	             sendMessageCode(activity.getCreatorUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_CANCEL_TO_CREATOR, null);
	             long cancelEndTime = System.currentTimeMillis();
	             LOGGER.debug("Canel the activity signup, elapse={}, cmd={}", (cancelEndTime - cancelStartTime), cmd);

				 LOGGER.info("cancelSignup end activityId: " + activity.getId() + " userId: " + user.getId() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());

	             return dto;
	        	
	        });
        }).first();

        Long  userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        // 报名取消事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhActivities.class.getSimpleName());
            event.setEntityId(cmd.getActivityId());
            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_ENTER_CANCEL.dft());
        });
		return  resDto;
    }

	public void signupOrderRefund(Activity activity, Long userId){
		long startTime = System.currentTimeMillis();
		ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), userId, ActivityRosterStatus.NORMAL.getCode());


		//只有需要支付并已经支付的才需要退款
		if(activity.getChargeFlag() == null || activity.getChargeFlag().byteValue() == ActivityChargeFlag.UNCHARGE.getCode() || 
				roster == null || roster.getPayFlag() == null || roster.getPayFlag().byteValue() != ActivityRosterPayFlag.PAY.getCode()){
			return;
		}

		Long refoundOrderNo = this.onlinePayService.createBillId(DateHelper.currentGMTTime().getTime());

		//支付时是不同的版本，此处也要按不同的版本做处理，当前有版本1、2，默认是老版本1 edit by yanjun 20170919
        RefundOrderCommandResponse refundOrderCommandResponse = refundActivitySignupOrder(activity, roster, userId);
        if (refundOrderCommandResponse != null) {
            roster.setRefundPayOrderId(refundOrderCommandResponse.getOrderId());
        }
		roster.setPayFlag(ActivityRosterPayFlag.REFUND.getCode());
		roster.setRefundOrderNo(refundOrderCommandResponse.getBusinessOrderNumber());
		roster.setRefundAmount(roster.getPayAmount());
		roster.setRefundTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		activityProvider.updateRoster(roster);

		
		long endTime = System.currentTimeMillis();
		LOGGER.debug("Refund from vendor, userId={}, activityId={}, elapse={}", userId, activity.getId(), (endTime - startTime));
	}

	private void refundV1(Activity activity, ActivityRoster roster, Long userId, Long refoundOrderNo){
		PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
		String refoundApi =  this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.refound", "POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record");
		String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.appKey", "");
		refundCmd.setAppKey(appKey);
		Long timestamp = System.currentTimeMillis();
		refundCmd.setTimestamp(timestamp);
		Integer randomNum = (int) (Math.random()*1000);
		refundCmd.setNonce(randomNum);
		refundCmd.setRefundOrderNo(String.valueOf(refoundOrderNo));

		refundCmd.setOrderNo(String.valueOf(roster.getOrderNo()));

		refundCmd.setOnlinePayStyleNo(VendorType.fromCode(roster.getVendorType()).getStyleNo());

		// 老数据无该字段，它们都是ACTIVITYSIGNUPORDER类型的  edit by yanjun 20170713
		if(roster.getOrderType() != null && !"".equals(roster.getOrderType())){
			refundCmd.setOrderType(roster.getOrderType());
		}else{
			refundCmd.setOrderType(OrderType.OrderTypeEnum.ACTIVITYSIGNUPORDER.getPycode());
		}


		refundCmd.setRefundAmount(roster.getPayAmount());

		refundCmd.setRefundMsg("报名取消退款");
		this.setSignatureParam(refundCmd);

		PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refoundApi, refundCmd, PayZuolinRefundResponse.class);
		if(refundResponse != null && refundResponse.getErrorCode() != null && refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
			LOGGER.info("Refund from vendor successfully, orderNo={}, userId={}, activityId={}, refundCmd={}, response={}",
					roster.getOrderNo(), userId, activity.getId(), refundCmd, refundResponse);
		} else{
			LOGGER.error("Refund failed from vendor, orderNo={}, userId={}, activityId={}, refundCmd={}, response={}",
					roster.getOrderNo(), userId, activity.getId(), refundCmd, refundResponse);
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_REFUND_ERROR,
					"bill  refound error");
		}

	}
    //更换使用新的退款V3
//	private void refundV2(Activity activity, ActivityRoster roster, Long userId, Long refoundOrderNo){
//		Long amount = payService.changePayAmount(roster.getPayAmount());
//		CreateOrderRestResponse refundResponse = payService.refund(OrderType.OrderTypeEnum.ACTIVITYSIGNUPORDER.getPycode(), roster.getOrderNo(), refoundOrderNo, amount);
//
//		if(refundResponse != null || refundResponse.getErrorCode() != null && refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
//			LOGGER.info("Refund from vendor successfully, orderNo={}, userId={}, activityId={}, amount={}, response={}",
//					roster.getOrderNo(), userId, activity.getId(), amount, StringHelper.toJsonString(refundResponse));
//		} else{
//			LOGGER.error("Refund from vendor successfully, orderNo={}, userId={}, activityId={}, amount={}, response={}",
//					roster.getOrderNo(), userId, activity.getId(), amount, StringHelper.toJsonString(refundResponse));
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_REFUND_ERROR,
//					"bill  refound error");
//		}
//
//	}
    private Long refundV3(Activity activity, ActivityRoster roster, Long userId){
        CreateOrderCommand createOrderCommand = new CreateOrderCommand();
        BigDecimal amount = activity.getChargePrice();
        if(amount == null){
            createOrderCommand.setAmount(0L);
        }
        createOrderCommand.setAmount(amount.multiply(new BigDecimal(100)).longValue());
        createOrderCommand.setRefundOrderId(roster.getPayOrderId());
        Long orderNo = this.onlinePayService.createBillId(DateHelper
                .currentGMTTime().getTime());
        createOrderCommand.setBizOrderNum("activity"+orderNo);
        createOrderCommand.setAccountCode("NS"+UserContext.getCurrentNamespaceId().toString());
        createOrderCommand.setSourceType(1);
        String homeUrl = configurationProvider.getValue("home.url", "");
        String backUri = configurationProvider.getValue("pay.v2.callback.url.activity", "");
        String backUrl = homeUrl + contextPath + backUri;
        createOrderCommand.setBackUrl(backUrl);
        CreateOrderRestResponse refundResponse = payServiceV2.createRefundOrder(createOrderCommand);

        if(refundResponse != null || refundResponse.getErrorCode() != null && refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
            LOGGER.info("Refund from vendor successfully, orderNo={}, userId={}, activityId={}, amount={}, response={}",
                    roster.getOrderNo(), userId, activity.getId(), amount, StringHelper.toJsonString(refundResponse));
        } else{
            LOGGER.error("Refund from vendor successfully, orderNo={}, userId={}, activityId={}, amount={}, response={}",
                    roster.getOrderNo(), userId, activity.getId(), amount, StringHelper.toJsonString(refundResponse));
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
                    RentalServiceErrorCode.ERROR_REFUND_ERROR,
                    "bill  refound error");
        }
        if (refundResponse.getResponse() != null) {
            return refundResponse.getResponse().getOrderId();
        }
        return null;
    }

    private RefundOrderCommandResponse refundActivitySignupOrder(Activity activity, ActivityRoster roster, Long userId){
        CreateRefundOrderCommand createRefundOrderCommand = prepareRefundCommand(roster);
        CreateRefundOrderRestResponse refundOrderRestResponse = this.orderService.createRefundOrder(createRefundOrderCommand);
        if(refundOrderRestResponse != null && refundOrderRestResponse.getErrorCode() != null && refundOrderRestResponse.getErrorCode().equals(HttpStatus.OK.value())){
            LOGGER.info("Refund from vendor successfully, orderNo={}, userId={}, activityId={}, response={}",
                    roster.getOrderNo(), userId, activity.getId(), StringHelper.toJsonString(refundOrderRestResponse));
        } else{
            LOGGER.error("Refund from vendor failed, orderNo={}, userId={}, activityId={}, response={}",
                    roster.getOrderNo(), userId, activity.getId(), StringHelper.toJsonString(refundOrderRestResponse));
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
                    RentalServiceErrorCode.ERROR_REFUND_ERROR,
                    "bill  refound error");
        }

        return refundOrderRestResponse.getResponse();
    }

    private CreateRefundOrderCommand prepareRefundCommand(ActivityRoster roster){
        CreateRefundOrderCommand createRefundOrderCommand = new CreateRefundOrderCommand();
        String systemId = configurationProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
        createRefundOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        createRefundOrderCommand.setAccountCode(generateAccountCode());
        createRefundOrderCommand.setBusinessOrderNumber(roster.getOrderNo());
        Double payAmount = roster.getPayAmount().doubleValue() * 100;
        createRefundOrderCommand.setAmount(payAmount.longValue());
        createRefundOrderCommand.setBusinessOperatorType(BusinessPayerType.USER.getCode());
        createRefundOrderCommand.setBusinessOperatorId(String.valueOf(UserContext.currentUserId()));
        createRefundOrderCommand.setCallbackUrl(getPayCallbackUrl());
        createRefundOrderCommand.setSourceType(SourceType.MOBILE.getCode());
        return createRefundOrderCommand;
    }
	/***给支付相关的参数签名*/
	private void setSignatureParam(PayZuolinRefundCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getAppKey());
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",cmd.getAppKey());
		map.put("timestamp",cmd.getTimestamp()+"");
		map.put("nonce",cmd.getNonce()+"");
		map.put("refundOrderNo",cmd.getRefundOrderNo());
		map.put("orderNo", cmd.getOrderNo());
		map.put("onlinePayStyleNo",cmd.getOnlinePayStyleNo() );
		map.put("orderType",cmd.getOrderType() );
		//modify by wh 2016-10-24 退款使用toString,下订单的时候使用doubleValue,两边用的不一样,为了和电商保持一致,要修改成toString
//		map.put("refundAmount", cmd.getRefundAmount().doubleValue()+"");
		map.put("refundAmount", cmd.getRefundAmount().toString());
		map.put("refundMsg", cmd.getRefundMsg()); 
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		cmd.setSignature(signature);
	}
	
	
	
	private Object restCall(String api, Object command, Class<?> responseType) {
		String host = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.host", "https://pay.zuolin.com");
		return restCall(api, command, responseType, host);
	}
	private Object restCall(String api, Object o, Class<?> responseType,String host) {
		AsyncRestTemplate template = new AsyncRestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new StringHttpMessageConverter(Charset
				.forName("UTF-8")));
		template.setMessageConverters(messageConverters);
		String[] apis = api.split(" ");
		String method = apis[0];

		String url = host
				+ api.substring(method.length() + 1, api.length()).trim();

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		HttpEntity<String> requestEntity = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Host", host); 
		headers.add("charset", "UTF-8");

		ListenableFuture<ResponseEntity<String>> future = null;

		if (method.equalsIgnoreCase("POST")) {
			requestEntity = new HttpEntity<>(StringHelper.toJsonString(o),
					headers);
			LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
			future = template.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
		} else {
			Map<String, String> params = new HashMap<String, String>();
			StringHelper.toStringMap("", o, params);
			LOGGER.debug("params is :" + params.toString());

			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramMap.add(entry.getKey().substring(1),
						URLEncoder.encode(entry.getValue()));
			}

			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramMap)
					.build().toUriString();
			requestEntity = new HttpEntity<>(null, headers);
			LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
			future = template.exchange(url, HttpMethod.GET, requestEntity,
					String.class);
		}

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = future.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.info("restCall error " + e.getMessage());
			return null;
		}

		if (responseEntity != null
				&& responseEntity.getStatusCode() == HttpStatus.OK) {

			// String bodyString = new
			// String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8")
			// ;
			String bodyString = responseEntity.getBody();
			LOGGER.debug(bodyString);
			LOGGER.debug("HEADER" + responseEntity.getHeaders());
//			return bodyString;
			return StringHelper.fromJsonString(bodyString, responseType);

		}

		LOGGER.info("restCall error " + responseEntity.getStatusCode());
		return null;

	}
	
    @Override
    public ActivityDTO checkin(ActivityCheckinCommand cmd) {
        User user = UserContext.current().getUser();
        if (user == null) {
            LOGGER.error("user do not login!");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_USER_NOT_LOGIN, "user do not login ");
        }
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        if (DateHelper.currentGMTTime().getTime() > activity.getEndTimeMs()) {
            LOGGER.error("activity is already ended, activityid+{}",cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_ACTIVITY_END, "activity is already ended, activityid+{}",cmd.getActivityId());
        }
        ActivityRoster acroster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
        if(acroster == null) {
        	LOGGER.error("handle activityRoster error ,the activityRoster does not exsit.activityId={}, userId = {}",cmd.getActivityId()
        			, user.getId());
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER, "check in error id = {}, userId = {}", cmd.getActivityId(), user.getId());
        }
        // 签到增加异常消息 modify sfyan 20160712
        if(acroster.getConfirmFlag() == null) {
        	acroster.setConfirmFlag(ConfirmStatus.UN_CONFIRMED.getCode());
        }
        
        LOGGER.info("activity ConfirmFlag = " + activity.getConfirmFlag() + ", acroster ConfirmFlag = " + acroster.getConfirmFlag());
    	LOGGER.info("activity.getConfirmFlag() == null is " + (activity.getConfirmFlag() == null) 
    			+ "activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode() is " + (activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode())
    			+ "activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode() is " + (activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode())
    			+ "acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue()" + (acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue()));
        
    	if(ConfirmStatus.fromCode(acroster.getConfirmFlag()) != ConfirmStatus.CONFIRMED){
    		LOGGER.error("check in error ,has not officially Join activities.activityId = {}, userId = {}, confirmFlag = {} ",cmd.getActivityId()
        			, user.getId(), acroster.getConfirmFlag());
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_CHECKIN_UN_CONFIRMED, "check in error id = {}, userId = {}", cmd.getActivityId(), user.getId());
    	}
    	
    	if(CheckInStatus.fromCode(acroster.getCheckinFlag()) == CheckInStatus.CHECKIN){
    		LOGGER.error("check in error , already check in. activityId = {}, userId = {}, checkinFlag = {} ",cmd.getActivityId()
        			, user.getId(), acroster.getCheckinFlag());
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_CHECKIN_ALREADY, "check in error id = {}, userId = {}", cmd.getActivityId(), user.getId());
    	}
    	
    	dbProvider.execute(status->{
        	if(activity.getConfirmFlag() == null || ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.UN_CONFIRMED 
        			|| (ConfirmStatus.fromCode(activity.getConfirmFlag()) == ConfirmStatus.CONFIRMED && acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue())){
        		
        		Long familyId = getFamilyId();
        		
        		ActivityRoster roster = activityProvider.checkIn(activity, user.getId(), familyId);
//                Post p = createPost(user.getId(),post,null,"");
////                p.setContent(configurationProvider.getValue(CHECKIN_AUTO_COMMENT, ""));
//                p.setContent(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE,
//                    String.valueOf(ActivityLocalStringCode.ACTIVITY_CHECKIN), UserContext.current().getUser().getLocale(), ""));
                
                //字段错了 签到应记录到checkin字段中 modified by xiongying 20160708
                if (familyId != null)
                    activity.setCheckinFamilyCount(activity.getCheckinFamilyCount() + 1);
                activity.setCheckinAttendeeCount(activity.getCheckinAttendeeCount()
                        + (roster.getAdultCount() + roster.getChildCount()));
                
//                roster.setCheckinFlag((byte)1);
//                forumProvider.createPost(p);
                LOGGER.debug("roster={}", roster);
        	}
            
            return status;
        });
        
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setProcessStatus(getStatus(activity).getCode());
        
        //dto checkin名字不用 要手动转 modified by xiongying 20160708
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
//        //是否需要确认，要确认则确认后才能签到
//        if(activity.getConfirmFlag()==null) {
//        	dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
//        }
//        else {
//        	if(activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode())
//        		dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
//        	if(activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode())
//        		//tobecontinue
//        		;
//        }
        
        if(activity.getConfirmFlag() == null || activity.getConfirmFlag() == ConfirmStatus.UN_CONFIRMED.getCode() 
    			|| (activity.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode() && acroster.getConfirmFlag() == ConfirmStatus.CONFIRMED.getCode().longValue())){
        	dto.setUserActivityStatus(ActivityStatus.CHECKEINED.getCode());
        
        }
        
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        dto.setForumId(post.getForumId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        fixupVideoInfo(dto);//added by janson
        return dto;
    }

    private ProcessStatus getStatus(Activity activity) {
        return StatusChecker.getProcessStatus(activity.getStartTimeMs(), activity.getEndTimeMs());
    }

    @Override
    public ActivityListResponse findActivityDetails(ActivityListCommand cmd) {
        User user = UserContext.current().getUser();
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        
        Post post = forumProvider.findPostById(activity.getPostId());
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getAnchor()==null?0L:cmd.getAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }
        
        //一般用户只查已确认的，创建者查询确认和不确认的人 add by yanjun 20170505  feature activity 3.0.0
        boolean onlyConfirm = true;
        if(user.getId().equals(activity.getCreatorUid())){
        	onlyConfirm = false;
        }
        List<ActivityRoster> rosterList = activityProvider.listRosterPagination(locator, cmd.getPageSize(),
                activity.getId(), onlyConfirm);
        
        ActivityRoster userRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
        LOGGER.info("find roster {}",userRoster);
        ActivityListResponse response = new ActivityListResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setForumId(post.getForumId());
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setActivityId(activity.getId());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        /////////////////////////////////////
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        fixupVideoInfo(dto);//added by janson
        ////////////////////////////////////
        
        //填充报名信息的统计数据 start  add by yanjun 20170503
        populateRosterStatistics(dto);
        //填充报名信息的统计数据  end
        
        response.setActivity(dto);
        List<ActivityMemberDTO> result = rosterList.stream().map(r -> {
            ActivityMemberDTO d = ConvertHelper.convert(r, ActivityMemberDTO.class);
            d.setConfirmFlag(convertToInt(r.getConfirmFlag()));
            d.setCreatorFlag(0);
            if (r.getUid().longValue() == post.getCreatorUid().longValue())
                d.setCreatorFlag(1);
            d.setLotteryWinnerFlag(convertToInt(r.getLotteryFlag()));
            d.setCheckinFlag(r.getCheckinFlag()==null?0:r.getCheckinFlag().intValue());
            d.setConfirmTime(r.getConfirmTime()==null?null:r.getConfirmTime().toString());
            if (r.getFamilyId() != null) {
                FamilyDTO family = familyProvider.getFamilyById(r.getFamilyId());
                if (family != null) {
                    d.setFamilyName(family.getName());
                    d.setFamilyId(r.getFamilyId());
                }
            }
            
            User currentUser = userProvider.findUserById(r.getUid());
            d.setId(r.getId());
            GetGeneralFormValuesCommand getGeneralFormValuesCommand = new GetGeneralFormValuesCommand();
            getGeneralFormValuesCommand.setSourceId(r.getId());
            getGeneralFormValuesCommand.setSourceType(ActivitySignupFormHandler.GENERAL_FORM_MODULE_HANDLER_ACTIVITY_SIGNUP);
            getGeneralFormValuesCommand.setOriginFieldFlag(NormalFlag.NEED.getCode());
            List<PostApprovalFormItem> values = this.generalFormService.getGeneralFormValues(getGeneralFormValuesCommand);
            if (!CollectionUtils.isEmpty(values)) {
                for (PostApprovalFormItem postApprovalFormItem : values) {
                    if (postApprovalFormItem.getFieldName().equals("USER_PHONE")) {
                        d.setPhone(Arrays.asList(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()).getFieldValue()));
                    }
                    if (postApprovalFormItem.getFieldName().equals("USER_NAME")) {
                        d.setUserName(processCommonTextField(postApprovalFormItem, postApprovalFormItem.getFieldValue()).getFieldValue());
                    }
                }
            }else {
                //导入和手动添加的用户uid为0
                if (currentUser != null && currentUser.getId() != 0) {
                    d.setUserAvatar(contentServerService.parserUri(currentUser.getAvatar(), EntityType.ACTIVITY.getCode(), activity.getId()));
                    d.setUserName(populateUserName(currentUser, activity.getPostId()));

                    List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(currentUser.getId());

                    List<String> phones = identifiers.stream().filter((a)-> { return IdentifierType.fromCode(a.getIdentifierType()) == IdentifierType.MOBILE; })
                            .map((a) -> { return a.getIdentifierToken(); })
                            .collect(Collectors.toList());
                    d.setPhone(phones);
                }else {
                    d.setUserName(r.getRealName());
                    d.setPhone(Arrays.asList(r.getPhone()));
                }
            }

            return d;
        }).collect(Collectors.toList());
        if(rosterList.size()<cmd.getPageSize()){
            response.setNextAnchor(null);
        }else{
            response.setNextAnchor(locator.getAnchor());
        }
        response.setRoster(result);
        response.setCreatorFlag(0);
        // current user is sender?
        if (user.getId().equals(activity.getCreatorUid())) {
            // return url
            String baseDir = configurationProvider.getValue(DEFAULT_HOME_URL, "");
            response.setCheckinQRUrl(baseDir + "/activity/checkin?activityId=" + activity.getId());
            response.setCreatorFlag(1);
        }
        //填充创建者信息
        populatePostCreatorInfo(activity.getCreatorUid(), response);
        return response;
    }

    private void populatePostCreatorInfo(Long creatorId, ActivityListResponse response) {
        String creatorNickName = "";
        String creatorAvatar = "";
        User creator = userProvider.findUserById(creatorId);
        if(creator != null) {
            creatorNickName = creator.getNickName();
            creatorAvatar = creator.getAvatar();
        }

        response.setCreatorNickName(creatorNickName);
        response.setCreatorAvatar(creatorAvatar);
        /*解决web 帖子头像问题，当帖子创建者没有头像时，取默认头像   by sw */
        if(StringUtils.isEmpty(creatorAvatar)) {

            //防止creator空指针  add by yanjun 20171011
            Integer namespaceId = 0;
            if(creator != null && creator.getNamespaceId() != null){
                namespaceId = creator.getNamespaceId();
            }
            creatorAvatar = configProvider.getValue(namespaceId, "user.avatar.default.url", "");
        }

        if(creatorAvatar != null && creatorAvatar.length() > 0) {
            String avatarUrl = getResourceUrlByUir(creatorAvatar,
                    EntityType.USER.getCode(), creatorId);
            response.setCreatorAvatarUrl(avatarUrl);
        }
    }

    private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=" + uri
                        + ", ownerType=" + ownerType + ", ownerId=" + ownerId, e);
            }
        }

        return url;
    }
    
    /**
     * 增加几个统计维度：已确认、已付款、未付款、已签到和未签到 add by yanjun 20170502
     * @param dto
     */
    private void populateRosterStatistics(ActivityDTO dto){
    	//已确认
        Condition condition = Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(ConfirmStatus.CONFIRMED.getCode());
        condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
        Integer confirmUserCount = activityProvider.countActivityRosterByCondition(dto.getActivityId(), condition);
        dto.setConfirmUserCount(confirmUserCount);
        //已支付和待支付, 待支付 = 已确认 - 已支付
        Integer payUserCount = 0;
        if(dto.getChargeFlag() != null && dto.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()){
        	condition = Tables.EH_ACTIVITY_ROSTER.PAY_FLAG.eq(ActivityRosterPayFlag.PAY.getCode());
            condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
        	payUserCount = activityProvider.countActivityRosterByCondition(dto.getActivityId(), condition);
            dto.setPayUserCount(payUserCount);
            
            dto.setUnPayUserCount(confirmUserCount - payUserCount);
        }
        //已签到和待签到，待签到 = 已确认|已支付 - 已签到
        if(dto.getCheckinFlag() !=null && dto.getCheckinFlag().intValue() == ActivitySignupFlag.SIGNUP.getCode().intValue()){
        	condition = Tables.EH_ACTIVITY_ROSTER.CHECKIN_FLAG.eq(CheckInStatus.CHECKIN.getCode());
            condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
        	Integer checkinUserCount = activityProvider.countActivityRosterByCondition(dto.getActivityId(), condition);
            dto.setCheckinUserCount(checkinUserCount);
            
            if(dto.getChargeFlag() == ActivityChargeFlag.CHARGE.getCode()){
            	dto.setUnCheckinUserCount(payUserCount - checkinUserCount);
            }else{
            	dto.setUnCheckinUserCount(confirmUserCount - checkinUserCount);
            }
        }
    }
    
    private String populateUserName(User user, long postId) {
    	// 根据产品姚绮云要求，不要显示@xxxx, add by tt, 20170307
    	return user.getNickName();
//    	Post post = this.forumProvider.findPostById(postId);
//        VisibleRegionType regionType = VisibleRegionType.fromCode(post.getVisibleRegionType());
//        Long regionId = post.getVisibleRegionId();
//        
//        if(regionType != null && regionId != null) {
//            String creatorNickName = user.getNickName();
//            if(creatorNickName == null) {
//                creatorNickName = "";
//            }
//            switch(regionType) {
//            case COMMUNITY:
//                Community community = communityProvider.findCommunityById(regionId);
//                if(community != null)
//                	creatorNickName = creatorNickName + "@" + community.getName();
//                break;
//            case REGION:
//                Organization organization = organizationProvider.findOrganizationById(regionId);
//                if(organization !=null)
//                	creatorNickName = creatorNickName + "@" + organization.getName();
//                break;
//            default:
//                LOGGER.error("Unsupported visible region type, userId=" + user.getId() 
//                    + ", regionType=" + regionType + ", postId=" + post.getId());
//            }
//            return creatorNickName;
//        } else {
//            LOGGER.error("Region type or id is null, userId=" + user.getId() + ", postId=" + post.getId());
//        }
//        
//        return "";
        
    }

    private Integer convertToInt(Long val) {
        if (val == null) {
            return null;
        }
        return val.intValue();
    }

    private Integer convertToInt(Byte code) {
        if (code == null)
            return null;
        return code.intValue();
    }

    private ActivityStatus getActivityStatus(ActivityRoster userRoster) {
        LOGGER.info("check roster the current roster is {}",userRoster);
        if(userRoster==null || ConfirmStatus.REJECT.getCode().equals(userRoster.getConfirmFlag())){
            return ActivityStatus.UN_SIGNUP;
        }
        if (CheckInStatus.CHECKIN.getCode().equals(userRoster.getCheckinFlag())) {
            return ActivityStatus.CHECKEINED;
        }
        if (userRoster.getConfirmFlag() != null&&userRoster.getConfirmFlag().longValue()!=0L) {
            return ActivityStatus.CONFIRMED;
        }
        return ActivityStatus.SIGNUP;

    }

    private Long getFamilyId() {
        User user = UserContext.current().getUser();
        if (user != null && user.getAddressId() != null) {
            Family family = familyProvider.findFamilyByAddressId(user.getAddressId());
            if (family == null) {
                return null;
            }
            return family.getId();
        }
        return null;
    }

    //活动确认
    @Override
    public ActivityDTO confirm(ActivityConfirmCommand cmd) {
    	ActivityRoster itemTemp = activityProvider.findRosterById(cmd.getRosterId());
    	if (itemTemp == null) {
    		LOGGER.error("cannnot find roster record in database");
    		throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    				ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER,
    				"cannnot find roster record in database id=" + cmd.getRosterId());
    	}

    	//在锁内部更新报名和活动信息  add by yanjun 20170522
    	return (ActivityDTO) this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
    		
    		//在锁内部重新活动信息  add by yanjun 20170522
    		ActivityRoster item = activityProvider.findRosterById(cmd.getRosterId());

    		//在锁的内部重新校验报名信息，防止报名取消了之后再发起确认操作  add by yanjun 20170905
			if (item == null || item.getStatus() == null || item.getStatus().byteValue() != ActivityRosterStatus.NORMAL.getCode()) {
				LOGGER.error("cannnot find roster record in database");
				throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
						ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER,
						"cannnot find roster record in database id=" + cmd.getRosterId());
			}


			Activity activity = activityProvider.findActivityById(item.getActivityId());
    		if (activity == null) {
    			LOGGER.error("cannnot find activity record in database");
    			// TODO
    			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    					ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "cannnot find activity record in database id="
    							+ cmd.getRosterId());
    		}


    		Post post = forumProvider.findPostById(activity.getPostId());
    		//validate post status
    		if (post == null) {
    			LOGGER.error("cannnot find post record in database");
    			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    					ActivityServiceErrorCode.ERROR_INVALID_POST_ID,
    					"cannnot find post record in database id=" + cmd.getRosterId());
    		}

    		// 因为使用新规则已报名=已确认。  如果活动不需要确认在报名时限制人数，如果活动需要确认则在确认时限制。     add by yanjun 20170503
    		if (activity.getMaxQuantity() != null && activity.getSignupAttendeeCount() >= activity.getMaxQuantity().intValue()) {
    			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    					ActivityServiceErrorCode.ERROR_BEYOND_CONTRAINT_QUANTITY,
    					"beyond contraint quantity!");
    		}

    		// 后台管理系统确认不用判断是不是创建者
    		User user = UserContext.current().getUser();
    		//        if (post.getCreatorUid().longValue() != user.getId().longValue()) {
    		//            LOGGER.error("the user is invalid.cannot confirm");
    		//            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    		//                    ActivityServiceErrorCode.ERROR_INVALID_USER,
    		//                    "the user is invalid.cannot confirm id=" + cmd.getRosterId());
    		//        }
    		dbProvider.execute(status -> {
				LOGGER.info("confirm start activityId: " + activity.getId() + " userId: " + item.getUid() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());
    			//           forumProvider.createPost(createPost(user.getId(), post, cmd.getConfirmFamilyId(), cmd.getTargetName()));


    			//因为使用新规则已报名=已确认 start。  add by yanjun 20170503
    			//1、signup：不需要确认的话，立刻添加到已报名人数；2、conform：添加到已报名人数；3、reject：不处理；4、cancel：如果已确认则减，如果未确认则不处理
    			int adult = item.getAdultCount() == null ? 0 : item.getAdultCount();
    			int child = item.getChildCount() == null ? 0 : item.getChildCount();
    			activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()+adult+child);
    			if(user.getAddressId()!=null){
    				activity.setSignupFamilyCount(activity.getSignupFamilyCount()+1);
    			}
    			//因为使用新规则已报名=已确认 end

    			activity.setConfirmAttendeeCount(activity.getConfirmAttendeeCount() + item.getChildCount()
    			+ item.getChildCount());
    			activity.setConfirmFamilyCount(activity.getConfirmFamilyCount() + 1);
    			activity.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());
    			activityProvider.updateActivity(activity);
    			item.setConfirmUid(user.getId());
    			item.setConfirmTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
    			item.setConfirmFamilyId(cmd.getConfirmFamilyId());
    			item.setConfirmFlag(ConfirmStatus.CONFIRMED.getCode());

    			//设置订单开始时间, 结束时间，用于定时取消订单
    			if(activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()){
    				populateNewRosterOrder(item, activity.getCategoryId());

    				//启动定时器，当时间超过设定时间时，取消订单。
    				rosterPayTimeoutService.pushTimeout(item);
    			}
    			activityProvider.updateRoster(item);

				LOGGER.info("confirm end activityId: " + activity.getId() + " userId: " + item.getUid() + " signupAttendeeCount: " + activity.getSignupAttendeeCount());
    			return status;
    		});

    		ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
    		dto.setActivityId(activity.getId());
    		dto.setForumId(post.getForumId());
    		dto.setPosterUrl(getActivityPosterUrl(activity));
    		dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
    		dto.setEnrollUserCount(activity.getSignupAttendeeCount());
    		dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
    		dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
    		dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
    		dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
    		dto.setUserActivityStatus(getActivityStatus(item).getCode());
    		dto.setProcessStatus(getStatus(activity).getCode());
    		dto.setFamilyId(activity.getCreatorFamilyId());
    		dto.setStartTime(activity.getStartTime().toString());
    		dto.setStopTime(activity.getEndTime().toString());
    		dto.setSignupEndTime(getSignupEndTime(activity).toString());
    		dto.setGroupId(activity.getGroupId());
    		fixupVideoInfo(dto);//added by janson

    		//管理员同意活动的报名
    		if (item.getUid().longValue() != 0L) {
    			if(activity.getChargeFlag() == null || activity.getChargeFlag().byteValue() == ActivityChargeFlag.UNCHARGE.getCode()){
    				Map<String, String> map = new HashMap<String, String>();
    				map.put("postName", activity.getSubject());

    				//创建带链接跳转的消息头    add by yanjun 20170513 
    				ActivityDetailActionData actionData = new ActivityDetailActionData();
    				actionData.setForumId(post.getForumId());
    				actionData.setTopicId(post.getId());
    				String url =  RouterBuilder.build(Router.ACTIVITY_DETAIL, actionData);
    				String subject = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE, 
    						String.valueOf(ActivityLocalStringCode.ACTIVITY_HAVE_CONFIRM), 
    						UserContext.current().getUser().getLocale(), 
    						"Activity Have been Confirm");
    				Map<String, String> meta = createActivityRouterMeta(url, subject);
    				sendMessageCode(item.getUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_SIGNUP_TO_USER_HAVE_CONFIRM, meta);

    			}else{
    				Long durationTime = item.getOrderExpireTime().getTime() - item.getOrderStartTime().getTime();
    				Integer days = (int) (durationTime / 1000 / 3600 / 24);
    				Integer hours  = (int) (durationTime / 1000 / 3600 % 24);

    				Map<String, String> map = new HashMap<String, String>();
    				map.put("postName", activity.getSubject());
    				map.put("payTimeDays", days.toString());
    				map.put("payTimeHours", hours.toString());

    				ActivityDetailActionData actionData = new ActivityDetailActionData();
    				actionData.setForumId(post.getForumId());
    				actionData.setTopicId(post.getId());
    				String url =  RouterBuilder.build(Router.ACTIVITY_DETAIL, actionData);
    				String subject = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE, 
    						String.valueOf(ActivityLocalStringCode.ACTIVITY_TO_PAY), 
    						UserContext.current().getUser().getLocale(), 
    						"Activity To Been Pay");

    				//创建带链接跳转的消息头    add by yanjun 20170513
    				Map<String, String> meta = createActivityRouterMeta(url, subject);

    				sendMessageCode(item.getUid(), user.getLocale(), map, ActivityNotificationTemplateCode.ACTIVITY_CREATOR_CONFIRM_TO_USER_TO_PAY, meta);
    			}

    		}
    		return dto;
    	}).first();
    }

    private Post createPost(Long uid, Post p, Long familyId, String targetName) {
        // for checkin
        Post post = new Post();
        post.setParentPostId(p.getId());
        post.setForumId(p.getForumId());
//        String template = configurationProvider.getValue(CONFIRM_AUTO_COMMENT, "");
        String template = localeStringService.getLocalizedString(
        		ActivityLocalStringCode.SCOPE,
                String.valueOf(ActivityLocalStringCode.ACTIVITY_CONFIRM),
                UserContext.current().getUser().getLocale(),
                "");
        post.setContent(TemplatesConvert.convert(template, new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", targetName);
            }
        }, ""));
        post.setCreatorUid(uid);
        post.setContentType(PostContentType.TEXT.getCode());
        post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        post.setCreatorUid(UserContext.current().getUser().getId());
        return post;
    }

    @Override
    public ActivityDTO findSnapshotByPostId(Long postId) {
        Activity activity = activityProvider.findSnapshotByPostId(postId);
        if (activity == null) {
            LOGGER.error("cannot find activity for post.postId={}",postId);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "cannot find activity");
        }
        Post post = forumProvider.findPostById(activity.getPostId());
        User user = UserContext.current().getUser();
        ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
        LOGGER.info("find roster {}",roster);
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        //活动添加是否有活动附件标识 add by xiongying 20161207
        boolean existAttachments = activityProvider.existActivityAttachments(activity.getId());
        dto.setActivityAttachmentFlag(existAttachments);

        dto.setActivityId(activity.getId());
        dto.setForumId(post.getForumId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setUserActivityStatus(getActivityStatus(roster).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String convertStartTime = format.format(activity.getStartTime().getTime());
//        String convertEndTime = format.format(activity.getEndTime().getTime());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        fixupVideoInfo(dto);//added by janson
        return dto;
    }

    @Override
    public void rejectPost(ActivityRejectCommand cmd) {
    	User user = UserContext.current().getUser();
    	ActivityRoster rosterTemp = activityProvider.findRosterById(cmd.getRosterId());
    	if (rosterTemp == null) {
    		LOGGER.error("cannot reject the roster");
    		throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    				ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER, "invalid roster id");
    	}

    	//在锁内部更新报名和活动信息  add by yanjun 20170522
    	this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {

    		//在锁内部重新查询报名信息  add by yanjun 20170522
    		ActivityRoster roster = activityProvider.findRosterById(cmd.getRosterId());

			//在锁的内部重新校验报名信息，防止报名取消了之后再发起拒绝操作  add by yanjun 20170905
			if (roster == null || roster.getStatus() == null || roster.getStatus().byteValue() != ActivityRosterStatus.NORMAL.getCode()) {
				LOGGER.error("cannnot find roster record in database");
				throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
						ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER,
						"cannnot find roster record in database id=" + cmd.getRosterId());
			}

    		Activity activity = activityProvider.findActivityById(roster.getActivityId());
    		if (activity == null) {
    			LOGGER.error("invalid activity.id={}", roster.getActivityId());
    			throw RuntimeErrorException
    			.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
    					"invalid activity id=" + roster.getActivityId());
    		}

    		Long postId = activity.getPostId();
    		Post post = forumProvider.findPostById(postId);
    		//validate post status
    		if (post == null) {
    			LOGGER.error("invalid post.id={}", postId);
    			throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    					ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id=" + postId);
    		}
    		// 后台管理系统不用判断是不是创建者
    		//        if (user.getId().longValue() != post.getCreatorUid().longValue()) {
    		//            LOGGER.error("No permission to reject the roster.rosterId={}", cmd.getRosterId());
    		//            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
    		//                    ActivityServiceErrorCode.ERROR_INVALID_USER, "invalid post id=" + postId);
    		//        }

    		int total = roster.getAdultCount() + roster.getChildCount();
    		dbProvider.execute(status->{
    			//need lock
    			//            activityProvider.deleteRoster(roster);
    			if (ConfirmStatus.fromCode(roster.getConfirmFlag()) == ConfirmStatus.CONFIRMED) {
    				int result = activity.getCheckinAttendeeCount() - total;
    				activity.setConfirmAttendeeCount(result < 0 ? 0 : result);
    				if(roster.getConfirmFamilyId()!=null)
    					activity.setConfirmFamilyCount(activity.getConfirmFamilyCount()-1);
    			}
    			if (CheckInStatus.CHECKIN.getCode().equals(roster.getCheckinFlag())) {
    				int result = activity.getCheckinAttendeeCount() - total;
    				activity.setCheckinAttendeeCount(result < 0 ? 0 : result);
    				if(roster.getConfirmFamilyId()!=null)
    					activity.setCheckinFamilyCount(activity.getCheckinFamilyCount()-1);
    			}
    			//因为使用新规则已报名=已确认 start。  add by yanjun 20170503
    			//1、signup：不需要确认的话，立刻添加到已报名人数；2、conform：添加到已报名人数；3、reject：不处理；4、cancel：如果已确认则减，如果未确认则不处理
    			//            activity.setSignupAttendeeCount(activity.getSignupAttendeeCount()-total);
    			//            if(roster.getConfirmFamilyId()!=null)
    			//                activity.setSignupFamilyCount(activity.getSignupFamilyCount()-1);
    			activityProvider.updateActivity(activity);

    			roster.setConfirmFlag(ConfirmStatus.REJECT.getCode());
    			roster.setStatus(ActivityRosterStatus.REJECT.getCode());
    			activityProvider.updateRoster(roster);

    			return status;
    		});

    		if (roster.getUid().longValue() != 0L) {
    			User queryUser = userProvider.findUserById(roster.getUid());
    			if (activity.getGroupId() != null) {
    				RejectJoinGroupRequestCommand rejectCmd=new RejectJoinGroupRequestCommand();
    				rejectCmd.setGroupId(activity.getGroupId());
    				rejectCmd.setUserId(roster.getUid());
    				rejectCmd.setRejectText(cmd.getReason());
    				//reject to join group
    				//groupService.rejectJoinGroupRequest(rejectCmd);
    			}
    			Post comment = createPost(user.getId(), post, null, "");
    			//            String template=configurationProvider.getValue(REJECT_AUTO_COMMENT, "");
    			String template = localeStringService.getLocalizedString(
    					ActivityLocalStringCode.SCOPE,
    					String.valueOf(ActivityLocalStringCode.ACTIVITY_REJECT),
    					UserContext.current().getUser().getLocale(),
    					"");
    			comment.setContent(TemplatesConvert.convert(template, new HashMap<String, String>(){/**
    			 * 
    			 */
    				private static final long serialVersionUID = 8928858603520552572L;

    				{
    					put("subject", activity.getSubject());
    					put("reason",cmd.getReason()==null?"":cmd.getReason());
    					put("username",queryUser.getNickName()==null?queryUser.getAccountName():queryUser.getNickName());

    				}}, ""));
    			//            forumProvider.createPost(comment);



    			MessageDTO messageDto = new MessageDTO();
    			messageDto.setAppId(AppConstants.APPID_MESSAGING);
    			messageDto.setSenderUid(user.getId());
    			messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), queryUser.getId().toString()));
    			messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(user.getId())));
    			messageDto.setBodyType(MessageBodyType.TEXT.getCode());
    			messageDto.setBody(comment.getContent());
    			messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

    			UserLogin u = userService.listUserLogins(user.getId()).get(0);
    			messagingService.routeMessage(u, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
    					queryUser.getId().toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    		}

    		return null;
    	});
    }

    @Override
    public ActivityListResponse findActivityDetailsByPostId(Post post) {
       User user=UserContext.current().getUser();
        Activity activity = activityProvider.findSnapshotByPostId(post.getId());
        if(activity==null){
            return null;
        }
        List<ActivityRoster> rosterList = activityProvider.listRosters(activity.getId(), ActivityRosterStatus.NORMAL);
        ActivityRoster userRoster = activityProvider.findRosterByUidAndActivityId(activity.getId(), UserContext
                .current().getUser().getId(), ActivityRosterStatus.NORMAL.getCode());
        ActivityListResponse response = new ActivityListResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);

        //返回系统当前时间 add by yanjun 20170510
        dto.setSystemTime(DateHelper.currentGMTTime().getTime());
        
        ActivityRoster creator = activityProvider.findRosterByUidAndActivityId(activity.getId(), activity.getCreatorUid(), ActivityRosterStatus.NORMAL.getCode());
        if (creator.getFormId() == null) {
            dto.setSignupFormFlag(TrueOrFalseFlag.FALSE.getCode());
        }else {
            dto.setSignupFormFlag(TrueOrFalseFlag.TRUE.getCode());
        }
        //活动添加是否有活动附件标识 add by xiongying 20161207
        boolean existAttachments = activityProvider.existActivityAttachments(activity.getId());
        dto.setActivityAttachmentFlag(existAttachments);

        dto.setActivityId(activity.getId());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?null:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?null:activity.getSignupFlag().intValue());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setFamilyId(activity.getCreatorFamilyId());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String convertStartTime = format.format(activity.getStartTime().getTime());
//        String convertEndTime = format.format(activity.getEndTime().getTime());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setForumId(post.getForumId());
        dto.setUserActivityStatus(userRoster == null ? ActivityStatus.UN_SIGNUP.getCode() : getActivityStatus(
                userRoster).getCode());
        
        //返回倒计时 add by yanjun 20170510 start
        dto.setUserPayFlag(userRoster == null || userRoster.getPayFlag() == null ? ActivityRosterPayFlag.UNPAY.getCode() : userRoster.getPayFlag());
        if(activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()  && userRoster != null && userRoster.getOrderStartTime() != null &&
        		(userRoster.getPayFlag() == null || userRoster.getPayFlag().byteValue() == ActivityRosterPayFlag.UNPAY.getCode())){
        	Long countdown =  userRoster.getOrderExpireTime().getTime() - DateHelper.currentGMTTime().getTime();
        	if(countdown > 0){
        		dto.setUserOrderCountdown(countdown);
        	}
        }
        //返回倒计时 add by yanjun 20170510 end
        
        //填充报名信息的统计数据 start  add by yanjun 20170503
        populateRosterStatistics(dto);
        //填充报名信息的统计数据  end
        
        
        fixupVideoInfo(dto);//added by janson
        response.setActivity(dto);
        
       
        
        List<ActivityMemberDTO> result = rosterList.stream().map(r -> {
            ActivityMemberDTO d = ConvertHelper.convert(r, ActivityMemberDTO.class);
            d.setConfirmFlag(convertToInt(r.getConfirmFlag()));
            d.setCreatorFlag(0);
            if (r.getUid().longValue() == post.getCreatorUid().longValue())
                d.setCreatorFlag(1);
            d.setLotteryWinnerFlag(convertToInt(r.getLotteryFlag()));
            d.setCheckinFlag(r.getCheckinFlag()==null?0:r.getCheckinFlag().intValue());
            d.setConfirmTime(r.getConfirmTime()==null?null:r.getConfirmTime().toString());
            if (r.getFamilyId() != null) {
                FamilyDTO family = familyProvider.getFamilyById(r.getFamilyId());
                if (family != null) {
                    d.setFamilyName(family.getName());
                    d.setFamilyId(r.getFamilyId());
                }
                User currentUser = userProvider.findUserById(r.getUid());
                d.setId(r.getId());
                if (currentUser != null) {
                    d.setUserAvatar(contentServerService.parserUri(currentUser.getAvatar(), EntityType.ACTIVITY.getCode(), activity.getId()));
                    d.setUserName(currentUser.getAccountName());
                    
                    List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(currentUser.getId());
                    
                    List<String> phones = identifiers.stream().filter((a)-> { return IdentifierType.fromCode(a.getIdentifierType()) == IdentifierType.MOBILE; })
                            .map((a) -> { return a.getIdentifierToken(); })
                            .collect(Collectors.toList());
                    d.setPhone(phones);
                }else {
					d.setUserName(r.getRealName());
					d.setPhone(Arrays.asList(r.getPhone()));
				}

            }
//            
//            //返回倒计时 add by yanjun 20170510 start
//            if(d.getPayFlag() != null && d.getPayFlag() == ActivityRosterPayFlag.UNPAY.getCode() && d.getOrderStartTime() != null){
//            	Long countdown =  d.getOrderExpireTime().getTime() - DateHelper.currentGMTTime().getTime();
//            	if(countdown > 0){
//            		d.setOrderCountdown(countdown);
//            	}else{
//            		d.setOrderCountdown(0L);
//            	}
//            }
            //返回倒计时 add by yanjun 20170510 end
            
            return d;
        }).collect(Collectors.toList());
        response.setRoster(result);
        response.setCreatorFlag(0);
        // current user is sender?
        if (user.getId().equals(activity.getCreatorUid())) {
            // return url
            String baseDir = configurationProvider.getValue(DEFAULT_HOME_URL, "");
            response.setCheckinQRUrl(baseDir + "/activity/checkin?activityId=" + activity.getId());
            response.setCreatorFlag(1);
        }
        return response;
    }
    
    private static Date convert(String time,String format){
        SimpleDateFormat f=new SimpleDateFormat(format);
        try {
            return f.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> listActivityCategories(ListActivityCategoriesCommand cmd) {
    	User user = UserContext.current().getUser();
    	Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

    	Long parentId = ( null == cmd.getParentId() ) ? CategoryConstants.CATEGORY_ID_ACTIVITY : cmd.getParentId();
    	
        Tuple[] orderBy = new Tuple[1]; 
        orderBy[0] = new Tuple<String, SortOrder>("default_order", SortOrder.ASC);
        List<Category> result = categoryProvider.listChildCategories(namespaceId, parentId, CategoryAdminStatus.ACTIVE,orderBy);
        
//        if(cmd != null && cmd.getCommunityFlagId() != null  && CommunityAppType.TECHPARK.getCode() == cmd.getCommunityFlagId()) {
//        	List<Category> tech = categoryProvider.listChildCategories(namespaceId, CategoryConstants.CATEGORY_ID_TECH_ACTIVITY, CategoryAdminStatus.ACTIVE,orderBy);
//        	result.addAll(tech);
//        }
        return result;
    }

    @Override
    public Tuple<Long,List<ActivityDTO>>  listActivities(ListActivitiesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        CrossShardListingLocator locator=new CrossShardListingLocator();
        if(cmd.getAnchor()!=null){
            locator.setAnchor(cmd.getAnchor());
        }
        
        // 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
//        Condition condtion=null;
//        if(!StringUtils.isEmpty(cmd.getTag())){
//            condtion= Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag());
//        }
//        List<Condition> conditions =new ArrayList<Condition>();
//        if(cmd.getLatitude()!=null&&cmd.getLongitude()!=null){     
//            double latitude= cmd.getLatitude();
//            double longitude=cmd.getLongitude();
//            GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
//            GeoHash[] adjacents = geo.getAdjacent();
//            List<String> geoHashCodes = new ArrayList<String>();
//            geoHashCodes.add(geo.toBase32());
//            for(GeoHash g : adjacents) {
//                geoHashCodes.add(g.toBase32());
//            }
//            conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
//        }

        List<String> geoHashCodes = new ArrayList<String>();
        if(cmd.getLatitude()!=null&&cmd.getLongitude()!=null){     
            double latitude= cmd.getLatitude();
            double longitude=cmd.getLongitude();
            GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
            GeoHash[] adjacents = geo.getAdjacent();
            geoHashCodes.add(geo.toBase32());
            for(GeoHash g : adjacents) {
                geoHashCodes.add(g.toBase32());
            }
        }
        
        Condition condtion = buildNearbyActivityCondition(namespaceId, geoHashCodes, cmd.getTag());
        
        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        //List<Activity> ret = activityProvider.listActivities(locator, value+1,condtion,Operator.OR, conditions.toArray(new Condition[conditions.size()]));
        List<Activity> ret = activityProvider.listActivities(locator, value+1, condtion, false, null);
        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
            Post post = forumProvider.findPostById(activity.getPostId());
            if(post==null){
                return null;
            }
            if(activity.getPosterUri()==null){
            	this.forumProvider.populatePostAttachments(post);
            	List<Attachment> attachmentList = post.getAttachments();
            	if(attachmentList != null && attachmentList.size() != 0){
            		for(Attachment attachment : attachmentList){
            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
            				activity.setPosterUri(attachment.getContentUri());
            			break;
            		}
            	}
            }
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
            dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setSignupEndTime(getSignupEndTime(activity).toString());
            dto.setGroupId(activity.getGroupId());
            dto.setPosterUrl(getActivityPosterUrl(activity));
            dto.setForumId(post.getForumId());
            fixupVideoInfo(dto);//added by janson
            return dto;
            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
         // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
        if(activityDtos.size()<value){
            locator.setAnchor(null);
        }
        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
    }

    @Override
    public Tuple<Long, List<ActivityDTO>> listNearByActivities(ListNearByActivitiesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        double latitude= cmd.getLatitude();
       double longitude=cmd.getLongitude();
       GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 6);
       GeoHash[] adjacents = geo.getAdjacent();
       List<String> geoHashCodes = new ArrayList<String>();
       geoHashCodes.add(geo.toBase32());
       for(GeoHash g : adjacents) {
           geoHashCodes.add(g.toBase32());
       }
       CrossShardListingLocator locator=new CrossShardListingLocator();
       locator.setAnchor(cmd.getAnchor());
       int pageSize=configurationProvider.getIntValue("pagination.page.size", 20);
       // 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
       //List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
       //List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1,null,Operator.OR,conditions.toArray(new Condition[conditions.size()])).stream().map(activity->{
       Condition condition = buildNearbyActivityCondition(namespaceId, geoHashCodes, null);
       List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition, false, null).stream().map(activity->{
          ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
          dto.setActivityId(activity.getId());
          Post post = forumProvider.findPostById(activity.getPostId());
          if(post==null){
              return null;
          }
          if(activity.getPosterUri()==null){
          	this.forumProvider.populatePostAttachments(post);
          	List<Attachment> attachmentList = post.getAttachments();
          	if(attachmentList != null && attachmentList.size() != 0){
          		for(Attachment attachment : attachmentList){
          			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
          				activity.setPosterUri(attachment.getContentUri());
          			break;
          		}
          	}
          }
          dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
          dto.setEnrollUserCount(activity.getSignupAttendeeCount());
          dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
          dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
          dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
          dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
          dto.setProcessStatus(getStatus(activity).getCode());
          dto.setFamilyId(activity.getCreatorFamilyId());
          dto.setPosterUrl(getActivityPosterUrl(activity));
          dto.setStartTime(activity.getStartTime().toString());
          dto.setStopTime(activity.getEndTime().toString());
          dto.setSignupEndTime(getSignupEndTime(activity).toString());
          dto.setGroupId(activity.getGroupId());
          dto.setForumId(post.getForumId());
          User user = UserContext.current().getUser();
          if (user != null) {
        	  List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
        	  if (favorite == null || favorite.size() == 0) {
        		  dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
        	  } else {
        		  dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
        	  }
        	  //add UserActivityStatus by xiongying 20160628
        	  ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
        	  dto.setUserActivityStatus(getActivityStatus(roster).getCode());
          }else {
        	  dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
          }
          fixupVideoInfo(dto);//added by janson
          return dto;
           //全部查速度太慢，先把查出的部分排序 by xiongying20161208
       // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
       })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
       if(result.size()<pageSize)
       {
           locator.setAnchor(null);
       }
        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
    }

    private Timestamp getSignupEndTime(Activity activity) {
    	if (activity.getSignupEndTime() == null) {
			return activity.getStartTime();
		}
    	return activity.getSignupEndTime();
    }
    
	@Override
	public boolean isPostIdExist(Long postId) {
		Activity activity = activityProvider.findSnapshotByPostId(postId);
		
		if(activity == null || "".equals(activity))
			return false;
		
		return true;
	}

	@Override
	public void updatePost(ActivityPostCommand cmd, Long postId) {
		Activity activity = activityProvider.findSnapshotByPostId(postId);
		 
		activity.setStatus((byte)0);
		activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		activityProvider.updateActivity(activity);
		
		createScheduleForActivity(activity);
	}

	@Override
	public  Tuple<Long, List<ActivityDTO>> listNearByActivitiesV2(ListNearByActivitiesCommandV2 cmdV2) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(cmdV2.getCommunity_id());
		
		List<String> geoHashCodes = new ArrayList<String>();

		for(CommunityGeoPoint geoPoint : geoPoints){
			
			double latitude = geoPoint.getLatitude();
			double longitude = geoPoint.getLongitude();
			
			GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 5);
			
			GeoHash[] adjacents = geo.getAdjacent();
			geoHashCodes.add(geo.toBase32());
	        for(GeoHash g : adjacents) {
	           geoHashCodes.add(g.toBase32());
	        }
		}

		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmdV2.getAnchor());
		int pageSize=configurationProvider.getIntValue("pagination.page.size", 20);
		// 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
		// List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
		// List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1,null,Operator.OR,conditions.toArray(new Condition[conditions.size()])).stream().map(activity->{
		Condition condition = buildNearbyActivityCondition(namespaceId, geoHashCodes, null);
		List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition, false, null).stream().map(activity->{
			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
			dto.setActivityId(activity.getId());
			Post post = forumProvider.findPostById(activity.getPostId());
			if(post==null){
                return null;
            }
            if(activity.getPosterUri()==null){
            	this.forumProvider.populatePostAttachments(post);
            	List<Attachment> attachmentList = post.getAttachments();
            	if(attachmentList != null && attachmentList.size() != 0){
            		for(Attachment attachment : attachmentList){
            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
            				activity.setPosterUri(attachment.getContentUri());
            			break;
            		}
            	}
            }
			dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
			dto.setEnrollUserCount(activity.getSignupAttendeeCount());
	        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
	        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
			dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setPosterUrl(getActivityPosterUrl(activity));
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
	        dto.setSignupEndTime(getSignupEndTime(activity).toString());
			dto.setGroupId(activity.getGroupId());
			dto.setForumId(post.getForumId());
			User user = UserContext.current().getUser();
			if (user != null) {
            	List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
            	if (favorite == null || favorite.size() == 0) {
            		dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            	} else {
            		dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            	}
            	//add UserActivityStatus by xiongying 20160628
            	ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
            	dto.setUserActivityStatus(getActivityStatus(roster).getCode());
			}else {
				dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
			}
			fixupVideoInfo(dto);//added by janson
			return dto;

            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
			// 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
       
		if(result.size()<pageSize){
			
			locator.setAnchor(null);
		}
		return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
	}

	@Override
	public Tuple<Long, List<ActivityDTO>> listCityActivities(ListNearByActivitiesCommandV2 cmdV2) {
	    Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(cmdV2.getCommunity_id());
		
		List<String> geoHashCodes = new ArrayList<String>();
		
		for(CommunityGeoPoint geoPoint : geoPoints){
			
			double latitude = geoPoint.getLatitude();
			double longitude = geoPoint.getLongitude();
			
			GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, 4);
			
			GeoHash[] adjacents = geo.getAdjacent();
			geoHashCodes.add(geo.toBase32());
	        for(GeoHash g : adjacents) {
	           geoHashCodes.add(g.toBase32());
	        }
		}
	        
	        
		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmdV2.getAnchor());
		int pageSize=configurationProvider.getIntValue("pagination.page.size", 20);
		// 修改查询活动接口，把参数改为一个Condition对象，以便调用者方便构造条件 by lqs 20160419
		// List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
		// List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1,null,Operator.OR,conditions.toArray(new Condition[conditions.size()])).stream().map(activity->{
		Condition condition = buildNearbyActivityCondition(namespaceId, geoHashCodes, null);
		List<ActivityDTO> result = activityProvider.listActivities(locator, pageSize+1, condition, false, null).stream().map(activity->{
			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
			dto.setActivityId(activity.getId());
			Post post = forumProvider.findPostById(activity.getPostId());
			if(post==null){
                return null;
            }
            if(activity.getPosterUri()==null){
            	this.forumProvider.populatePostAttachments(post);
            	List<Attachment> attachmentList = post.getAttachments();
            	if(attachmentList != null && attachmentList.size() != 0){
            		for(Attachment attachment : attachmentList){
            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
            				activity.setPosterUri(attachment.getContentUri());
            			break;
            		}
            	}
            }
			dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
			dto.setEnrollUserCount(activity.getSignupAttendeeCount());
	        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
	        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
			dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setPosterUrl(getActivityPosterUrl(activity));
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
	        dto.setSignupEndTime(getSignupEndTime(activity).toString());
			dto.setGroupId(activity.getGroupId());
			dto.setForumId(post.getForumId());
			fixupVideoInfo(dto);//added by janson
			return dto;

            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
			// 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/collect(Collectors.toList());
       
		if(result.size()<pageSize){
			
			locator.setAnchor(null);
		}
		return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), result);
	}

	@Override
	public ListActivitiesReponse listActivitiesByTag(ListActivitiesByTagCommand cmd) {
        List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(cmd.getCommunity_id());
        
		StringBuilder strBuilder = new StringBuilder();
		List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
		for(CommunityGeoPoint geoPoint : geoPoints){
		    if(geoPoint.getLatitude() != null && geoPoint.getLongitude() != null) {
    		    GeoLocation geoLocation = new GeoLocation();
    		    geoLocation.setLatitude(geoPoint.getLatitude());
    		    geoLocation.setLongitude(geoPoint.getLongitude());
    		    geoLocationList.add(geoLocation);
		    } else {
		        if(LOGGER.isWarnEnabled()) {
		            LOGGER.warn("Invalid latitude or longitude, geoPoint={}", geoPoint);
		        }
		    }
		    
            if (strBuilder.length() > 0) {
                strBuilder.append(",");
            } else {
                strBuilder.append(geoPoint.getCommunityId());
            }
		}
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, communityId=" + cmd.getCommunity_id() + ", communities="
                + strBuilder);
        }
        
        ActivityLocationScope scope = ActivityLocationScope.NEARBY;
        if(cmd.getRange() == 4) {
            scope = ActivityLocationScope.SAME_CITY;
        }
        
        ListActivitiesByLocationCommand execCmd = new ListActivitiesByLocationCommand();
        execCmd.setLocationPointList(geoLocationList);
        execCmd.setPageAnchor(cmd.getAnchor());
        execCmd.setScope(scope.getCode());
        execCmd.setTag(cmd.getTag());
        execCmd.setPageSize(cmd.getPageSize());
        execCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        execCmd.setOfficialFlag(cmd.getOfficialFlag());
        execCmd.setCategoryId(cmd.getCategoryId());
        execCmd.setContentCategoryId(cmd.getContentCategoryId());
        execCmd.setActivityStatusList(cmd.getActivityStatusList());
        return listActivitiesByLocation(execCmd);
		
        // 把公用的代码转移到listActivitiesByLocation()中，公司场景和小区场景获取经纬度的方式不一样 by lqs 20160419
//        CrossShardListingLocator locator=new CrossShardListingLocator();
//        if(cmd.getAnchor()!=null){
//            locator.setAnchor(cmd.getAnchor());
//        }
//        Condition condtion=null;
//        if(!StringUtils.isEmpty(cmd.getTag())){
//            condtion= Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag());
//        }
//        
//        int range = cmd.getRange();
//        range = (range <= 0) ? 6 : range;
//        
//        List<String> geoHashCodes = new ArrayList<String>();
//		StringBuilder strBuilder = new StringBuilder();
//		for(CommunityGeoPoint geoPoint : geoPoints){
//			
//			double latitude = geoPoint.getLatitude();
//			double longitude = geoPoint.getLongitude();
//			
//			GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, range);
//			
//			GeoHash[] adjacents = geo.getAdjacent();
//			geoHashCodes.add(geo.toBase32());
//	        for(GeoHash g : adjacents) {
//	           geoHashCodes.add(g.toBase32());
//	        }
//	        
//	        if(strBuilder.length() > 0) {
//	            strBuilder.append(",");
//	        } else {
//	            strBuilder.append(geoPoint.getCommunityId());
//	        }
//		}
//		if(LOGGER.isDebugEnabled()) {
//		    LOGGER.debug("Query activities by geohash, communityId=" + cmd.getCommunity_id() + ", communities=" + strBuilder);
//		}
//		List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
//        
//        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
//        List<Activity> ret = activityProvider.listActivities(locator, value+1,condtion,Operator.OR, conditions.toArray(new Condition[conditions.size()]));
//        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
//            Post post = forumProvider.findPostById(activity.getPostId());
//            if(post==null){
//                return null;
//            }
//            if(activity.getPosterUri()==null){
//            	this.forumProvider.populatePostAttachments(post);
//            	List<Attachment> attachmentList = post.getAttachments();
//            	if(attachmentList != null && attachmentList.size() != 0){
//            		for(Attachment attachment : attachmentList){
//            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
//            				activity.setPosterUri(attachment.getContentUri());
//            			break;
//            		}
//            	}
//            }
//            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
//            dto.setActivityId(activity.getId());
//            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
//            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
//            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
//            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
//            dto.setProcessStatus(getStatus(activity).getCode());
//            dto.setFamilyId(activity.getCreatorFamilyId());
//            dto.setStartTime(activity.getStartTime().toString());
//            dto.setStopTime(activity.getEndTime().toString());
//            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(getActivityPosterUrl(activity));
//            dto.setForumId(post.getForumId());
//            return dto;
//        }).filter(r->r!=null).collect(Collectors.toList());
//        if(activityDtos.size()<value){
//            locator.setAnchor(null);
//        }
//        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
	}
	
	private List<Long> getViewedActivityIds() {
		
		List<Long> ids = new ArrayList<Long>();
		UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(UserContext.current().getUser().getId(), 
				UserProfileContstant.VIEWED_ACTIVITY_NEW);
        
		if(userProfile != null) {
	        String idString = userProfile.getStringTag1();
	        String id = idString.substring(1, idString.toString().length()-1);
	        if(!StringUtils.isEmpty(id)) {
	        	String[] idArr = id.split(", ");
				Long[] idLong = new Long[idArr.length];
				for(int i = 0; i < idArr.length; i++) {
					idLong[i] = Long.parseLong(idArr[i]);
				}
				ids = Arrays.asList(idLong);
			}
        }
		
		return ids;
	}
	
	@Override
	public ListActivitiesReponse listActivitiesByLocation(ListActivitiesByLocationCommand cmd) {
		User user = UserContext.current().getUser();
		Long uid = user.getId();
	    ListActivitiesReponse response = null;
	    List<GeoLocation> geoLocationList = cmd.getLocationPointList();
	    if(geoLocationList == null || geoLocationList.size() == 0) {
	        if(LOGGER.isInfoEnabled()) {
	            LOGGER.info("The location point list is empty, cmd={}", cmd);
	        }
	        return response;
	    }
	    
	    int geoCharCount = convertLocationScopeToGeoCharCount(cmd.getScope());
	    List<String> geoHashCodes = convertLocationToGeoCodes(geoLocationList, geoCharCount);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, geoCharCount={}, cmd={}, geoHashCodes={}", geoCharCount, cmd, geoHashCodes);
        }
        
        //List<Condition> conditions = geoHashCodes.stream().map(r->Tables.EH_ACTIVITIES.GEOHASH.like(r+"%")).collect(Collectors.toList());
        Condition condition = buildNearbyActivityCondition(cmd.getNamespaceId(), geoHashCodes, cmd.getTag());

        //comment by tt, 20170116
//        if(null != cmd.getOfficialFlag()){
//            condition = condition.and(Tables.EH_ACTIVITIES.OFFICIAL_FLAG.eq(cmd.getOfficialFlag()));
//        }
//
//        //增加活动类型判断add by xiongying 20161117
//        if(null != cmd.getCategoryId()) {
//            ActivityCategories category = activityProvider.findActivityCategoriesById(cmd.getCategoryId());
//            if (category != null) {
//            	if(SelectorBooleanFlag.TRUE.equals(SelectorBooleanFlag.fromCode(category.getDefaultFlag()))) {
//                    condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.in(cmd.getCategoryId(), 0L));
//                } else {
//                    condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
//                }
//			}
//        }
        
        // 旧版本查询活动时，只有officialFlag标记，新版本查询活动时有categoryId，当然更老的版本两者都没有
        // 为了兼容，规定categoryId为0对应发现里的活动（非官方活动），categoryId为1对应原官方活动
        if (cmd.getCategoryId() == null) {
        	OfficialFlag officialFlag = OfficialFlag.fromCode(cmd.getOfficialFlag());
			if(officialFlag == null) officialFlag = OfficialFlag.NO;
			Long categoryId = officialFlag == OfficialFlag.YES?1L:0L;
			cmd.setCategoryId(categoryId);
		}
//        else {
//			officialFlag = categoryId.longValue() == 1L?OfficialFlag.YES:OfficialFlag.NO;
//		}
        // 把officialFlag换成categoryId一个条件
        condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
        
        //增加活动主题分类，add by tt, 20170109
        if (cmd.getContentCategoryId() != null) {
        	//老版本用id作为标识，新版本id无意义，使用entryId和namespaceId作为标识。此处弃用findActivityCategoriesById  add by yanjun 20170524
        	ActivityCategories category = activityProvider.findActivityCategoriesByEntryId(cmd.getContentCategoryId(), UserContext.getCurrentNamespaceId());
        	//如果没有查到分类或者分类的allFlag为是，则表示查询全部，不用加条件
        	if (category != null && TrueOrFalseFlag.FALSE == TrueOrFalseFlag.fromCode(category.getAllFlag())) {
        		condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(cmd.getContentCategoryId()));
			}
		}
        
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        List<Activity> activities=new ArrayList<Activity>();
        
        //查第一页时，一部分为上次查询过后新发的贴 modified by xiongying 20160707
        //产品1.6需求：去掉查一部分新发的贴的功能 modified by xiongying 20161208
//        if (locator.getAnchor() == null || locator.getAnchor() == 0L){
//        	Timestamp lastViewedTime = null;
//        	String counts = configurationProvider.getValue(ConfigConstants.ACTIVITY_LIST_NUM, "3");
//        	UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(UserContext.current().getUser().getId(),
//        								UserProfileContstant.VIEWED_ACTIVITY_NEW);
//
//        	if(profile != null) {
//        		lastViewedTime = new Timestamp(Long.valueOf(profile.getItemValue()));
//        	}
//        	List<Activity> newActivities = activityProvider.listNewActivities(locator, Integer.valueOf(counts), lastViewedTime, condition);
//	    	if(newActivities != null && newActivities.size() > 0) {
//	    		activities.addAll(newActivities);
//	    	}
//        }
//
//		List<Long> ids = getViewedActivityIds();

        // 添加活动状态筛选     add by xq.tian  2017/01/24
        if (cmd.getActivityStatusList() != null) {
            Condition statusCondition = this.buildActivityProcessStatusCondition(cmd.getActivityStatusList());
            if (statusCondition != null) {
                condition = condition.and(statusCondition);
            }
        }

        List<Activity> ret = activityProvider.listActivities(locator, pageSize - activities.size() + 1, condition, false, null);
        
//        if(ret != null && ret.size() > 0) {
//        	for(Activity act : ret) {
//        		if(!ids.contains(act.getId())) {
//        			activities.add(act);
//            	}
//        	}
//        }


        activities.addAll(ret);
        List<ActivityDTO> activityDtos = activities.stream().map(activity->{
            Post post = forumProvider.findPostById(activity.getPostId());
            if (activity.getPosterUri() == null && post != null) {
                this.forumProvider.populatePostAttachments(post);
                List<Attachment> attachmentList = post.getAttachments();
                if (attachmentList != null && attachmentList.size() != 0) {
                    for (Attachment attachment : attachmentList) {
                        if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
                            activity.setPosterUri(attachment.getContentUri());
                        break;
                    }
                }
            }
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
            dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
            dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setSignupEndTime(getSignupEndTime(activity).toString());
            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(getActivityPosterUrl(activity));
            String posterUrl = getActivityPosterUrl(activity);
            dto.setPosterUrl(posterUrl);
            if (post != null) {
                dto.setForumId(post.getForumId());
            }
            List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(uid, UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
            if (favorite == null || favorite.size() == 0) {
                dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            } else {
                dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            }
            //add UserActivityStatus by xiongying 20160628
            ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), uid, ActivityRosterStatus.NORMAL.getCode());
            dto.setUserActivityStatus(getActivityStatus(roster).getCode());
            fixupVideoInfo(dto);//added by janson

            return dto;
            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
         // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/
        collect(Collectors.toList());

        Long nextPageAnchor = locator.getAnchor();

        response = new ListActivitiesReponse(nextPageAnchor, activityDtos);
        return response;
	}

    private Condition buildActivityProcessStatusCondition(List<Integer> activityStatusList) {
        Timestamp currTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        Condition notStartCond = null;
        Condition underWayCond = null;
        Condition endCond = null;
        if (activityStatusList.contains(NOTSTART.getCode())) {
            notStartCond = Tables.EH_ACTIVITIES.START_TIME.gt(currTime);
        }
        if (activityStatusList.contains(UNDERWAY.getCode())) {
            underWayCond = Tables.EH_ACTIVITIES.START_TIME.le(currTime).and(Tables.EH_ACTIVITIES.END_TIME.gt(currTime));
        }
        if (activityStatusList.contains(ProcessStatus.END.getCode())) {
            endCond = Tables.EH_ACTIVITIES.END_TIME.lt(currTime);
        }
        Optional<Condition> condition = Stream.of(notStartCond, underWayCond, endCond).filter(Objects::nonNull).reduce(Condition::or);
        return condition.isPresent() ? condition.get() : null;
    }

    private String getActivityPosterUrl(Activity activity) {
		
		if(activity.getPosterUri() == null) {
			String posterUrl = contentServerService.parserUri(configurationProvider.getValue(ConfigConstants.ACTIVITY_POSTER_DEFAULT_URL, ""), EntityType.ACTIVITY.getCode(), activity.getId());
			return posterUrl;
		} else {
			String posterUrl = contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId());
			if(posterUrl.equals(activity.getPosterUri())) {
				posterUrl = contentServerService.parserUri(configurationProvider.getValue(ConfigConstants.ACTIVITY_POSTER_DEFAULT_URL, ""), EntityType.ACTIVITY.getCode(), activity.getId());
			}
			return posterUrl;
		}
		
	}
	
	private Condition buildNearbyActivityCondition(Integer namespaceId, List<String> geoHashCodes, String tag) {
	    Condition condition = null;
	    if(namespaceId != null) {
	        condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId);
	    }
	    
        if(!StringUtils.isEmpty(tag)){
            if(condition == null) {
                condition = Tables.EH_ACTIVITIES.TAG.eq(tag);
            } else {
                condition = condition.and(Tables.EH_ACTIVITIES.TAG.eq(tag));
            }
        } 
        
        Condition geoCondition = buildActivityGeoLocationCondition(geoHashCodes);
        if(geoCondition != null) {
            if(condition == null) {
                condition = geoCondition;
            } else {
                condition = condition.and(geoCondition);
            }
        }
        
        return condition;
	}
	
	private Condition buildActivityGeoLocationCondition(List<String> geoHashCodes) {
	    Condition condition = null;
	    
	    if(geoHashCodes != null) {
	        for(String geoHashCode : geoHashCodes) {
	            if(condition == null) {
	                condition = Tables.EH_ACTIVITIES.GEOHASH.like(geoHashCode + "%");
	            } else {
	                condition = condition.or(Tables.EH_ACTIVITIES.GEOHASH.like(geoHashCode + "%"));
	            }
	        }
	    }
	    
	    return condition;
	}
	
	private int convertLocationScopeToGeoCharCount(Byte locationScope) {
	    ActivityLocationScope scope = ActivityLocationScope.fromCode(locationScope);
        int geoCharCount = 6; // 默认在3公里内，按GEO算法最接近的字符数为6
        if(scope != null) {
            switch(scope) {
            case ALL:
                geoCharCount = 1;
                break;
            case NEARBY:
                geoCharCount = 6;
                break;
            case SAME_CITY:
                geoCharCount = 4;
            }
        }
        
        return geoCharCount;
	}
	
	private List<String> convertLocationToGeoCodes(List<GeoLocation> geoLocationList, int geoCharCount) {
	    List<String> geoHashCodes = new ArrayList<String>();
        for(GeoLocation geoLocation : geoLocationList){
            Double latitude = geoLocation.getLatitude();
            Double longitude = geoLocation.getLongitude();
            
            GeoHash geo = GeoHash.withCharacterPrecision(latitude, longitude, geoCharCount);
            
            GeoHash[] adjacents = geo.getAdjacent();
            geoHashCodes.add(geo.toBase32());
            for(GeoHash g : adjacents) {
               geoHashCodes.add(g.toBase32());
            }
        }
        
        return geoHashCodes;
	}
	
//	@Override
//	public Tuple<Long, List<ActivityDTO>> listActivitiesByNamespaceIdAndTag(
//			ListActivitiesByNamespaceIdAndTagCommand cmd) {
//		 
//		CrossShardListingLocator locator=new CrossShardListingLocator();
//		
//        if(null !=cmd.getAnchor()){
//            locator.setAnchor(cmd.getAnchor());
//        }
//        Condition condtion = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(cmd.getNamespaceId());
//        
//        if(!StringUtils.isEmpty(cmd.getTag())){
//            condtion= condtion.and(Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag()));
//        }
//
//        int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
//        
//        List<Activity> ret = activityProvider.listActivities(locator, value+1,condtion,Operator.OR, null);
//        
//        List<ActivityDTO> activityDtos = ret.stream().map(activity->{
//            Post post = forumProvider.findPostById(activity.getPostId());
//            if(post==null){
//                return null;
//            }
//            if(activity.getPosterUri()==null){
//            	this.forumProvider.populatePostAttachments(post);
//            	List<Attachment> attachmentList = post.getAttachments();
//            	if(attachmentList != null && attachmentList.size() != 0){
//            		for(Attachment attachment : attachmentList){
//            			if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
//            				activity.setPosterUri(attachment.getContentUri());
//            			break;
//            		}
//            	}
//            }
//            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
//            dto.setActivityId(activity.getId());
//            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
//            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
//            dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
//            dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
//            dto.setProcessStatus(getStatus(activity).getCode());
//            dto.setFamilyId(activity.getCreatorFamilyId());
//            dto.setStartTime(activity.getStartTime().toString());
//            dto.setStopTime(activity.getEndTime().toString());
//            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(getActivityPosterUrl(activity));
//            dto.setForumId(post.getForumId());
//            dto.setGuest(activity.getGuest());
//            return dto;
//        }).filter(r->r!=null).collect(Collectors.toList());
//        if(activityDtos.size()<value){
//            locator.setAnchor(null);
//        }
//        return new Tuple<Long, List<ActivityDTO>>(locator.getAnchor(), activityDtos);
//	}
	@Override
	public ListActivitiesReponse listActivitiesByNamespaceIdAndTag(ListActivitiesByNamespaceIdAndTagCommand cmd) {
		ListActivitiesReponse response = new ListActivitiesReponse(null, null);
		ListActivityTopicByCategoryAndTagCommand command = new ListActivityTopicByCategoryAndTagCommand();
		command.setCategoryId(cmd.getCategoryId());
		command.setCommunityId(cmd.getCommunityId());
		command.setPageAnchor(cmd.getPageAnchor());
		command.setPageSize(cmd.getPageSize());
		command.setTag(cmd.getTag());
		ListPostCommandResponse post = forumService.listActivityPostByCategoryAndTag(command);
	    if(post != null) {
	    	response.setNextPageAnchor(post.getNextPageAnchor());
	    	List<PostDTO> posts = post.getPosts();
	    	if(posts != null) {
	    		List<ActivityDTO> activityDtos = posts.stream().map(r -> {
	    			Activity activity = activityProvider.findActivityById(r.getEmbeddedId());
	    			if(activity != null) {
		    			if(activity.getPosterUri()==null){
		    	           
		    				List<AttachmentDTO> attachmentList = r.getAttachments();
				            if(attachmentList != null && attachmentList.size() != 0){
				                for(AttachmentDTO attachment : attachmentList){
				                    if(PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
				                    	activity.setPosterUri(attachment.getContentUri());
				                    break;
				                }
				            }
		    	        }
		    			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
		    			dto.setActivityId(activity.getId());
		    	        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
		    	        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
		    	        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
		    	        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
		    	        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
		    	        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
		    	        dto.setProcessStatus(getStatus(activity).getCode());
		    	        dto.setFamilyId(activity.getCreatorFamilyId());
		    	        dto.setStartTime(activity.getStartTime().toString());
		    	        dto.setStopTime(activity.getEndTime().toString());
		    	        dto.setSignupEndTime(getSignupEndTime(activity).toString());
		    	        dto.setGroupId(activity.getGroupId());
		    	        dto.setPosterUrl(getActivityPosterUrl(activity));
		    	        dto.setForumId(r.getForumId());
		    	        dto.setGuest(activity.getGuest());
		    	        fixupVideoInfo(dto);//added by janson
		    			
		    			return dto;
	    			}
	    			else {
	    				return null;
	    			}
	    		}).filter(r->r!=null).collect(Collectors.toList());
	    		
	    		response.setActivities(activityDtos);
	    	}
	    }
	    
	    
	    
	    return response;
	}
	
	// 由于场景扩展到园区，单纯靠Entity实例已经不能满足要求，需要改为按场景来区分 by lqs 20160513
//	@Override
//	public ListActivitiesReponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd) {
//	    long startTime = System.currentTimeMillis();
//	    User user = UserContext.current().getUser();
//        Long userId = user.getId();
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        SceneTokenDTO sceneTokenDto = userService.checkSceneToken(userId, cmd.getSceneToken());
//        
//        int geoCharCount = 6; // 默认使用6位GEO字符
//        ActivityLocationScope scope = ActivityLocationScope.fromCode(cmd.getScope());
//        if(scope == ActivityLocationScope.SAME_CITY) {
//            geoCharCount = 4;
//        }
//        
//        ListActivitiesReponse resp = null;
//        Community community = null;
//        ListActivitiesByTagCommand execCmd = null;
//        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
//        switch(entityType) {
//        case COMMUNITY_RESIDENTIAL:
//        case COMMUNITY_COMMERCIAL:
//        case COMMUNITY:
//            community = communityProvider.findCommunityById(sceneTokenDto.getEntityId());
//            execCmd = new ListActivitiesByTagCommand();
//            execCmd.setCommunity_id(community.getId());
//            execCmd.setAnchor(cmd.getPageAnchor());
//            execCmd.setPageSize(cmd.getPageSize());
//            execCmd.setTag(cmd.getTag());
//            execCmd.setRange(geoCharCount);
//            resp = listActivitiesByTag(execCmd);
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
//            if(family != null) {
//                community = communityProvider.findCommunityById(family.getCommunityId());
//                execCmd = new ListActivitiesByTagCommand();
//                execCmd.setCommunity_id(community.getId());
//                execCmd.setAnchor(cmd.getPageAnchor());
//                execCmd.setPageSize(cmd.getPageSize());
//                execCmd.setTag(cmd.getTag());
//                execCmd.setRange(geoCharCount);
//                resp = listActivitiesByTag(execCmd);
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
//                }
//            }
//            break;
//        case ORGANIZATION:
//            ListOrgNearbyActivitiesCommand execOrgCmd = ConvertHelper.convert(cmd, ListOrgNearbyActivitiesCommand.class);
//            execOrgCmd.setCommunityId(sceneTokenDto.getEntityId());
//            resp = listOrgNearbyActivities(execOrgCmd);
//            break;
//        default:
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
//            break;
//        }
//        
//        if(LOGGER.isDebugEnabled()) {
//            long endTime = System.currentTimeMillis();
//            LOGGER.debug("List nearby activities by scene, userId={}, namespaceId={}, elapse={}, cmd={}", 
//                userId, namespaceId, (endTime - startTime), cmd);
//        }
//        
//        return resp;
//	}
	
	@Override
	public ListActivitiesReponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd) {
		
		// 非登录用户只能看第一页 add by xiongying20161010
    	if(cmd.getPageAnchor() != null ) {
    		 if(!userService.isLogon()){
    			 LOGGER.error("Not logged in.");
  			   throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
  					   "Not logged in.");

    		 }
    	}
		
    	long startTime = System.currentTimeMillis();
	    User user = UserContext.current().getUser();
	    Long userId = user.getId();
	    Integer namespaceId = UserContext.getCurrentNamespaceId();
	    //SceneTokenDTO sceneTokenDto = userService.checkSceneToken(userId, cmd.getSceneToken());

        AppContext appContext = UserContext.current().getAppContext();

        int geoCharCount = 6; // 默认使用6位GEO字符
	    ActivityLocationScope scope = ActivityLocationScope.fromCode(cmd.getScope());
	    if(scope == ActivityLocationScope.SAME_CITY) {
	        geoCharCount = 4;
	    }
	    
	    ListActivitiesReponse resp = null;
	    //SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
        //检查游客是否能继续访问此场景 by sfyan 20161009
//        userService.checkUserScene(sceneType);
//	    switch(sceneType) {
//	    case DEFAULT:
//	    case PARK_TOURIST:
//	        resp = listActivitiesByScope(sceneTokenDto, cmd, geoCharCount, sceneTokenDto.getEntityId(), scope);
//	        break;
//	    case FAMILY:
//	        FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
//	        if(family != null) {
//	            resp = listActivitiesByScope(sceneTokenDto, cmd, geoCharCount, family.getCommunityId(), scope);
//	        } else {
//	            if(LOGGER.isWarnEnabled()) {
//	                LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
//	            }
//	        }
//	        break;
//        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
//        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
//	        Organization organization = organizationProvider.findOrganizationById(sceneTokenDto.getEntityId());
//            if(organization != null) {
//                Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
//                resp = listActivitiesByScope(sceneTokenDto, cmd, geoCharCount, communityId, scope);
//            }
//            break;
//	    case PM_ADMIN:
//			ListOrgNearbyActivitiesCommand execOrgCmd = ConvertHelper.convert(cmd, ListOrgNearbyActivitiesCommand.class);
//			execOrgCmd.setOrganizationId(sceneTokenDto.getEntityId());
//			//resp = listOrgNearbyActivities(execOrgCmd);
//			execOrgCmd.setSceneToken(cmd.getSceneToken());
//			resp = listOrgActivitiesByScope(execOrgCmd);
//	        break;
//	    default:
//	        LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
//	        break;
//	    }



        //旧版本的家庭场景没有communityId，又跪了
        if(appContext != null && appContext.getCommunityId() == null && appContext.getFamilyId() != null){
            FamilyDTO family = familyProvider.getFamilyById(appContext.getFamilyId());
            if(family != null){
                appContext.setCommunityId(family.getCommunityId());
            }
        }


        if(appContext.getCommunityId() != null){
            resp = listActivitiesByScope(null, cmd, geoCharCount, appContext.getCommunityId(), scope);
        }else if(appContext.getOrganizationId() != null){
            ListOrgNearbyActivitiesCommand execOrgCmd = ConvertHelper.convert(cmd, ListOrgNearbyActivitiesCommand.class);
			execOrgCmd.setOrganizationId(appContext.getOrganizationId());
            resp = listOrgActivitiesByScope(execOrgCmd);
        }

	    
	    if(LOGGER.isDebugEnabled()) {
	        long endTime = System.currentTimeMillis();
	        LOGGER.debug("List nearby activities by scene, userId={}, namespaceId={}, elapse={}, cmd={}", 
	            userId, namespaceId, (endTime - startTime), cmd);
	    }
	    
	    return resp;
	}

	//华润要求只能看到当前小区的活动，因此增加一种位置范围-COMMUNITY。根据传来的范围参数，如果是小区使用新的方法，否则使用老方法。
	private ListActivitiesReponse listActivitiesByScope(SceneTokenDTO sceneTokenDto, ListNearbyActivitiesBySceneCommand cmd,
														int geoCharCount, Long communityId, ActivityLocationScope scope){
		//if(scope != null && scope.getCode() == ActivityLocationScope.COMMUNITY.getCode()){

        //现在应该都是园区的
        return listOfficialActivitiesByScene(cmd);
//		}else{
//			return listCommunityNearbyActivities(sceneTokenDto, cmd, geoCharCount, communityId);
//		}
	}

	//华润要求只能看到当前小区的活动，因此增加一种位置范围-COMMUNITY。根据传来的范围参数，如果是小区使用新的方法，否则使用老方法。
	private  ListActivitiesReponse listOrgActivitiesByScope(ListOrgNearbyActivitiesCommand execOrgCmd){
		//if(execOrgCmd.getScope() != null && execOrgCmd.getScope() == ActivityLocationScope.COMMUNITY.getCode()){

        //现在应该都是园区的
        ListNearbyActivitiesBySceneCommand command = ConvertHelper.convert(execOrgCmd, ListNearbyActivitiesBySceneCommand.class);
			return listOfficialActivitiesByScene(command);
//		}else{
//			return listOrgNearbyActivities(execOrgCmd);
//		}
	}
	
	//根据小区获取活动
	private ListActivitiesReponse listCommunityActivities(SceneTokenDTO sceneTokenDto, ListNearbyActivitiesBySceneCommand cmd, Long communityId){
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		Long uid = user.getId();
	    ListActivitiesReponse response = null;
	    
	    //前台参数没有officialFlag，在后面的代码中，本身就会被设成非官方 add by yanjun 20170413
//		 if(999987L == namespaceId){
//			 cmd.setOfficialFlag(OfficialFlag.NO.getCode());
//         }
	    
		  // 旧版本查询活动时，只有officialFlag标记，新版本查询活动时有categoryId，当然更老的版本两者都没有
        // 为了兼容，规定categoryId为0对应发现里的活动（非官方活动），categoryId为1对应原官方活动
        if (cmd.getCategoryId() == null) {
//        	OfficialFlag officialFlag = OfficialFlag.fromCode(cmd.getOfficialFlag());
//			if(officialFlag == null) officialFlag = OfficialFlag.NO;
//			Long categoryId = officialFlag == OfficialFlag.YES?1L:0L;
//			cmd.setCategoryId(categoryId);
        	cmd.setCategoryId(0L);
		}
        // 把officialFlag换成categoryId一个条件
        Condition condition = Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId());
        
        if(namespaceId != null) {
	        condition = condition.and(Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(namespaceId));
	    }
        
        //增加活动主题分类，add by tt, 20170109
        if (cmd.getContentCategoryId() != null) {
        	//老版本用id作为标识，新版本id无意义，使用entryId和namespaceId作为标识。此处弃用findActivityCategoriesById  add by yanjun 20170524
        	ActivityCategories category = activityProvider.findActivityCategoriesByEntryId(cmd.getContentCategoryId(), UserContext.getCurrentNamespaceId());
        	//如果没有查到分类或者分类的allFlag为是，则表示查询全部，不用加条件
        	if (category != null && TrueOrFalseFlag.FALSE == TrueOrFalseFlag.fromCode(category.getAllFlag())) {
        		condition = condition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(cmd.getContentCategoryId()));
			}
		}
        
        //根据园区或者园区对应组织机构查询
        Condition communityCondition = Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        communityCondition = communityCondition.and(Tables.EH_ACTIVITIES.VISIBLE_REGION_ID.eq(communityId));
        //获取园区的组织机构
        List<Long>  organizationIds = organizationService.getOrganizationIdsTreeUpToRoot(communityId);
        if(organizationIds != null && organizationIds.size() > 0){
        	 Condition regionCondition = Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
             regionCondition = regionCondition.and(Tables.EH_ACTIVITIES.VISIBLE_REGION_ID.in(organizationIds));
             communityCondition = communityCondition.or(regionCondition);
        }
        condition = condition.and(communityCondition);
        
        if(!StringUtils.isEmpty(cmd.getTag())){
            condition = condition.and(Tables.EH_ACTIVITIES.TAG.eq(cmd.getTag()));
        } 
        
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        List<Activity> activities=new ArrayList<Activity>();


        // 添加活动状态筛选     add by xq.tian  2017/01/24
        if (cmd.getActivityStatusList() != null) {
            Condition statusCondition = this.buildActivityProcessStatusCondition(cmd.getActivityStatusList());
            if (statusCondition != null) {
                condition = condition.and(statusCondition);
            }
        }

        List<Activity> ret = activityProvider.listActivities(locator, pageSize - activities.size() + 1, condition, false, null);

        activities.addAll(ret);
        List<ActivityDTO> activityDtos = activities.stream().map(activity->{
            Post post = forumProvider.findPostById(activity.getPostId());
            if (activity.getPosterUri() == null && post != null) {
                this.forumProvider.populatePostAttachments(post);
                List<Attachment> attachmentList = post.getAttachments();
                if (attachmentList != null && attachmentList.size() != 0) {
                    for (Attachment attachment : attachmentList) {
                        if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
                            activity.setPosterUri(attachment.getContentUri());
                        break;
                    }
                }
            }
            ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
            dto.setActivityId(activity.getId());
            dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
            dto.setEnrollUserCount(activity.getSignupAttendeeCount());
            dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
            dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
            dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
            dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
            dto.setProcessStatus(getStatus(activity).getCode());
            dto.setFamilyId(activity.getCreatorFamilyId());
            dto.setStartTime(activity.getStartTime().toString());
            dto.setStopTime(activity.getEndTime().toString());
            dto.setSignupEndTime(getSignupEndTime(activity).toString());
            dto.setGroupId(activity.getGroupId());
//            dto.setPosterUrl(getActivityPosterUrl(activity));
            String posterUrl = getActivityPosterUrl(activity);
            dto.setPosterUrl(posterUrl);
            if (post != null) {
                dto.setForumId(post.getForumId());
            }
            List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(uid, UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
            if (favorite == null || favorite.size() == 0) {
                dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
            } else {
                dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
            }
            //add UserActivityStatus by xiongying 20160628
            ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), uid, ActivityRosterStatus.NORMAL.getCode());
            dto.setUserActivityStatus(getActivityStatus(roster).getCode());
            fixupVideoInfo(dto);//added by janson

            return dto;
            //全部查速度太慢，先把查出的部分排序 by xiongying20161208
         // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
        })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/
        collect(Collectors.toList());

        Long nextPageAnchor = locator.getAnchor();

        response = new ListActivitiesReponse(nextPageAnchor, activityDtos);
        return response;
	}
	
	private ListActivitiesReponse listCommunityNearbyActivities(SceneTokenDTO sceneTokenDto, ListNearbyActivitiesBySceneCommand cmd, 
	        int geoCharCount, Long communityId) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
	    if(communityId != null) {
    	    ListActivitiesByTagCommand execCmd = new ListActivitiesByTagCommand();
            execCmd.setCommunity_id(communityId);
            execCmd.setAnchor(cmd.getPageAnchor());
            execCmd.setPageSize(cmd.getPageSize());
            execCmd.setTag(cmd.getTag());
            execCmd.setRange(geoCharCount);
            execCmd.setCategoryId(cmd.getCategoryId());
            execCmd.setContentCategoryId(cmd.getContentCategoryId());
            execCmd.setActivityStatusList(cmd.getActivityStatusList());
            if(999987L == namespaceId){
            	execCmd.setOfficialFlag(OfficialFlag.NO.getCode());
            }
            return listActivitiesByTag(execCmd);
	    } else {
	        LOGGER.error("Community not found to query nearby activities, sceneTokenDto={}, communityId={}", sceneTokenDto, communityId);
	        return null;
	    }
	}
	
	@Override
    public ListActivitiesReponse listOrgNearbyActivities(ListOrgNearbyActivitiesCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
	    OrganizationDetail orgDetail = organizationProvider.findOrganizationDetailByOrganizationId(cmd.getOrganizationId());
	    if(orgDetail != null && orgDetail.getLatitude() != null && orgDetail.getLongitude() != null) {
            GeoLocation geoLocation = new GeoLocation();
            geoLocation.setLatitude(orgDetail.getLatitude());
            geoLocation.setLongitude(orgDetail.getLongitude());
            geoLocationList.add(geoLocation);
	    }
	    
	    // 由于存在大量的公司没有自身的经纬度，故取其所管理的小区来作为经纬度
	    if(geoLocationList.size() == 0) {
	        List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());
            for(CommunityDTO community : communities) {
                List<CommunityGeoPoint> geoPoints = communityProvider.listCommunityGeoPoints(community.getId());
                
                StringBuilder strBuilder = new StringBuilder();
                for(CommunityGeoPoint geoPoint : geoPoints){
                    if(geoPoint.getLatitude() != null && geoPoint.getLongitude() != null) {
                        GeoLocation geoLocation = new GeoLocation();
                        geoLocation.setLatitude(geoPoint.getLatitude());
                        geoLocation.setLongitude(geoPoint.getLongitude());
                        geoLocationList.add(geoLocation);
                    } else {
                        if(LOGGER.isWarnEnabled()) {
                            LOGGER.warn("Invalid latitude or longitude, cmd={}, geoPoint={}", cmd, geoPoint);
                        }
                    }
                }
            }
	    }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query activities by geohash, cmd={}, geoLocationList={}", cmd, geoLocationList);
        }
        
        ListActivitiesByLocationCommand execCmd = new ListActivitiesByLocationCommand();
        execCmd.setLocationPointList(geoLocationList);
        execCmd.setPageAnchor(cmd.getPageAnchor());
        execCmd.setScope(cmd.getScope());
        execCmd.setTag(cmd.getTag());
        execCmd.setPageSize(cmd.getPageSize());
        execCmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        execCmd.setCategoryId(cmd.getCategoryId());
        execCmd.setContentCategoryId(cmd.getContentCategoryId());
        execCmd.setActivityStatusList(cmd.getActivityStatusList());
        if(999987L == namespaceId){
        	execCmd.setOfficialFlag(OfficialFlag.NO.getCode());
        }
        return listActivitiesByLocation(execCmd);
   }

	@Override
	public ActivityShareDetailResponse getActivityShareDetail(
			ActivityTokenDTO postToken) {
		
        Activity activity = activityProvider.findSnapshotByPostId(postToken.getPostId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.postId={}", postToken.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid activity postId " + postToken.getPostId());
        }
        GetTopicCommand command = new GetTopicCommand();
        command.setTopicId(postToken.getPostId());
        command.setForumId(postToken.getForumId());
        PostDTO post = forumService.getTopic(command);
        if (post == null) {
            LOGGER.error("handle post failed,maybe post be deleted.postId={}", activity.getPostId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_POST_ID, "invalid post id " + activity.getPostId());
        }
        
        ActivityShareDetailResponse response = new ActivityShareDetailResponse();
        ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
        dto.setForumId(post.getForumId());
        dto.setActivityId(activity.getId());
        dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
        dto.setEnrollUserCount(activity.getSignupAttendeeCount());
        dto.setConfirmFlag(activity.getConfirmFlag()==null?0:activity.getConfirmFlag().intValue());
        dto.setCheckinFlag(activity.getSignupFlag()==null?0:activity.getSignupFlag().intValue());
        dto.setActivityId(activity.getId());
        dto.setProcessStatus(getStatus(activity).getCode());
        dto.setStartTime(activity.getStartTime().toString());
        dto.setStopTime(activity.getEndTime().toString());
        dto.setSignupEndTime(getSignupEndTime(activity).toString());
        dto.setFamilyId(activity.getCreatorFamilyId());
        dto.setGroupId(activity.getGroupId());
        dto.setPosterUrl(getActivityPosterUrl(activity));
        dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
        dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
        fixupVideoInfo(dto);//added by janson
        response.setActivity(dto);
        
        response.setContent(post.getContent());
        response.setChildCount(post.getChildCount());
        response.setViewCount(post.getViewCount());
        response.setNamespaceId(activity.getNamespaceId());
        response.setAttachments(post.getAttachments());
        response.setSubject(post.getSubject());
        response.setCreatorNickName(post.getCreatorNickName());
        response.setCreateTime(post.getCreateTime());
        response.setCreatorAvatarUrl(post.getCreatorAvatarUrl());
        
		return response;
	}

//	@Override
//	public ListActivitiesReponse listOfficialActivitiesByScene(ListNearbyActivitiesBySceneCommand command) {
//		Long userId = UserContext.current().getUser().getId();
//		QueryOrganizationTopicCommand cmd = new QueryOrganizationTopicCommand();
//		SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(command.getSceneToken(), SceneTokenDTO.class);
//		processOfficalActivitySceneToken(userId, sceneTokenDTO, cmd);
//		cmd.setContentCategory(CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
//		cmd.setEmbeddedAppId(AppConstants.APPID_ACTIVITY);
//		cmd.setOfficialFlag(OfficialFlag.YES.getCode());
//		cmd.setPageAnchor(command.getPageAnchor());
//		cmd.setPageSize(command.getPageSize());
//		
//		// 由于listOrgTopics查询官方活动时，当一个机构下面没有管理小区或者以普通公司的身份查询的时候，会查不到东西，使用新的方法来解决 by lqs 20160730
//		// ListPostCommandResponse postResponse = forumService.listOrgTopics(cmd);
//		ListPostCommandResponse postResponse = forumService.listOfficialActivityTopics(cmd);
//		List<PostDTO> posts = postResponse.getPosts();		
//		final List<ActivityDTO> activities = new ArrayList<>();
//		if (posts != null && posts.size() > 0) {
//			posts.forEach(p->{
//				//吐槽：这里ActivityPostCommand和ActivityDTO中相同的字段，名字竟然不一样，如postUri和postUrl
//				ActivityDTO activity = (ActivityDTO) StringHelper.fromJsonString(p.getEmbeddedJson().replace("posterUri", "posterUrl"), ActivityDTO.class);
//				activity.setFavoriteFlag(p.getFavoriteFlag());
//				fixupVideoInfo(activity);//added by janson
//				activities.add(activity);
//			});
//		}
//		
//		ListActivitiesReponse reponse = new ListActivitiesReponse(postResponse.getNextPageAnchor(), activities);
//		return reponse;
//	}
	
	@Override
	public ListActivitiesReponse listOfficialActivitiesByScene(ListNearbyActivitiesBySceneCommand command) {
		Long userId = UserContext.current().getUser().getId();
		QueryOrganizationTopicCommand cmd = new QueryOrganizationTopicCommand();


        AppContext appContext = UserContext.current().getAppContext();

        if(appContext.getCommunityId() != null && appContext.getOrganizationId() != null){
            cmd.setCommunityId(appContext.getCommunityId());
            cmd.setOrganizationId(appContext.getOrganizationId());
        }else if(appContext.getCommunityId() != null){
            cmd.setCommunityId(appContext.getCommunityId());
            List<OrganizationCommunityDTO> list = organizationProvider.findOrganizationCommunityByCommunityId(appContext.getCommunityId());
            if (list != null && list.size() > 0) {
                cmd.setOrganizationId(list.get(0).getOrganizationId());
            }
        }else if(appContext.getOrganizationId() != null) {
            cmd.setOrganizationId(appContext.getOrganizationId());
            OrganizationDTO org = organizationService.getOrganizationById(appContext.getOrganizationId());
            if (org != null && org.getCommunityId() != null) {
                cmd.setCommunityId(org.getCommunityId());
            }

        }else {
            SceneTokenDTO sceneTokenDTO = WebTokenGenerator.getInstance().fromWebToken(command.getSceneToken(), SceneTokenDTO.class);
            processOfficalActivitySceneToken(userId, sceneTokenDTO, cmd);
        }


        cmd.setContentCategory(CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY);
		cmd.setEmbeddedAppId(AppConstants.APPID_ACTIVITY);
		cmd.setOfficialFlag(OfficialFlag.YES.getCode());
		cmd.setPageAnchor(command.getPageAnchor());
		cmd.setPageSize(command.getPageSize());
		cmd.setCategoryId(command.getCategoryId());
		cmd.setContentCategoryId(command.getContentCategoryId());
        cmd.setActivityStatusList(command.getActivityStatusList());
		
		ListActivitiesReponse activities = listOfficialActivities(cmd);
		
		return activities;
	}
	
	@Override
	public ListActivitiesReponse listOfficialActivities(QueryOrganizationTopicCommand cmd) {
		long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long organizationId = cmd.getOrganizationId();
        Long communityId = cmd.getCommunityId();
        Integer namespaceId = cmd.getNamespaceId();

        //先记录是否是查询整个域空间的帖子
        boolean searchNamesapceFlag = false;
		if(cmd.getCommunityId() == null && cmd.getOrganizationId()==null){
			searchNamesapceFlag = true;
		}

        if(namespaceId == null){
        	namespaceId = UserContext.getCurrentNamespaceId();
		}
        List<Long> forumIds = new ArrayList<Long>();
        
        List<Long> communityIdList = new ArrayList<Long>();

		//获取园区id和论坛Id,并返回orgId，因为当查询域空间时需要orgid来查发送到“全部”的帖子 edit by yanjun 20170830
		organizationId = forumService.populateCommunityIdAndForumId(communityId, organizationId, namespaceId, communityIdList, forumIds);

        //重复了，去重
        Set forumIdset = new HashSet();
        forumIdset.addAll(forumIds);
        forumIdset.remove(null);
        forumIds = new ArrayList<Long>();
        forumIds.addAll(forumIdset);

        // 当论坛list为空时，JOOQ的IN语句会变成1=0，导致条件永远不成立，也就查不到东西
        if(forumIds.size() == 0) {
            LOGGER.error("Forum not found for offical activities, cmd={}", cmd);
            return null;
        }
        
        Condition activityCondition = Tables.EH_ACTIVITIES.FORUM_ID.in(forumIds);
        
        //增加categoryId add by xiongying 20161118
        if(null != cmd.getCategoryId()) {
        	//老版本用id作为标识，新版本id无意义，使用entryId和namespaceId作为标识。此处弃用findActivityCategoriesById  add by yanjun 20170524
            ActivityCategories category = activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), namespaceId);
            if (category != null) {
            	if(SelectorBooleanFlag.TRUE.equals(SelectorBooleanFlag.fromCode(category.getDefaultFlag()))) {
                    activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.in(cmd.getCategoryId(), 0L));
                } else {
                    activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
                }
			}
        }else{
			//默认categoryId为1  edit by yanjun 20170712
			activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(1L));
		}


        //增加活动主题分类，add by tt, 20170109
        if (cmd.getContentCategoryId() != null) {
        	//老版本用id作为标识，新版本id无意义，使用entryId和namespaceId作为标识。此处弃用findActivityCategoriesById  add by yanjun 20170524
        	ActivityCategories category = activityProvider.findActivityCategoriesByEntryId(cmd.getContentCategoryId(), namespaceId);
        	//如果没有查到分类或者分类的allFlag为是，则表示查询全部，不用加条件
        	if (category != null && TrueOrFalseFlag.FALSE == TrueOrFalseFlag.fromCode(category.getAllFlag())) {
        		activityCondition = activityCondition.and(Tables.EH_ACTIVITIES.CONTENT_CATEGORY_ID.eq(cmd.getContentCategoryId()));
			}
		}

        // 可见性条件：如果有当前小区/园区，则加上小区条件；如果有对应的管理机构，则加上机构条件；这两个条件为或的关系；
        Condition communityCondition = null;
        if(communityIdList != null && communityIdList.size() > 0) {
            communityCondition = Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            communityCondition = communityCondition.and(Tables.EH_ACTIVITIES.VISIBLE_REGION_ID.in(communityIdList));

            //范围帖子会在各个目标发一个clone帖子，同时有一个发送到全部clone帖子，还有一个发送到全部的real真身帖子  add by yanjun 20170807
			if(searchNamesapceFlag){
				//全部 -- 查询各个目标的（正常），或者发送到“全部”的（clone、正常）
				communityCondition = communityCondition.and(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode()));
				communityCondition = communityCondition.or(Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode()));

			}else{
				//单个 -- 发送到单个目标的（clone、正常），或者发送到“全部”（正常）
				communityCondition = communityCondition.or(Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode())
						.and(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode())));
			}

        }
        Condition orgCondition = null;
        if(organizationId != null) {
            orgCondition = Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.in(VisibleRegionType.REGION.getCode(),VisibleRegionType.ORGANIZATION.getCode());

			//范围帖子会在各个目标发一个clone帖子，同时有一个发送到全部clone帖子，还有一个发送到全部的real真身帖子  add by yanjun 20170807
			if(searchNamesapceFlag){
				//全部 -- 查询各个目标的（正常），或者发送到“全部”的（clone、正常）
				orgCondition = orgCondition.and(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode()));
				orgCondition = orgCondition.or(Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode()));

			}else{
				//单个 -- 发送到单个目标的（clone、正常），或者发送到“全部”（正常）
				orgCondition = orgCondition.and(Tables.EH_ACTIVITIES.VISIBLE_REGION_ID.eq(organizationId));
				orgCondition = orgCondition.or(Tables.EH_ACTIVITIES.VISIBLE_REGION_TYPE.eq(VisibleRegionType.ALL.getCode())
						.and(Tables.EH_ACTIVITIES.CLONE_FLAG.eq(PostCloneFlag.NORMAL.getCode())));
			}

        }

        Condition visibleCondition = communityCondition;
        if(visibleCondition == null) {
            visibleCondition = orgCondition;
        } else {
        	if (orgCondition != null) {
        		visibleCondition = visibleCondition.or(orgCondition);
			}
        }
        
        Condition condition = activityCondition;
        if(visibleCondition != null) {
            condition = condition.and(visibleCondition);
            //搜索的时候不要搜出真身帖 add by yanjun 2171011
            condition = condition.and(Tables.EH_ACTIVITIES.CLONE_FLAG.ne(PostCloneFlag.REAL.getCode()));
        }

		//删除官方标志  使用CATEGORY_ID， 不传默认使用CATEGORY_ID为1，详见前面condition条件 edit by yanjun 20170712
        //condition = condition.and(Tables.EH_ACTIVITIES.OFFICIAL_FLAG.eq(OfficialFlag.YES.getCode()));

        // 添加活动状态筛选     add by xq.tian  2017/01/24
        if (cmd.getActivityStatusList() != null) {
            Condition statusCondition = this.buildActivityProcessStatusCondition(cmd.getActivityStatusList());
            if (statusCondition != null) {
                condition = condition.and(statusCondition);
            }
        }

		//支持标签搜索  add by yanjun 20170712
		if(!StringUtils.isEmpty(cmd.getTag())){
			condition = condition.and(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
		}
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        // TODO: Locator里设置系统论坛ID存在着分区的风险，因为上面的条件是多个论坛，需要后面理顺  by lqs 20160730
        CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(cmd.getPageAnchor());

        Boolean orderByCreateTime = false;
        if(cmd.getOrderByCreateTime() != null && cmd.getOrderByCreateTime() == 1) {
            orderByCreateTime = true;
        }
        List<ActivityDTO> dtos = this.getOrgActivities(locator, pageSize, condition, cmd.getPublishStatus(), orderByCreateTime, cmd.getNeedTemporary());
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query offical activities, userId=" + operatorId + ", size=" + dtos.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }   
        
        ListActivitiesReponse response = new ListActivitiesReponse(locator.getAnchor(), dtos);
        return response;
	}
	
	private List<ActivityDTO> getOrgActivities(CrossShardListingLocator locator,Integer pageSize, Condition condition, String publishStatus, Boolean orderByCreateTime, Byte needTemporary){
    	User user = UserContext.current().getUser();
    	
    	Timestamp timestemp = new Timestamp(DateHelper.currentGMTTime().getTime());
    	
        if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.UNPUBLISHED){
        	condition = condition.and(Tables.EH_ACTIVITIES.START_TIME.gt(timestemp));
        }
        
        if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.PUBLISHED){
        	condition = condition.and(Tables.EH_ACTIVITIES.START_TIME.lt(timestemp));
        	condition = condition.and(Tables.EH_ACTIVITIES.END_TIME.gt(timestemp));
        }
        
        if(TopicPublishStatus.fromCode(publishStatus) == TopicPublishStatus.EXPIRED){
        	condition = condition.and(Tables.EH_ACTIVITIES.END_TIME.lt(timestemp));
        }

        if(orderByCreateTime == null) {
            orderByCreateTime = false;
        }
        List<Activity> activities = this.activityProvider.listActivities(locator, pageSize + 1, condition, orderByCreateTime, needTemporary);

        //我勒个去呀，下面if和else的代码实一样的，先注释一块   edit by yanjun 20170830
        //if(orderByCreateTime) {
		List<ActivityDTO> activityDtos = activities.stream().map(activity -> {
			Post post = forumProvider.findPostById(activity.getPostId());
			if (post == null || post.getStatus() == null || post.getStatus().equals(PostStatus.INACTIVE.getCode())) {
				return null;
			}

			// 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170807
			if(PostCloneFlag.fromCode(activity.getCloneFlag()) == PostCloneFlag.CLONE){
				post = forumProvider.findPostById(post.getRealPostId());
				activity = activityProvider.findSnapshotByPostId(post.getId());
			}

			if (activity.getPosterUri() == null) {
				this.forumProvider.populatePostAttachments(post);
				List<Attachment> attachmentList = post.getAttachments();
				if (attachmentList != null && attachmentList.size() != 0) {
					for (Attachment attachment : attachmentList) {
						if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
							activity.setPosterUri(attachment.getContentUri());
						break;
					}
				}
			}
			ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
			dto.setActivityId(activity.getId());
			dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
			dto.setEnrollUserCount(activity.getSignupAttendeeCount());
			dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
			dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
			dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
			dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
			dto.setProcessStatus(getStatus(activity).getCode());
			dto.setFamilyId(activity.getCreatorFamilyId());
			dto.setStartTime(activity.getStartTime().toString());
			dto.setStopTime(activity.getEndTime().toString());
			dto.setSignupEndTime(getSignupEndTime(activity).toString());
			dto.setGroupId(activity.getGroupId());
			dto.setPosterUrl(getActivityPosterUrl(activity));
			if (user != null) {
				List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
				if (favorite == null || favorite.size() == 0) {
					dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
				} else {
					dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
				}
				//add UserActivityStatus by xiongying 20160628
				ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
				dto.setUserActivityStatus(getActivityStatus(roster).getCode());
			}else {
				dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
			}
			fixupVideoInfo(dto);

			Byte flag = forumService.getInteractFlagByPost(post);
			dto.setInteractFlag(flag);
			return dto;
		}).filter(r -> r != null).collect(Collectors.toList());

		return activityDtos;
//        } else {
//            List<ActivityDTO> activityDtos = activities.stream().map(activity -> {
//                Post post = forumProvider.findPostById(activity.getPostId());
//                if (post == null || post.getStatus() == null || post.getStatus().equals(PostStatus.INACTIVE.getCode())) {
//                    return null;
//                }
//
//				// 如果是clone帖子，则寻找它的真身帖子和真身活动   add by yanjun 20170807
//				if(PostCloneFlag.fromCode(activity.getCloneFlag()) == PostCloneFlag.CLONE){
//					post = forumProvider.findPostById(post.getRealPostId());
//					activity = activityProvider.findSnapshotByPostId(post.getId());
//				}
//                if (activity.getPosterUri() == null) {
//                    this.forumProvider.populatePostAttachments(post);
//                    List<Attachment> attachmentList = post.getAttachments();
//                    if (attachmentList != null && attachmentList.size() != 0) {
//                        for (Attachment attachment : attachmentList) {
//                            if (PostContentType.IMAGE.getCode().equals(attachment.getContentType()))
//                                activity.setPosterUri(attachment.getContentUri());
//                            break;
//                        }
//                    }
//                }
//                ActivityDTO dto = ConvertHelper.convert(activity, ActivityDTO.class);
//                dto.setActivityId(activity.getId());
//                dto.setEnrollFamilyCount(activity.getSignupFamilyCount());
//                dto.setEnrollUserCount(activity.getSignupAttendeeCount());
//                dto.setCheckinUserCount(activity.getCheckinAttendeeCount());
//                dto.setCheckinFamilyCount(activity.getCheckinFamilyCount());
//                dto.setConfirmFlag(activity.getConfirmFlag() == null ? 0 : activity.getConfirmFlag().intValue());
//                dto.setCheckinFlag(activity.getSignupFlag() == null ? 0 : activity.getSignupFlag().intValue());
//                dto.setProcessStatus(getStatus(activity).getCode());
//                dto.setFamilyId(activity.getCreatorFamilyId());
//                dto.setStartTime(activity.getStartTime().toString());
//                dto.setStopTime(activity.getEndTime().toString());
//                dto.setSignupEndTime(getSignupEndTime(activity).toString());
//                dto.setGroupId(activity.getGroupId());
//                dto.setPosterUrl(getActivityPosterUrl(activity));
//                if (user != null) {
//                	List<UserFavoriteDTO> favorite = userActivityProvider.findFavorite(user.getId(), UserFavoriteTargetType.ACTIVITY.getCode(), activity.getPostId());
//                    if (favorite == null || favorite.size() == 0) {
//                        dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
//                    } else {
//                        dto.setFavoriteFlag(PostFavoriteFlag.FAVORITE.getCode());
//                    }
//                    //add UserActivityStatus by xiongying 20160628
//                    ActivityRoster roster = activityProvider.findRosterByUidAndActivityId(activity.getId(), user.getId(), ActivityRosterStatus.NORMAL.getCode());
//                    dto.setUserActivityStatus(getActivityStatus(roster).getCode());
//				}else {
//					dto.setFavoriteFlag(PostFavoriteFlag.NONE.getCode());
//				}
//                fixupVideoInfo(dto);
//                return dto;
//                //全部查速度太慢，先把查出的部分排序 by xiongying20161208
//             // 产品妥协了，改成按开始时间倒序排列，add by tt, 20170117
//            })./*filter(r->r!=null).sorted((p1, p2) -> p2.getStartTime().compareTo(p1.getStartTime())).sorted((p1, p2) -> p1.getProcessStatus().compareTo(p2.getProcessStatus())).*/filter(r -> r != null).collect(Collectors.toList());

//            return activityDtos;
//        }
    }

	private void processOfficalActivitySceneToken(Long userId, SceneTokenDTO sceneTokenDTO, QueryOrganizationTopicCommand cmd) {
		Long organizationId = null;
		Long communityId = null;
	    SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());
        //检查游客是否能继续访问此场景 by sfyan 20161009
        userService.checkUserScene(sceneType);
	    switch(sceneType) {
	    case DEFAULT:
	    case PARK_TOURIST:
	        communityId = sceneTokenDTO.getEntityId();
			List<OrganizationCommunityDTO> list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
			if (list != null && list.size() > 0) {
				organizationId = list.get(0).getOrganizationId();
			}
	        break;
	    case FAMILY:
	        FamilyDTO family = familyProvider.getFamilyById(sceneTokenDTO.getEntityId());
	        if(family != null) {
	            communityId = family.getCommunityId();
	            list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
				if (list != null && list.size() > 0) {
					organizationId = list.get(0).getOrganizationId();
				}
	        } else {
	            if(LOGGER.isWarnEnabled()) {
	                LOGGER.warn("Family not found, sceneToken=" + sceneTokenDTO);
	            }
	        }
	        break;
        case ENTERPRISE: 
        case ENTERPRISE_NOAUTH: 
            // 对于普通公司，也需要取到其对应的管理公司，以便拿到管理公司所发的公告 by lqs 20160730
            OrganizationDTO org = organizationService.getOrganizationById(sceneTokenDTO.getEntityId());
            if(org != null) {
                communityId = org.getCommunityId();
                if(communityId == null) {
                    LOGGER.error("No community found for organization, organizationId={}, cmd={}, sceneToken={}", 
                        sceneTokenDTO.getEntityId(), cmd, sceneTokenDTO);
                } else {
                    list = organizationProvider.findOrganizationCommunityByCommunityId(communityId);
                    if (list != null && list.size() > 0) {
                        organizationId = list.get(0).getOrganizationId();
                    }
                }
            } else {
                LOGGER.error("Organization not found, organizationId={}, cmd={}, sceneToken={}", sceneTokenDTO.getEntityId(), cmd, sceneTokenDTO);
            }
            break;
        case PM_ADMIN:
        	organizationId = sceneTokenDTO.getEntityId();
        	org = organizationService.getOrganizationById(sceneTokenDTO.getEntityId());
            if(org != null) {
                communityId = org.getCommunityId();
                if(communityId == null) {
                    LOGGER.error("No community found for organization, organizationId={}, cmd={}, sceneToken={}", 
                        sceneTokenDTO.getEntityId(), cmd, sceneTokenDTO);
                } 
            }
            break;
	    default:
	        LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDTO);
	        break;
	    }
	    
	    cmd.setOrganizationId(organizationId);
	    // 补充小区/园区ID，方便后面构建查询条件：既要查本园区的官方活动，又要查对应的管理公司发给所有园区的官方活动 by lqs 20160730
	    cmd.setCommunityId(communityId);
	}

	@Override
	public ListOfficialActivityByNamespaceResponse listOfficialActivityByNamespace(
			ListOfficialActivityByNamespaceCommand cmd) {
		if (cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters, cmd="+cmd);
		}
		ListPostCommandResponse postResponse = forumService.listOfficialActivityByNamespace(cmd);
		List<PostDTO> posts = postResponse.getPosts();
		final List<ActivityDTO> activities = new ArrayList<>();
		if (posts != null && posts.size() > 0) {
			posts.forEach(p->{
				//吐槽：这里ActivityPostCommand和ActivityDTO中相同的字段，名字竟然不一样，如postUri和postUrl
				ActivityDTO activity = (ActivityDTO) StringHelper.fromJsonString(p.getEmbeddedJson().replace("posterUri", "posterUrl"), ActivityDTO.class);
				activity.setFavoriteFlag(p.getFavoriteFlag());
				activities.add(activity);
			});
		}
		
		ListOfficialActivityByNamespaceResponse reponse = new ListOfficialActivityByNamespaceResponse(postResponse.getNextPageAnchor(), activities);
		return reponse;
	}
	
	@Override
	public UserVideoPermissionDTO requestVideoPermission(RequestVideoPermissionCommand cmd) {
        User user = UserContext.current().getUser();
        UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.YZB_VIDEO_PERMISION);
        if(null != profile) {
            profile.setItemValue(cmd.toString());
            userActivityProvider.updateUserProfile(profile);
        } else {
            UserProfile p2 = new UserProfile();
            p2.setItemName(UserProfileContstant.YZB_VIDEO_PERMISION);
            p2.setItemKind((byte)0);
            p2.setItemValue(cmd.toString());
            p2.setOwnerId(user.getId());
            userActivityProvider.addUserProfile(p2);
        }
        
        return GetVideoPermisionInfo(new GetVideoPermissionInfoCommand());
	}
	
	@Override
	public UserVideoPermissionDTO GetVideoPermisionInfo(GetVideoPermissionInfoCommand cmd) {
	    UserVideoPermissionDTO dto = new UserVideoPermissionDTO();
	    User user = UserContext.current().getUser();
	    UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.YZB_VIDEO_PERMISION);
        if(profile == null || null == profile.getItemValue()) {
            return dto;
        }
        
        RequestVideoPermissionCommand req = (RequestVideoPermissionCommand)StringHelper.fromJsonString(profile.getItemValue(), RequestVideoPermissionCommand.class);
        
        dto.setVideoToken(req.getVideoToken());
        dto.setSessionId(req.getSessionId());
        
        return dto;
	}


	//live from normal user
	private ActivityVideoDTO setUserActivityVideo(SetActivityVideoInfoCommand cmd) {
	    ActivityVideo video = new ActivityVideo();
        //app sdk
        video.setIntegralTag1(0l);
        video.setVideoSid(cmd.getVid());
        
        YzbDevice oldDev = yzbDeviceProvider.findYzbDeviceByActivityId(cmd.getActivityId());
        if(oldDev != null) {
            oldDev.setState(VideoState.UN_READY.getCode());
            yzbDeviceProvider.updateYzbDevice(oldDev);
            
            yzbVideoService.setContinue(oldDev.getDeviceId(), 0); 
        }
        
        User user = UserContext.current().getUser();
        
        if(video.getVideoSid() == null) {
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_VIDEO_PARAM_ERROR, "video client params error");
        }
        
        ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(cmd.getActivityId());
        if(oldVideo != null) {
            //new activity, delete the old one
            oldVideo.setVideoState(VideoState.INVALID.getCode());
            activityVideoProvider.updateActivityVideo(oldVideo);
        }
        
        video.setCreatorUid(user.getId());
        video.setManufacturerType(VideoManufacturerType.YZB.toString());
        video.setRoomType(ActivityVideoRoomType.YZB.toString());
        video.setIntegralTag1(0l);

        VideoState videoState = VideoState.fromCode(cmd.getState());

        if (videoState != null) {
            video.setVideoState(videoState.getCode());
        }

        // 直播开始，设置开始时间为当前时间
        if (videoState == VideoState.LIVE) {
            video.setStartTime(System.currentTimeMillis());
        } else if (videoState == VideoState.RECORDING && oldVideo != null) {
            // 直播结束，设置这条记录的开始时间为直播开始时间
            video.setStartTime(oldVideo.getStartTime());
        }

        video.setOwnerType("activity");
        video.setOwnerId(cmd.getActivityId());
        activityVideoProvider.createActivityVideo(video);
        ActivityVideoDTO dto = ConvertHelper.convert(video, ActivityVideoDTO.class);
        dto.setVideoUrl("yzb://" + video.getVideoSid());
        dto.setRmtp("");
        
        return dto;
	}
	
	@Override
	public ActivityVideoDTO setActivityVideo(SetActivityVideoInfoCommand cmd) {	   
	    if(cmd.getRoomId() == null) {
	        return setUserActivityVideo(cmd);
	    } else {
	        //live from device
	        ActivityVideo video = new ActivityVideo();
	        String rmtp = "";
	        boolean setContinue = true;
	        
            //default, use device
	        video.setRoomId(cmd.getRoomId());
	        video.setIntegralTag1(1l);
	        video.setVideoState(VideoState.UN_READY.getCode());
	        video.setVideoSid("");
            video.setStartTime(System.currentTimeMillis());
	        
	        YzbDevice device = null;
	        YzbDevice oldDev = yzbDeviceProvider.findYzbDeviceByActivityId(cmd.getActivityId());
	        if(oldDev != null && oldDev.getRoomId().equals(cmd.getRoomId())) {
	            //found old device
	            device = oldDev;
	            oldDev = null;
	        } else {
	            device = yzbDeviceProvider.findYzbDeviceById(cmd.getRoomId());    
	        }
            if(oldDev != null) {
                //set old device to no ready
                oldDev.setState(VideoState.UN_READY.getCode());
                yzbDeviceProvider.updateYzbDevice(oldDev);
                yzbVideoService.setContinue(oldDev.getDeviceId(), 0);
            }
	        
	        if(device == null) {
	            //create new device
	            device = new YzbDevice();
	            device.setDeviceId(cmd.getRoomId());
	            if(cmd.getNamespaceId() == null) {
	                device.setNamespaceId(UserContext.getCurrentNamespaceId());
	            }
	            device.setState(VideoState.UN_READY.getCode());
	            device.setStatus((byte)1); //valid
               device.setRelativeType("activity");
               device.setRelativeId(0l);
               device.setLastVid("");
               device.setRoomId(cmd.getRoomId());
	            yzbDeviceProvider.createYzbDevice(device);
	            
	            setContinue = false;
	        } else {
	            if(!cmd.getActivityId().equals(device.getRelativeId())
	                    && device.getState().equals(VideoState.LIVE.getCode())) {
	                LOGGER.warn("new living but device is already living, cmd=" + cmd + " device=" + device.getRelativeId());
	                //Close old living right now
	                ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(device.getRelativeId());
	                if(oldVideo != null 
	                        && oldVideo.getVideoSid() != null 
	                        && !oldVideo.getVideoState().equals(VideoState.RECORDING.getCode())
	                        ) {
	                    oldVideo.setVideoState(VideoState.RECORDING.getCode());
	                    oldVideo.setEndTime(System.currentTimeMillis());
	                    activityVideoProvider.updateActivityVideo(oldVideo);
	                }
	            }
	            
	            if(device.getRelativeId() == null 
	                    || !cmd.getActivityId().equals(device.getRelativeId())
	                    || !device.getState().equals(VideoState.LIVE.getCode())) {
	                setContinue = false;
	            }
	            
	        }
	        
            ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(cmd.getActivityId());
            if(oldVideo != null) {
                video.setVideoSid(oldVideo.getVideoSid());
                video.setVideoState(oldVideo.getVideoState());
                video.setStartTime(oldVideo.getStartTime());
                
                //new activity, delete the old one
                oldVideo.setVideoState(VideoState.INVALID.getCode());
                activityVideoProvider.updateActivityVideo(oldVideo);
            }
            
            if(setContinue) {
                //continue use old vid
                yzbVideoService.setContinue(cmd.getRoomId(), 1);
            } else {
                //start new vid
               yzbVideoService.setContinue(cmd.getRoomId(), 0);
            }
            
            if(cmd.getActivityId().equals(device.getRelativeId())) {
                video.setVideoSid(device.getLastVid());
            }
            
            device.setRelativeId(cmd.getActivityId());
            yzbDeviceProvider.updateYzbDevice(device); 
            
            User user = UserContext.current().getUser();
            video.setCreatorUid(user.getId());
            video.setManufacturerType(VideoManufacturerType.YZB.toString());
            video.setRoomType(ActivityVideoRoomType.YZB.toString());
            video.setOwnerType("activity");
            video.setOwnerId(cmd.getActivityId());
            activityVideoProvider.createActivityVideo(video);
            ActivityVideoDTO dto = ConvertHelper.convert(video, ActivityVideoDTO.class);
            dto.setVideoUrl("yzb://" + video.getVideoSid());
            dto.setRmtp(rmtp);
            
            return dto;
	    }
	}
	
   @Override
    public ActivityVideoDTO getActivityVideo(GetActivityVideoInfoCommand cmd) {
       ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(cmd.getActivityId());
	   if(null != video){
		   ActivityVideoDTO dto = ConvertHelper.convert(video, ActivityVideoDTO.class);
		   if(null != video.getVideoSid())
			   dto.setVideoUrl("yzb://" + video.getVideoSid());
		   return dto;
	   }
       return null;
    }
   
   @Override
   public void createScheduleForActivity(Activity act) {
       if(act.getIsVideoSupport() == null || act.getIsVideoSupport() <= 0) {
           return;
       }
       
       String triggerName = YzbConstant.SCHEDULE_TARGET_NAME + System.currentTimeMillis();
       String jobName = triggerName;
       
       Long now = System.currentTimeMillis();
       Long endTime = act.getEndTimeMs();
       if(endTime == null) {
           endTime = act.getEndTime().getTime();
       }
       
       Map<String, Object> map = new HashMap<String, Object>();
       //String cronExpression = "0/5 * * * * ?";
       map.put("id", act.getId().toString());
       map.put("endTime", endTime.toString());
       map.put("now", now.toString());
       
       scheduleProvider.scheduleSimpleJob(triggerName, jobName, new Date(endTime + 60*1000), ActivityVideoScheduleJob.class, map);
   }
   
   @Override
   public void onActivityFinished(Long activityId, Long endTime) {
       Activity activity = activityProvider.findActivityById(activityId);
       if(activity == null || !activity.getEndTimeMs().equals(endTime)) {
           LOGGER.warn("invalid activityId=" + activityId + " endTime=" + endTime);
           return;
       }
       
       ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByActivityId(activityId);
       if(oldVideo != null) {
           if(oldVideo.getVideoState().equals(VideoState.LIVE.getCode())) {
               oldVideo.setVideoState(VideoState.RECORDING.getCode());
               oldVideo.setEndTime(System.currentTimeMillis());
               activityVideoProvider.updateActivityVideo(oldVideo);
           }
           
           YzbDevice dev = yzbDeviceProvider.findYzbDeviceByActivityId(activityId);
           if(dev != null && dev.getState().equals(VideoState.LIVE.getCode())) {
               dev.setState(VideoState.UN_READY.getCode());
               dev.setRelativeId(0l);
               yzbDeviceProvider.updateYzbDevice(dev);
           }
           
       }
   }
   
   @Override 
   public void onVideoDeviceChange(YzbVideoDeviceChangeCommand cmd) {
       LOGGER.info("video ondevicechange=" + cmd);
       if(cmd.getDevid() == null) {
           return;
       }
       
       YzbDevice device = yzbDeviceProvider.findYzbDeviceById(cmd.getDevid());
       if(device == null) {
       return;    
       }
       
       if(cmd.getOptcode().equals("livestart")) {
           ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(device.getRelativeId());
           LOGGER.info("video ondevicechange, video=" + video);
           if(video != null && video.getIntegralTag1().equals(1l) 
                   && !video.getVideoState().equals(VideoState.RECORDING.getCode())
                   && cmd.getVid() != null && !cmd.getVid().isEmpty()) {
               video.setVideoSid(cmd.getVid());
               video.setVideoState(VideoState.LIVE.getCode());
               activityVideoProvider.updateActivityVideo(video);

               device.setLastVid(cmd.getVid());
               device.setState(VideoState.LIVE.getCode());
               yzbDeviceProvider.updateYzbDevice(device);
               
           }
       } else if(cmd.getOptcode().equals("livestop")) {
           LOGGER.info("video livestop");
           device.setState(VideoState.UN_READY.getCode());
           yzbDeviceProvider.updateYzbDevice(device);
//           ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(device.getRelativeId());
//           if(video != null && video.getVideoState().equals(VideoState.LIVE.getCode())) {
//               video.setVideoState(VideoState.RECORDING.getCode());
//               activityVideoProvider.updateActivityVideo(video);
//           }
           
       }
      
   }
   
   @Override
   public VideoCapabilityResponse getVideoCapability(GetVideoCapabilityCommand cmd) {

       Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
   
       VideoCapabilityResponse obj = new VideoCapabilityResponse();
       if(cmd.getOfficialFlag() == null || cmd.getOfficialFlag().equals(OfficialFlag.NO.getCode())) {
           Long official = this.configurationProvider.getLongValue(namespaceId
                   , YzbConstant.VIDEO_NONE_OFFICIAL_SUPPORT, (long)VideoSupportType.NO_SUPPORT.getCode());
           obj.setVideoSupportType(official.byteValue());
       } else {
           Long official = this.configurationProvider.getLongValue(namespaceId
                   , YzbConstant.VIDEO_OFFICIAL_SUPPORT, (long)VideoSupportType.NO_SUPPORT.getCode());
           obj.setVideoSupportType(official.byteValue()); 
       }
       return obj;
   }
   
   private void fixupVideoInfo(ActivityDTO dto) {
       if(dto.getVideoUrl() != null) {
           return;
       }
       
       if(dto.getIsVideoSupport() == null) {
           dto.setIsVideoSupport((byte)0);
       }
       dto.setVideoState(VideoState.UN_READY.getCode());
       
       if(dto.getIsVideoSupport() != null && dto.getIsVideoSupport().byteValue() > 0) {
           ActivityVideo video = activityVideoProvider.getActivityVideoByActivityId(dto.getActivityId());
           if(video != null && video.getVideoSid() != null) {
               dto.setVideoUrl("yzb://" + video.getVideoSid());
               dto.setVideoState(video.getVideoState());
           }
       }
   }

	@Override
	public GetActivityDetailByIdResponse getActivityDetailById(GetActivityDetailByIdCommand cmd) {
		return forumService.getActivityDetailById(cmd);
	}

	@Override
	public ActivityWarningResponse setActivityWarning(SetActivityWarningCommand cmd) {
		if (cmd.getNamespaceId()==null ||cmd.getCategoryId() == null || cmd.getDays() == null
				|| cmd.getHours() == null || cmd.getHours().intValue() == 0) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cmd="+cmd);
		}
		WarningSetting warningSetting = findWarningSetting(cmd.getNamespaceId(), cmd.getCategoryId());
		if (warningSetting != null && warningSetting.getId() != null) {
			warningSetting.setTime((long) ((cmd.getDays()*24+cmd.getHours())*3600*1000));
			warningSetting.setUpdateTime(warningSetting.getCreateTime());
			warningSetting.setOperatorUid(warningSetting.getCreatorUid());
			
			warningSettingProvider.updateWarningSetting(warningSetting);
		}else {
			warningSetting = new WarningSetting();
			warningSetting.setNamespaceId(cmd.getNamespaceId());
			warningSetting.setCategoryId(cmd.getCategoryId());
			warningSetting.setTime((long) ((cmd.getDays()*24+cmd.getHours())*3600*1000));
			warningSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			warningSetting.setCreatorUid(UserContext.current().getUser().getId());
			warningSetting.setUpdateTime(warningSetting.getCreateTime());
			warningSetting.setOperatorUid(warningSetting.getCreatorUid());
			warningSetting.setType(EhActivities.class.getSimpleName());
			
			warningSettingProvider.createWarningSetting(warningSetting);
		}
		
		return ConvertHelper.convert(cmd, ActivityWarningResponse.class);
	}

	@Override
	public ActivityWarningResponse queryActivityWarning(GetActivityWarningCommand cmd) {
		if (cmd.getNamespaceId()==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cmd="+cmd);
		}
		
		WarningSetting warningSetting = findWarningSetting(cmd.getNamespaceId(), cmd.getCategoryId());
		
		if (warningSetting != null) {
			Integer days = (int) (warningSetting.getTime() / 1000 / 3600 / 24);
			Integer hours  = (int) (warningSetting.getTime() / 1000 / 3600 % 24);
			return new ActivityWarningResponse(warningSetting.getNamespaceId(), cmd.getCategoryId(), days, hours, warningSetting.getTime());
		}
		
		return new ActivityWarningResponse(cmd.getNamespaceId(), cmd.getCategoryId(), 0, 1, 3600*1000L);
	}
	
	private WarningSetting findWarningSetting(Integer namespaceId, Long categoryId){
		WarningSetting warningSetting =  warningSettingProvider.findWarningSettingByNamespaceAndType(namespaceId, categoryId, EhActivities.class.getSimpleName());
		if (warningSetting == null) {
			warningSetting = new WarningSetting();
			warningSetting.setNamespaceId(namespaceId);
			warningSetting.setCategoryId(categoryId);
			warningSetting.setTime(3600*1000L);
		}
		return warningSetting;
	}
	
	@Override
	public RosterOrderSettingDTO setRosterOrderSetting(SetRosterOrderSettingCommand cmd) {
		if(cmd.getNamespaceId()==null || cmd.getCategoryId() == null
				|| cmd.getDays() == null || cmd.getHours() == null ){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cmd="+cmd);
		}
		RosterOrderSetting rosterOrderSetting = rosterOrderSettingProvider.findRosterOrderSettingByNamespace(cmd.getNamespaceId(), cmd.getCategoryId());
		if (rosterOrderSetting != null && rosterOrderSetting.getId() != null) {
			rosterOrderSetting.setTime(((long)( cmd.getDays()*24+cmd.getHours()))*3600*1000);
			rosterOrderSetting.setUpdateTime(rosterOrderSetting.getCreateTime());
			rosterOrderSetting.setOperatorUid(rosterOrderSetting.getCreatorUid());
			rosterOrderSetting.setWechatSignup(cmd.getWechatSignup());
			
			rosterOrderSettingProvider.updateRosterOrderSetting(rosterOrderSetting);
		}else {
			rosterOrderSetting = new RosterOrderSetting();
			rosterOrderSetting.setNamespaceId(cmd.getNamespaceId());
			rosterOrderSetting.setCategoryId(cmd.getCategoryId());
			rosterOrderSetting.setTime(((long)( cmd.getDays()*24+cmd.getHours()))*3600*1000);
			rosterOrderSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rosterOrderSetting.setCreatorUid(UserContext.current().getUser().getId());
			rosterOrderSetting.setUpdateTime(rosterOrderSetting.getCreateTime());
			rosterOrderSetting.setOperatorUid(rosterOrderSetting.getCreatorUid());
			rosterOrderSetting.setWechatSignup(cmd.getWechatSignup());
			
			rosterOrderSettingProvider.createRosterOrderSetting(rosterOrderSetting);
		}
		RosterOrderSettingDTO dto = ConvertHelper.convert(rosterOrderSetting, RosterOrderSettingDTO.class);
		dto.setDays(cmd.getDays());
		dto.setHours(cmd.getHours());
		return dto;
	}

	@Override
	public RosterOrderSettingDTO getRosterOrderSetting(GetRosterOrderSettingCommand cmd) {
		if (cmd.getNamespaceId()==null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cmd="+cmd);
		}
		
		RosterOrderSetting rosterOrderSetting = rosterOrderSettingProvider.findRosterOrderSettingByNamespace(cmd.getNamespaceId(), cmd.getCategoryId());
		
		if (rosterOrderSetting != null) {
			Integer days = (int) (rosterOrderSetting.getTime() / 1000 / 3600 / 24);
			Integer hours  = (int) (rosterOrderSetting.getTime() / 1000 / 3600 % 24);
			return new RosterOrderSettingDTO(rosterOrderSetting.getNamespaceId(), cmd.getCategoryId(), days, hours,
					rosterOrderSetting.getTime(), rosterOrderSetting.getWechatSignup());
		}
		
		return new RosterOrderSettingDTO(cmd.getNamespaceId(), cmd.getCategoryId(), 1, 0, (1*24)*3600*1000L, WechatSignupFlag.YES.getCode());
	}
	

	@Override
	public ActivityTimeResponse setActivityTime(SetActivityTimeCommand cmd) {
		ActivityTimeResponse timeResponse = new ActivityTimeResponse();
		timeResponse.setNamespaceId(cmd.getNamespaceId());
		timeResponse.setCategoryId(cmd.getCategoryId());

		SetActivityWarningCommand warningCmd = new SetActivityWarningCommand();
		warningCmd.setNamespaceId(cmd.getNamespaceId());
		warningCmd.setDays(cmd.getWarningDays());
		warningCmd.setHours(cmd.getWarningHours());
		warningCmd.setCategoryId(cmd.getCategoryId());
		ActivityWarningResponse  warningResponse  = this.setActivityWarning(warningCmd);
		timeResponse.setWarningDays(warningResponse.getDays());
		timeResponse.setWarningHours(warningResponse.getHours());
		timeResponse.setWarningTime(warningResponse.getTime());

		
		SetRosterOrderSettingCommand orderCommand = new SetRosterOrderSettingCommand();
		orderCommand.setNamespaceId(cmd.getNamespaceId());
		orderCommand.setCategoryId(cmd.getCategoryId());
		orderCommand.setDays(cmd.getOrderDays());
		orderCommand.setHours(cmd.getOrderHours());
		orderCommand.setWechatSignup(cmd.getWechatSignup());
		RosterOrderSettingDTO orderResponse = this.setRosterOrderSetting(orderCommand);
		timeResponse.setOrderDays(orderResponse.getDays());
		timeResponse.setOrderHours(orderResponse.getHours());
		timeResponse.setOrderTime(orderResponse.getTime());
		timeResponse.setWechatSignup(orderResponse.getWechatSignup());

		//更新评论开关
		forumService.saveInteractSetting(cmd.getNamespaceId(), ForumModuleType.ACTIVITY.getCode(), cmd.getCategoryId(), cmd.getInteractFlag());
		timeResponse.setInteractFlag(cmd.getInteractFlag());

		return timeResponse;
	}

	@Override
	public ActivityTimeResponse getActivityTime(GetActivityTimeCommand cmd) {
		ActivityTimeResponse timeResponse = new ActivityTimeResponse();
		timeResponse.setNamespaceId(cmd.getNamespaceId());
		timeResponse.setCategoryId(cmd.getCategoryId());
		
		GetActivityWarningCommand warningCommand = new GetActivityWarningCommand();
		warningCommand.setNamespaceId(cmd.getNamespaceId());
		warningCommand.setCategoryId(cmd.getCategoryId());
		ActivityWarningResponse  warningResponse  = this.queryActivityWarning(warningCommand);
		timeResponse.setWarningDays(warningResponse.getDays());
		timeResponse.setWarningHours(warningResponse.getHours());
		timeResponse.setWarningTime(warningResponse.getTime());
		
		GetRosterOrderSettingCommand orderCommand = new GetRosterOrderSettingCommand();
		orderCommand.setNamespaceId(cmd.getNamespaceId());
		orderCommand.setCategoryId(cmd.getCategoryId());
		RosterOrderSettingDTO orderResponse = this.getRosterOrderSetting(orderCommand);
		timeResponse.setOrderDays(orderResponse.getDays());
		timeResponse.setOrderHours(orderResponse.getHours());
		timeResponse.setOrderTime(orderResponse.getTime());
		timeResponse.setWechatSignup(orderResponse.getWechatSignup());

		//评论设置
		InteractSetting interactSetting = forumProvider.findInteractSetting(cmd.getNamespaceId(), ForumModuleType.ACTIVITY.getCode(), cmd.getCategoryId());
		if(interactSetting == null){
			timeResponse.setInteractFlag(InteractFlag.SUPPORT.getCode());
		}else {
			timeResponse.setInteractFlag(interactSetting.getInteractFlag());
		}

		return timeResponse;
	}  
	
   
	/**
	 * 活动开始前的提醒，采用轮循+定时两种方式执行定时任务
	 * 轮循时按域空间设置的活动提前时间取出相应的活动（只取当前时间+n~当前时间+n+1之间的活动），再把这些活动设置成定时的任务
	 **/ 
    @Scheduled(cron="0 0 * * * ?")
    @Override
	public void activityWarningSchedule() {

		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			//使用tryEnter方法可以防止分布式部署时重复执行
			coordinationProvider.getNamedLock(CoordinationLocks.WARNING_ACTIVITY_SCHEDULE.getCode()).tryEnter(() -> {

				final Date now = DateUtils.getCurrentHour();
				List<NamespaceInfoDTO> namespaces = namespacesProvider.listNamespace();
				namespaces.add(new NamespaceInfoDTO(0, "zuolin", ""));

				//遍历每个域空间
				namespaces.forEach(n -> {
					//增加了入口的设置，要遍历每个域空间下每个入口的设置
					List<ActivityCategories> categories = activityProvider.listActivityCategory(n.getId(), null);
					if(categories == null || categories.size() == 0){
						setWarningSchedule(n, null, now);
					}else {
						for (int i = 0; i< categories.size(); i++){
							setWarningSchedule(n, categories.get(i).getEntryId(), now);
						}
					}

				});
			});
		}
	}

	private  void setWarningSchedule(NamespaceInfoDTO n, Long categoryId, Date now){
		WarningSetting warningSetting = findWarningSetting(n.getId(), categoryId);
		Timestamp queryStartTime = new Timestamp(now.getTime() + warningSetting.getTime());
		Timestamp queryEndTime = new Timestamp(now.getTime() + warningSetting.getTime() + 3600 * 1000);

		// 对于这个域空间时间范围内的活动，再单独设置定时任务
		List<Activity> activities = activityProvider.listActivitiesForWarning(n.getId(), categoryId, queryStartTime, queryEndTime);
		activities.forEach(a -> {
			if (a.getSignupAttendeeCount() != null && a.getSignupAttendeeCount() > 0 && a.getStartTime().getTime() - warningSetting.getTime() >= new Date().getTime()) {
				final Job job1 = new Job(
						WarnActivityBeginningAction.class.getName(),
						new Object[]{String.valueOf(a.getId())});

//        				jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
//        						new Date().getTime()+10000);
				jesqueClientFactory.getClientPool().delayedEnqueue(WarnActivityBeginningAction.QUEUE_NAME, job1,
						a.getStartTime().getTime() - warningSetting.getTime());
				LOGGER.debug("设置了一个活动提醒：" + a.getId());
			}
		});
	}
	@Override
	public List<ActivityCategoryDTO> listActivityEntryCategories(
			ListActivityEntryCategoriesCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<ActivityCategories> entityResultList = this.activityProvider.listActivityEntryCategories(namespaceId,
                cmd.getOwnerType(), cmd.getOwnerId(), null, CategoryAdminStatus.ACTIVE);
        return entityResultList.stream().map(r -> {
        	ActivityCategoryDTO dto = ConvertHelper.convert(r, ActivityCategoryDTO.class);
            return dto;
        }).collect(Collectors.toList());
	}

    @Override
    public ListActivityPromotionEntitiesBySceneReponse listActivityPromotionEntitiesByScene(ListActivityPromotionEntitiesBySceneCommand cmd) {

        ListNearbyActivitiesBySceneCommand listCmd = new ListNearbyActivitiesBySceneCommand();
        listCmd.setCategoryId(cmd.getCategoryId());
        listCmd.setSceneToken(cmd.getSceneToken());
        listCmd.setPageSize(cmd.getPageSize());
        listCmd.setPageAnchor(cmd.getPageAnchor());
        // 只要查询预告中与进行中的活动
        listCmd.setActivityStatusList(Arrays.asList(NOTSTART.getCode(), UNDERWAY.getCode()));

        ListActivitiesReponse activityReponse;
        if (OfficialFlag.fromCode(cmd.getPublishPrivilege()) == OfficialFlag.YES) {
            // 官方活动
            activityReponse = this.listOfficialActivitiesByScene(listCmd);
        } else {
            // 非官方活动
            activityReponse = this.listNearbyActivitiesByScene(listCmd);
        }
        ListActivityPromotionEntitiesBySceneReponse promotionReponse = new ListActivityPromotionEntitiesBySceneReponse();

        if (activityReponse != null && activityReponse.getActivities() != null) {
            List<ModulePromotionEntityDTO> entities = activityReponse.getActivities().stream()
                    .map(this::toModulePromotionEntityDTO).collect(Collectors.toList());
            promotionReponse.setEntities(entities);
        }
        return promotionReponse;
    }

    private ModulePromotionEntityDTO toModulePromotionEntityDTO(ActivityDTO activityDTO) {
        ModulePromotionEntityDTO dto = new ModulePromotionEntityDTO();
        dto.setId(activityDTO.getActivityId());
        dto.setDescription(activityDTO.getDescription());
        dto.setPosterUrl(activityDTO.getPosterUrl());
        dto.setSubject(activityDTO.getTag() + " | " + activityDTO.getSubject());

        Map<String, Long> metadata = new HashMap<>();
        metadata.put("forumId", activityDTO.getForumId());
        metadata.put("topicId", activityDTO.getPostId());

        dto.setMetadata(StringHelper.toJsonString(metadata));

        ModulePromotionInfoDTO infoDTO = new ModulePromotionInfoDTO();
        LocalDateTime startTime = LocalDateTime.parse(activityDTO.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"));
        infoDTO.setInfoType(ModulePromotionInfoType.TEXT.getCode());
        infoDTO.setContent(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dto.setInfoList(Collections.singletonList(infoDTO));
        return dto;
    }

    public void setActivityAchievement(SetActivityAchievementCommand cmd) {

        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }
        //敏感词过滤 start add by yanlong.liang 20180626
        Post post = this.forumProvider.findPostById(activity.getPostId());
        FilterWordsCommand command = new FilterWordsCommand();
        if (post != null) {
            command.setCommunityId(post.getVisibleRegionId());
            command.setModuleType(post.getModuleType());
        }
        List<String> list = new ArrayList<>();
        if (!StringUtils.isEmpty(cmd.getAchievement()) && !"link".equals(cmd.getAchievementType())) {
            list.add(cmd.getAchievement());
        }
        command.setTextList(list);
        this.sensitiveWordService.filterWords(command);
        // 敏感词过滤 end
        activity.setAchievement(cmd.getAchievement());
        activity.setAchievementType(cmd.getAchievementType());
        activity.setAchievementRichtextUrl(cmd.getAchievementRichtextUrl());
        activityProvider.updateActivity(activity);
    }

    @Override
    public GetActivityAchievementResponse getActivityAchievement(GetActivityAchievementCommand cmd) {
        Activity activity = activityProvider.findActivityById(cmd.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", cmd.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + cmd.getActivityId());
        }

        String achievement = activity.getAchievement();
        String achievementType = activity.getAchievementType();

        GetActivityAchievementResponse response = new GetActivityAchievementResponse();
        response.setAchievement(achievement);
        response.setAchievementType(achievementType);
        return response;
    }

    @Override
    public void createActivityAttachment(CreateActivityAttachmentCommand cmd) {
        ActivityAttachment attachment = ConvertHelper.convert(cmd, ActivityAttachment.class);
        attachment.setCreatorUid(UserContext.current().getUser().getId());
        ContentServerResource resource = contentServerService.findResourceByUri(cmd.getContentUri());
        Integer size = resource.getResourceSize();
        attachment.setFileSize(size);
        activityProvider.createActivityAttachment(attachment);
    }

    @Override
    public void deleteActivityAttachment(DeleteActivityAttachmentCommand cmd) {
        activityProvider.deleteActivityAttachment(cmd.getAttachmentId());
    }

    @Override
    public ListActivityAttachmentsResponse listActivityAttachments(ListActivityAttachmentsCommand cmd) {
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }

        ListActivityAttachmentsResponse response = new ListActivityAttachmentsResponse();
        List<ActivityAttachment> attachments = activityProvider.listActivityAttachments(locator, cmd.getPageSize() + 1, cmd.getActivityId());
        if(attachments != null && attachments.size() > 0) {
            if(attachments.size() > cmd.getPageSize()) {
                attachments.remove(attachments.size() - 1);
                response.setNextPageAnchor(attachments.get(attachments.size() - 1).getId());
            }

            List<ActivityAttachmentDTO> dtos = attachments.stream().map((r) -> {
                ActivityAttachmentDTO dto = ConvertHelper.convert(r, ActivityAttachmentDTO.class);
                String contentUrl = contentServerService.parserUri(dto.getContentUri(), EntityType.ACTIVITY.getCode(), dto.getActivityId());
                User creator = userProvider.findUserById(dto.getCreatorUid());
                if(creator != null) {
                    dto.setCreatorName(creator.getNickName());
                }
                dto.setContentUrl(contentUrl);
                return dto;
            }).collect(Collectors.toList());
            response.setAttachments(dtos);
        }

        return response;
    }

    @Override
    public void downloadActivityAttachment(DownloadActivityAttachmentCommand cmd) {

    	if(cmd.getAttachmentId() == null || cmd.getActivityId() == null) {
    		return ;
    	}
    	
        ActivityAttachment attachment = activityProvider.findByActivityAttachmentId(cmd.getAttachmentId());
        if(attachment == null) {
            LOGGER.error("handle activity attachment error ,the activity attachment does not exsit.cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ATTACHMENT_ID, "invalid activity attachment id " + cmd.getAttachmentId());
        }

        attachment.setDownloadCount(attachment.getDownloadCount() + 1);
        activityProvider.updateActivityAttachment(attachment);
    }


    private void filterGoods(ActivityGoods goods) {
        Activity activity = activityProvider.findActivityById(goods.getActivityId());
        if (activity == null) {
            LOGGER.error("handle activity error ,the activity does not exsit.id={}", goods.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID, "invalid activity id " + goods.getActivityId());
        }
        //敏感词过滤 start add by yanlong.liang 20180626
        Post post = this.forumProvider.findPostById(activity.getPostId());
        FilterWordsCommand command = new FilterWordsCommand();
        if (post != null) {
            command.setCommunityId(post.getVisibleRegionId());
            command.setModuleType(post.getModuleType());
        }
        List<String> list = new ArrayList<>();
        if (!StringUtils.isEmpty(goods.getName())) {
            list.add(goods.getName());
        }
        if (!StringUtils.isEmpty(goods.getHandlers())) {
            list.add(goods.getHandlers());
        }
        command.setTextList(list);
        this.sensitiveWordService.filterWords(command);
        // 敏感词过滤 end
    }
    @Override
    public void createActivityGoods(CreateActivityGoodsCommand cmd) {
        ActivityGoods goods = ConvertHelper.convert(cmd, ActivityGoods.class);
        goods.setCreatorUid(UserContext.current().getUser().getId());

        filterGoods(goods);

        activityProvider.createActivityGoods(goods);
    }

    @Override
    public void updateActivityGoods(UpdateActivityGoodsCommand cmd) {
        ActivityGoods goods = activityProvider.findActivityGoodsById(cmd.getId());
        if(goods == null) {
            LOGGER.error("handle activity goods error ,the activity goods does not exsit.cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_GOODS_ID, "invalid activity goods id " + cmd.getId());
        }

        goods.setName(cmd.getName());
        goods.setPrice(cmd.getPrice());
        goods.setQuantity(cmd.getQuantity());
        goods.setTotalPrice(cmd.getTotalPrice());
        goods.setHandlers(cmd.getHandlers());

        filterGoods(goods);

        activityProvider.updateActivityGoods(goods);
    }

    @Override
    public void deleteActivityGoods(DeleteActivityGoodsCommand cmd) {
        activityProvider.deleteActivityGoods(cmd.getGoodId());
    }

    @Override
    public ListActivityGoodsResponse listActivityGoods(ListActivityGoodsCommand cmd) {
        CrossShardListingLocator locator=new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if(cmd.getPageSize()==null){
            int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }

        ListActivityGoodsResponse response = new ListActivityGoodsResponse();
        List<ActivityGoods> goods = activityProvider.listActivityGoods(locator, cmd.getPageSize() + 1, cmd.getActivityId());

		response.setNextPageAnchor(null);
        if(goods != null && goods.size() > 0) {
            if(goods.size() > cmd.getPageSize()) {
                goods.remove(goods.size() - 1);
                response.setNextPageAnchor(goods.get(goods.size() - 1).getId());
            }

            List<ActivityGoodsDTO> dtos = goods.stream().map((r) -> {
                ActivityGoodsDTO dto = ConvertHelper.convert(r, ActivityGoodsDTO.class);
                return dto;
            }).collect(Collectors.toList());
            response.setGoods(dtos);
        }

        return response;
    }

    @Override
    public ActivityGoodsDTO getActivityGoods(GetActivityGoodsCommand cmd) {
        ActivityGoods goods = activityProvider.findActivityGoodsById(cmd.getGoodId());
        if(goods == null) {
            return null;
        } else {
            ActivityGoodsDTO dto = ConvertHelper.convert(goods, ActivityGoodsDTO.class);
            return dto;
        }
    }

	@Override
	public void videoCallback(VideoCallbackCommand cmd) {
		if(StringUtils.isEmpty(cmd.getLid()) || StringUtils.isEmpty(cmd.getModule())){
			LOGGER.warn("params is null");
			return;
		}
		User user = UserContext.current().getUser();

		VideoState videoState = null;
		if(cmd.getModule().equals("live")){
			if(cmd.getState() == 0){
				videoState = VideoState.UN_READY;
			}else if(cmd.getState() == 1){
				videoState = VideoState.LIVE;
			}else{
				//其他状态，不处理
				LOGGER.debug("lived Not handle. lid = {}, module = {}, from = {}", cmd.getLid(), cmd.getModule(), cmd.getFrom());
				return;
			}
		}else if(cmd.getModule().trim().equals("file") && !StringUtils.isEmpty(cmd.getFrom()) && cmd.getFrom().trim().equals("record")){
			if(cmd.getState() == -1){
				videoState = VideoState.EXCEPTION;
			}else if(cmd.getState() == 1){
				videoState = VideoState.RECORDING;
			}else{
				//其他状态，不处理
				LOGGER.debug("Recoding Not handle. lid = {}, module = {}, from = {}", cmd.getLid(), cmd.getModule(), cmd.getFrom());
				return;
			}
		}else{
			LOGGER.warn("params error. lid = {}, module = {}, from = {}", cmd.getLid(), cmd.getModule(), cmd.getFrom());
			return;
		}

		ActivityVideo oldVideo = activityVideoProvider.getActivityVideoByVid(cmd.getLid());
		if(oldVideo != null) {
			//new activity, delete the old one
			oldVideo.setVideoState(VideoState.INVALID.getCode());
			activityVideoProvider.updateActivityVideo(oldVideo);
		}else{
			LOGGER.warn("video Non-existent vid = {}", cmd.getLid());
			return;
		}

		ActivityVideo video = ConvertHelper.convert(oldVideo, ActivityVideo.class);
		video.setCreatorUid(user.getId());
		video.setVideoState(videoState.getCode());
		video.setId(null);
		if(videoState == VideoState.LIVE){
			if(cmd.getState() == 1){
				video.setStartTime(System.currentTimeMillis());
			}
		}
		activityVideoProvider.createActivityVideo(video);
	}

	@Override
	public ListActivityCategoryReponse listActivityCategory(ListActivityCategoryCommand cmd) {
		if (cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespaceId cannot be null");
		}
		
		List<ActivityCategories> ActivityCategoryList = activityProvider.listActivityCategory(cmd.getNamespaceId(), cmd.getCategoryId());
		
		List<ActivityCategoryDTO> resultList = ActivityCategoryList.stream().map(r->toActivityCategoryDTO(r)).collect(Collectors.toList());
		
		return new ListActivityCategoryReponse(resultList);
	}

	private ActivityCategoryDTO toActivityCategoryDTO(ActivityCategories activityCategory){
		ActivityCategoryDTO categoryDTO = ConvertHelper.convert(activityCategory, ActivityCategoryDTO.class);
		categoryDTO.setIconUrl(parserUri(categoryDTO.getIconUri(), EhActivityCategories.class.getSimpleName(), categoryDTO.getId()));
		categoryDTO.setSelectedIconUrl(parserUri(categoryDTO.getSelectedIconUri(), EhActivityCategories.class.getSimpleName(), categoryDTO.getId()));
		//真实的入口Id。老版本用id作为标识，新版本id无意义，使用entryId作为标识。 add by yanjun 20170524
		categoryDTO.setId(categoryDTO.getEntryId());
		if (categoryDTO.getSelectedIconUrl() == null) {
			categoryDTO.setSelectedIconUrl(categoryDTO.getIconUrl());
		}
		return categoryDTO;
	}
	
	private String parserUri(String uri,String ownerType, long ownerId){
		try {
			if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
				return contentServerService.parserUri(uri,ownerType,ownerId);
		} catch (Exception e) {
			LOGGER.error("Parser uri is error." + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.print(SignatureHelper.generateSecretKey());

//		String a = "{\n" +
//				"    \"module\": \"file\",\n" +
//				"    \"from\": \"record\",\n" +
//				"    \"appid\": \"K0MvwB2WmFJNrgg4\",\n" +
//				"    \"lid\": \"2kGogxABi86pHQkd\",\n" +
//				"    \"fid\": \"/video/4/5d/2kGogxABi86pHQkd.mp4\",\n" +
//				"    \"size\": \"1220641\",\n" +
//				"    \"dura\": \"14\",\n" +
//				"    \"state\": 1,\n" +
//				"    \"msg\": \"created\"\n" +
//				"}";
//
//		VideoCallbackCommand cmd = JSONObject.parseObject(a, VideoCallbackCommand.class);
//		VideoState videoState = null;
//		if(cmd.getModule().trim().equals("live")){
//			if(cmd.getState() == 0){
//				videoState = VideoState.UN_READY;
//			}else{
//				videoState = VideoState.LIVE;
//			}
//		}else if(cmd.getModule().trim().equals("file") && !StringUtils.isEmpty(cmd.getFrom()) && cmd.getFrom().trim().equals("record")){
//			if(cmd.getState() == -1){
//				videoState = VideoState.EXCEPTION;
//			}else if(cmd.getState() == 1){
//				videoState = VideoState.RECORDING;
//			}else{
//				videoState = VideoState.LIVE;
//			}
//		}else{
//			System.out.print("aaaa...........");
//			return;
//		}
	}
	
	private Map<String, String> createActivityRouterMeta(String url, String subject){
		Map<String, String> meta = new HashMap<String, String>();
		RouterMetaObject routerMetaObject = new RouterMetaObject();
		routerMetaObject.setUrl(url);
		meta.put(MessageMetaConstant.META_OBJECT_TYPE, "message.router");
		meta.put(MessageMetaConstant.META_OBJECT, routerMetaObject.toString());
		if(subject != null){
			meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subject);
		}
		return meta;
	}

	@Override
	public StatisticsSummaryResponse statisticsSummary(StatisticsSummaryCommand cmd) {
		StatisticsSummaryResponse response = new StatisticsSummaryResponse();
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}

		Integer activityCount = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), null, null, false);
		
		Integer activityDayCountCreate = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesmorning(), this.getTimesnight(), false);
		Integer activityDayCountDelete = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesmorning(), this.getTimesnight(), true);
		
		Integer activityMonthCountCreate = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesMonthmorning(), this.getTimesMonthnight(), false);
		Integer activityMonthCountDelete = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesMonthmorning(), this.getTimesMonthnight(), true);
		
		Integer activityWeekCountCreate = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesWeekmorning(), this.getTimesWeeknight(), false);
		Integer activityWeekCountDelete = activityProvider.countActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesWeekmorning(), this.getTimesWeeknight(), true);
		
		response.setActivityCount(activityCount);
		response.setActivityDayCount(activityDayCountCreate - activityDayCountDelete);
		response.setActivityMonthCount(activityMonthCountCreate- activityMonthCountDelete);
		response.setActivityWeekCount(activityWeekCountCreate - activityWeekCountDelete);
		
		Integer rosterCount = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), null, null, null, false);
		
		Integer rosterDayCountCreate = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesmorning(), this.getTimesnight(), null, false);
		Integer rosterDayCountCancel = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesmorning(), this.getTimesnight(), null, true);
		
		Integer rosterMonthCountCreate = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesMonthmorning(), this.getTimesMonthnight(), null, false);
		Integer rosterMonthCountCancel = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesMonthmorning(), this.getTimesMonthnight(), null, true);
		
		Integer rosterWeekCountCreate = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesWeekmorning(), this.getTimesWeeknight(), null, false);
		Integer rosterWeekCountCancel = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), this.getTimesWeekmorning(), this.getTimesWeeknight(), null, true);
		
		response.setRosterCount(rosterCount);
		response.setRosterDayCount(rosterDayCountCreate - rosterDayCountCancel);
		response.setRosterMonthCount(rosterMonthCountCreate - rosterMonthCountCancel);
		response.setRosterWeekCount(rosterWeekCountCreate - rosterWeekCountCancel);
		
		Integer manCount = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), null, null, UserGender.MALE, false);
		Integer womanCount = activityProvider.countActivityRoster(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), null, null, UserGender.FEMALE, false);
		
		response.setManCount(manCount);
		response.setWomanCount(womanCount);
		
		return response;
	}

	@Override
	public StatisticsActivityResponse statisticsActivity(StatisticsActivityCommand cmd) {
		StatisticsActivityResponse response = new StatisticsActivityResponse();
		List<StatisticsActivityDTO> listDto = new ArrayList<StatisticsActivityDTO>();
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		List<Activity> results = activityProvider.statisticsActivity(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId(), cmd.getStartTime(), cmd.getEndTime(), cmd.getTag());
		List<Long> activityIds = new ArrayList<Long>();
		
		if(results != null){
			results.forEach(r -> {
				StatisticsActivityDTO dto = ConvertHelper.convert(r, StatisticsActivityDTO.class);
				dto.setActivityId(r.getId());
				dto.setCreateTime(r.getCreateTime().getTime());
				dto.setEnrollUserCount(0);
				listDto.add(dto);
				activityIds.add(r.getId());
			});
		}
		List<Object[]> listObject = activityProvider.statisticsRosterPay(activityIds);
		
		if(listObject != null){
			listObject.forEach(r->{
				listDto.forEach(rr -> {
					
					if(r[0] != null && ((Long)r[0]).equals(rr.getActivityId())){
						Integer count = r[1] == null ? 0 : (Integer)r[1];
						rr.setEnrollUserCount(count);
					}
					
				});
			});
		}
		
		//排序 参考  com.everhomes.rest.activity.StatisticsOrderByFlag
		
		StatisticsActivityDTO temp = null;
		for(int i= 0; i< listDto.size(); i++){
			for(int j=i+1; j< listDto.size(); j++){
				if(cmd.getOrderBy() == null || cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.PUBLISH_TIME_DESC.getCode()){
					if(listDto.get(j).getCreateTime().longValue() > listDto.get(i).getCreateTime()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					}
				}else if(cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.PEOPLE_COUNT_ASC.getCode()){
					if(listDto.get(j).getEnrollUserCount().intValue() < listDto.get(i).getEnrollUserCount()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					} 
				}else if(cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.PEOPLE_COUNT_DESC.getCode()){
					if(listDto.get(j).getEnrollUserCount().intValue() > listDto.get(i).getEnrollUserCount()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					} 
				}else if(cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.PUBLISH_TIME_ASC.getCode()){
					if(listDto.get(j).getCreateTime().longValue() < listDto.get(i).getCreateTime()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					}
				}
			}
		}
		
		response.setList(listDto);
		return response;
	}

	@Override
	public StatisticsOrganizationResponse statisticsOrganization(StatisticsOrganizationCommand cmd) {
		StatisticsOrganizationResponse response = new StatisticsOrganizationResponse();
		List<StatisticsOrganizationDTO> listDto = new ArrayList<StatisticsOrganizationDTO>();
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		
		List<Object[]> listOrganization = activityProvider.statisticsOrganization(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId());
		if(listOrganization != null){
			listOrganization.forEach(r -> {
				StatisticsOrganizationDTO tempDto = new StatisticsOrganizationDTO();
				if(r[0] != null){
					tempDto.setOrgId((Long)r[0]);
				}
				if(r[1] != null){
					tempDto.setOrgName((String)r[1]);
				}
				tempDto.setSignPeopleCount((Integer)r[2]);
				tempDto.setSignActivityCount((Integer)r[3]);
				listDto.add(tempDto);
			});
		}
		//将所有获取不到企业名称的数据合为一个。
		Integer size = listDto.size();
		if (!CollectionUtils.isEmpty(listDto)) {
            Iterator<StatisticsOrganizationDTO> iterator = listDto.iterator();
            Integer anotherPeopleCount = 0;
            Integer anotherActivityCount = 0;
            while (iterator.hasNext()) {
                StatisticsOrganizationDTO dto = iterator.next();
                if (StringUtils.isEmpty(dto.getOrgName())) {
                    anotherPeopleCount += dto.getSignPeopleCount();
                    anotherActivityCount += dto.getSignActivityCount();
                    iterator.remove();
                }
            }
            if (size > listDto.size()) {
                StatisticsOrganizationDTO anotherDto = new StatisticsOrganizationDTO();
                anotherDto.setSignPeopleCount(anotherPeopleCount);
                anotherDto.setSignActivityCount(anotherActivityCount);
                listDto.add(anotherDto);
            }
        }
		response.setList(listDto);
		return response;
	}

	@Override
	public StatisticsTagResponse statisticsTag(StatisticsTagCommand cmd) {
		StatisticsTagResponse response = new StatisticsTagResponse();
		List<StatisticsTagDTO> listDto = new ArrayList<StatisticsTagDTO>();
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		
		List<Object[]> listActivityTag = activityProvider.statisticsActivityTag(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId());
		List<Object[]> listRosterTag = activityProvider.statisticsRosterTag(namespaceId, cmd.getCategoryId(), cmd.getContentCategoryId());
		
		final Integer[] activityCount = new Integer[1];
		activityCount[0] = 0;
		final Integer[] rosterCount = new Integer[1];
		rosterCount[0] = 0;
		
		if(listActivityTag != null && listRosterTag != null){
			listActivityTag.forEach(r -> {
				if((String) r[0] == null){
					return;
				}
				StatisticsTagDTO temp = new StatisticsTagDTO();
				temp.setTagName((String)r[0]);
				temp.setCreateActivityCount((Integer)r[1]);
				
				//先设成0防止空指针
				temp.setSignPeopleCount(0);;
				activityCount[0] += (Integer)r[1];
				
				listRosterTag.forEach(rr -> {
					if(((String) r[0]).equals((String)rr[0])){
						temp.setSignPeopleCount((Integer) rr[1]);
						rosterCount[0] += (Integer)rr[1];
					}
				});
				
				listDto.add(temp);
			});
		}
		
		//计算比例
		listDto.forEach(r -> {
			if(activityCount[0] > 0){
				r.setCreateActivityRate(r.getCreateActivityCount().doubleValue()/activityCount[0]);
			}else{
				r.setCreateActivityRate(0D);
			}
			
			if(rosterCount[0] > 0){
				r.setSignPeopleRate(r.getSignPeopleCount().doubleValue()/rosterCount[0]);
			}else{
				r.setSignPeopleRate(0D);
			}
			
		});
		
		//排序 参考  com.everhomes.rest.activity.StatisticsOrderByFlag
		
		StatisticsTagDTO temp = null;
		for(int i= 0; i< listDto.size(); i++){
			for(int j=i+1; j< listDto.size(); j++){
				if(cmd.getOrderBy() == null || cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.ACTIVITY_COUNT_DESC.getCode()){
					if(listDto.get(j).getCreateActivityCount().longValue() > listDto.get(i).getCreateActivityCount()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					}
				}else if(cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.PEOPLE_COUNT_ASC.getCode()){
					if(listDto.get(j).getSignPeopleCount().intValue() < listDto.get(i).getSignPeopleCount()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					} 
				}else if(cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.PEOPLE_COUNT_DESC.getCode()){
					if(listDto.get(j).getSignPeopleCount().intValue() > listDto.get(i).getSignPeopleCount()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					} 
				}else if(cmd.getOrderBy().byteValue()==StatisticsOrderByFlag.ACTIVITY_COUNT_ASC.getCode()){
					if(listDto.get(j).getCreateActivityCount().longValue() < listDto.get(i).getCreateActivityCount()){
						temp = listDto.get(i);
						listDto.set(i, listDto.get(j));
						listDto.set(j, temp);
					}
				}
			}
		}
		
		response.setList(listDto);
		
		
		return response;
	}
	
	
	 // 获得当天0点时间  
    private Timestamp getTimesmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return new Timestamp(cal.getTime().getTime());  
  
  
    }  
    // 获得当天24点时间  
    private Timestamp getTimesnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return new Timestamp(cal.getTime().getTime());  
    }  
  
    // 获得本周日0点时间  
    private Timestamp getTimesWeekmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
        return new Timestamp(cal.getTime().getTime());  
    }  
  
    // 获得本周六24点时间  
    private Timestamp getTimesWeeknight() {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getTimesWeekmorning());  
        cal.add(Calendar.DAY_OF_WEEK, 7);  
        return new Timestamp(cal.getTime().getTime());  
    }  
  
    // 获得本月第一天0点时间  
    private Timestamp getTimesMonthmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return new Timestamp(cal.getTime().getTime());  
    }  
  
    // 获得本月最后一天24点时间  
    private Timestamp getTimesMonthnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        return new Timestamp(cal.getTime().getTime());  
    }

	@Override
	public void syncActivitySignupAttendeeCount() {
		List<Long> listIds = activityProvider.listActivityIds();
		if(listIds != null){
			for(int i=0; i< listIds.size(); i++){
				Long activityId = listIds.get(i);
				this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ACTIVITY.getCode()).enter(()-> {
					Activity activity = activityProvider.findActivityById(activityId);
					
					//找不到活动不同步
					if(activity == null){
						return null;
					}
					//已确认为已报名
			        Condition condition = Tables.EH_ACTIVITY_ROSTER.CONFIRM_FLAG.eq(ConfirmStatus.CONFIRMED.getCode());
			        condition = condition.and(Tables.EH_ACTIVITY_ROSTER.STATUS.eq(ActivityRosterStatus.NORMAL.getCode()));
			        Integer confirmUserCount = activityProvider.countActivityRosterByCondition(activityId, condition);
			        
			        //数量相等不需要同步
			        if(activity.getSignupAttendeeCount() != null &&  activity.getSignupAttendeeCount().intValue() == confirmUserCount){
			        	return null;
			        }
			        
			        Integer oldSignupAttendeeCount = activity.getSignupAttendeeCount();
			        activity.setSignupAttendeeCount(confirmUserCount);
			        activityProvider.updateActivity(activity);
			        LOGGER.warn("Activity signupAttendeeCount warning Id=" + activityId + " old signupAttendeeCount=" + oldSignupAttendeeCount + " calculate count=" + confirmUserCount);
					return null;
				});
			}
		}
		
	}

    @Override
    public GetActivityPayeeDTO getActivityPayee(GetActivityPayeeCommand cmd) {
        if (cmd.getCategoryId() == null) {
            LOGGER.error("CategoryId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "CategoryId cannot be null.");
        }
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), UserContext.getCurrentNamespaceId());
        if (activityCategories == null) {
            LOGGER.error("activityCategories cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "activityCategories cannot be null.");
        }
	    ActivityBizPayee activityBizPayee = this.activityProvider.getActivityPayee(activityCategories.getId(), UserContext.getCurrentNamespaceId());
        GetActivityPayeeDTO activityPayeeDTO = new GetActivityPayeeDTO();
        if (activityBizPayee != null) {
            activityPayeeDTO.setAccountId(activityBizPayee.getBizPayeeId());
        }
        return activityPayeeDTO;
    }

    @Override
    public List<ActivityPayeeDTO> listActivityPayee(ListActivityPayeeCommand cmd) {
        if (cmd.getOrganizationId() == null) {
            LOGGER.error("organizationId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "organizationId cannot be null.");
        }
	    List<ActivityPayeeDTO> dtoList = new ArrayList<>();
	    String prefix = OwnerType.ORGANIZATION.getCode();
	    List<PayUserDTO> list = this.payServiceV2.getPayUserList(prefix + cmd.getOrganizationId().toString(), String.valueOf(0));
	    if (list != null && list.size() > 0) {
            for (PayUserDTO r : list) {
                ActivityPayeeDTO activityPayeeDTO = new ActivityPayeeDTO();
                activityPayeeDTO.setAccountId(r.getId());
                activityPayeeDTO.setAccountName(r.getRemark());
                Integer userType = r.getUserType();
                if(userType != null && userType.equals(2)) {
                    activityPayeeDTO.setAccountType(OwnerType.ORGANIZATION.getCode());
                    activityPayeeDTO.setAccountAliasName(r.getUserAliasName());
                } else {
                    activityPayeeDTO.setAccountType(OwnerType.USER.getCode());
                    activityPayeeDTO.setAccountAliasName(r.getUserName());
                }
                // 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
                Integer registerStatus = r.getRegisterStatus();
                if(registerStatus != null && registerStatus.intValue() == 1) {
                    activityPayeeDTO.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
                } else {
                    activityPayeeDTO.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
                }
                dtoList.add(activityPayeeDTO);
            }
        }
        return dtoList;
    }

    @Override
    public void createOrUpdateActivityPayee(CreateOrUpdateActivityPayeeCommand cmd) {
        if (cmd.getCategoryId() == null) {
            LOGGER.error("CategoryId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "CategoryId cannot be null.");
        }
        if (cmd.getPayeeId() == null) {
            LOGGER.error("payeeId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "payeeId cannot be null.");
        }
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), UserContext.getCurrentNamespaceId());
        if (activityCategories == null) {
            LOGGER.error("activityCategories cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "activityCategories cannot be null.");
        }
        ActivityBizPayee activityBizPayee = this.activityProvider.getActivityPayee(activityCategories.getId(), UserContext.getCurrentNamespaceId());
        if (activityBizPayee == null) {
            ActivityBizPayee persist = new ActivityBizPayee();
            persist.setNamespaceId(UserContext.getCurrentNamespaceId());
            persist.setBizPayeeType(OwnerType.ORGANIZATION.getCode());
            persist.setOwnerId(activityCategories.getId());
            persist.setBizPayeeId(cmd.getPayeeId());
            this.activityProvider.CreateActivityPayee(persist);
        }else {
            if (activityBizPayee.getBizPayeeId() != cmd.getPayeeId()) {
                activityBizPayee.setBizPayeeId(cmd.getPayeeId());
                this.activityProvider.updateActivityPayee(activityBizPayee);
            }
        }
    }

    @Override
    public CheckPayeeIsUsefulResponse checkPayeeIsUseful(CheckPayeeIsUsefulCommand cmd) {
        if (cmd.getCategoryId() == null) {
            LOGGER.error("CategoryId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "CategoryId cannot be null.");
        }
        CheckPayeeIsUsefulResponse checkPayeeIsUsefulResponse = new CheckPayeeIsUsefulResponse();
        checkPayeeIsUsefulResponse.setPayeeAccountStatus(ActivityPayeeStatusType.NULL.getCode());

        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), UserContext.getCurrentNamespaceId());
        if (activityCategories == null) {
            LOGGER.error("activityCategories cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "activityCategories cannot be null.");
        }

        ActivityBizPayee activityBizPayee = this.activityProvider.getActivityPayee(activityCategories.getId(), UserContext.getCurrentNamespaceId());
        if (activityBizPayee == null) {
            return checkPayeeIsUsefulResponse;
        }
        List<Long> idList = new ArrayList<>();
        idList.add(activityBizPayee.getBizPayeeId());
        List<PayUserDTO> list = this.payServiceV2.listPayUsersByIds(idList);
        if (list != null && list.size() > 0) {
            PayUserDTO payUserDTO = (PayUserDTO)list.get(0);
            if (payUserDTO.getRegisterStatus() == 0) {
                checkPayeeIsUsefulResponse.setPayeeAccountStatus(ActivityPayeeStatusType.UNDER_REVIEW.getCode());
                return checkPayeeIsUsefulResponse;
            }
            if (payUserDTO.getRegisterStatus() == 1) {
                checkPayeeIsUsefulResponse.setPayeeAccountStatus(ActivityPayeeStatusType.IN_USE.getCode());
                return checkPayeeIsUsefulResponse;
            }
        }
        return checkPayeeIsUsefulResponse;
    }

    @Override
    public void payNotify(OrderPaymentNotificationCommand cmd) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("payNotify-command=" + GsonUtil.toJson(cmd));
        }
        if(cmd == null || cmd.getPaymentErrorCode() != "200") {
            LOGGER.error("payNotify fail, cmd={}", cmd);
        }

        GetPurchaseOrderCommand getPurchaseOrderCommand = new GetPurchaseOrderCommand();
        String systemId = configurationProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
        getPurchaseOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        getPurchaseOrderCommand.setAccountCode(generateAccountCode());
        getPurchaseOrderCommand.setBusinessOrderNumber(cmd.getBizOrderNum());

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("payNotify-GetPurchaseOrderCommand=" + GsonUtil.toJson(getPurchaseOrderCommand));
        }
        GetPurchaseOrderRestResponse response = orderService.getPurchaseOrder(getPurchaseOrderCommand);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("payNotify-getPurchaseOrder response=" + GsonUtil.toJson(response));
        }
        if(response == null || !response.getErrorCode().equals(200)) {
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ROSTER,
                    "PayNotify getPurchaseOrder by bizOrderNum is error!");
        }
        PurchaseOrderDTO purchaseOrderDTO = response.getResponse();


        com.everhomes.pay.order.OrderType orderType = com.everhomes.pay.order.OrderType.fromCode(purchaseOrderDTO.getPaymentOrderType());
        if(orderType != null) {
            switch (orderType) {
                case PURCHACE:
                    if(purchaseOrderDTO.getPaymentStatus() == PurchaseOrderPaymentStatus.PAID.getCode()){
                        //支付成功
                       paySuccess(purchaseOrderDTO);
                    }
                    if(purchaseOrderDTO.getPaymentStatus() == PurchaseOrderPaymentStatus.FAILED.getCode()){
                        //支付失败
                        payFaild(purchaseOrderDTO);
                    }
                    break;
                default:
                    LOGGER.error("unsupport orderType, orderType={}, cmd={}", orderType.getCode(), StringHelper.toJsonString(cmd));
            }
        }else {
            LOGGER.error("orderType is null, cmd={}", StringHelper.toJsonString(cmd));
        }
    }

    private void paySuccess(PurchaseOrderDTO purchaseOrderDTO){
        LOGGER.info("PurchaseOrderDTO paySuccess start dto = {}", StringHelper.toJsonString(purchaseOrderDTO));

        ActivityRoster roster = activityProvider.findRosterByPayOrderId(purchaseOrderDTO.getId());
        if(roster == null){
            LOGGER.info("can not find roster by orderno = {}", purchaseOrderDTO.getId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
                    "no roster.");
        }
        Activity activity = activityProvider.findActivityById(roster.getActivityId());
        if(activity == null){
            LOGGER.info("can not find activity by id = {}", roster.getActivityId());
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_ID,
                    "no activity.");
        }
        //检验支付结果和应价格是否相等
        checkPayAmount(new BigDecimal(purchaseOrderDTO.getAmount()), activity.getChargePrice());
        //支付宝回调时，可能会同时回调多次，
        roster.setPayFlag(ActivityRosterPayFlag.PAY.getCode());
        roster.setPayTime(purchaseOrderDTO.getPaymentTime());

        String amount = String.valueOf(purchaseOrderDTO.getAmount() * 1.0/100);
        roster.setPayAmount(new BigDecimal(amount));
        LOGGER.info("amount = {}, roster amount = {}", amount, roster.getPayAmount());
        roster.setVendorType(String.valueOf(purchaseOrderDTO.getPaymentType()));
        roster.setOrderType(String.valueOf(purchaseOrderDTO.getPaymentType()));
        roster.setPayVersion(ActivityRosterPayVersionFlag.V3.getCode());

        activityProvider.updateRoster(roster);
    }

    private void payFaild(PurchaseOrderDTO purchaseOrderDTO) {
        LOGGER.info("PurchaseOrderDTO payFail dto = {}", purchaseOrderDTO);

        if(LOGGER.isDebugEnabled())
            LOGGER.error("onlinePayBillFail");
        ActivityRoster roster = activityProvider.findRosterByPayOrderId(purchaseOrderDTO.getId());
        if(roster == null){
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_NO_ROSTER,
                    "no roster.");
        }
        ActivityCancelSignupCommand cancelCmd = new ActivityCancelSignupCommand();
        cancelCmd.setActivityId(roster.getActivityId());
        cancelCmd.setUserId(roster.getUid());
        cancelCmd.setCancelType(ActivityCancelType.PAY_FAIL.getCode());
        this.cancelSignup(cancelCmd);
        LOGGER.info("PurchaseOrderDTO payFail end");
    }
    private void checkPayAmount(BigDecimal payAmount, BigDecimal chargePrice) {
        if(payAmount == null || chargePrice == null || payAmount.longValue() != chargePrice.multiply(new BigDecimal(100)).longValue()){
            LOGGER.error("payAmount and chargePrice is not equal.");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_PAYAMOUNT_ERROR,
                    "payAmount and chargePrice is not equal.");
        }
    }
    @Override
    public void exportActivity(ExportActivityCommand cmd) {

        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        if(cmd.getNamespaceId() != null){
            params.put("namespaceId", cmd.getNamespaceId());
        }
        if(cmd.getStartTime() != null){
            params.put("startTime", cmd.getStartTime());
        }
        if(cmd.getEndTime() != null){
            params.put("endTime", cmd.getEndTime());
        }

        if (cmd.getCategoryId() != null) {
            params.put("categoryId", cmd.getCategoryId());
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "activityList";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), cmd.getNamespaceId());
        if (namespace != null && activityCategories != null) {
            fileName = namespace.getName() + "_" + activityCategories.getName();
        }
        SimpleDateFormat fileNameSdf = new SimpleDateFormat("yyyyMMdd");
        fileName += "_活动报名_" + fileNameSdf.format(cmd.getStartTime()) + "_" +fileNameSdf.format(cmd.getEndTime()) +".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ActivityApplyExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
    public void exportOrganization(ExportOrganizationCommand cmd) {

        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        if(cmd.getNamespaceId() != null){
            params.put("namespaceId", cmd.getNamespaceId());
        }
        if (cmd.getCategoryId() != null) {
            params.put("categoryId", cmd.getCategoryId());
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "activityOrganizationList";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), cmd.getNamespaceId());
        if (namespace != null && activityCategories != null) {
            fileName = namespace.getName() + "_" + activityCategories.getName();
        }
        SimpleDateFormat fileNameSdf = new SimpleDateFormat("yyyyMMdd");
        fileName += "_企业报名_" + fileNameSdf.format(new Date()) +".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ActivityOrganizationExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
    public void exportTag(ExportTagCommand cmd) {

        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        if(cmd.getNamespaceId() != null){
            params.put("namespaceId", cmd.getNamespaceId());
        }
        if (cmd.getCategoryId() != null) {
            params.put("categoryId", cmd.getCategoryId());
        }

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String fileName = "activityTagList";
        Namespace namespace  = this.namespaceProvider.findNamespaceById(namespaceId);
        ActivityCategories activityCategories = this.activityProvider.findActivityCategoriesByEntryId(cmd.getCategoryId(), cmd.getNamespaceId());
        if (namespace != null && activityCategories != null) {
            fileName = namespace.getName() + "_" + activityCategories.getName();
        }
        SimpleDateFormat fileNameSdf = new SimpleDateFormat("yyyyMMdd");
        fileName += "_标签统计_" + fileNameSdf.format(new Date())+".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ActivityTagExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }
    @Override
    public void exportActivitySignupTemplate(ExportActivitySignupTemplateCommand cmd, HttpServletResponse httpResponse) {
        GetTemplateBySourceIdCommand getTemplateBySourceIdCommand = ConvertHelper.convert(cmd, GetTemplateBySourceIdCommand.class);
        getTemplateBySourceIdCommand.setOwnerId(cmd.getActivityId());
        GeneralFormDTO form = this.generalFormService.getTemplateBySourceId(getTemplateBySourceIdCommand);
        String fileName = "活动报名模板";
        ExcelUtils excelUtils = new ExcelUtils(httpResponse, fileName, fileName);
        List<String> titleNames = new ArrayList<>();
        List<String> propertyNames = new ArrayList<>();
        List<Integer> titleSizes = new ArrayList<>();
        if (form != null && !CollectionUtils.isEmpty(form.getFormFields())) {
            List<GeneralFormFieldDTO> formFieldDTOS = form.getFormFields();
            for (GeneralFormFieldDTO generalFormFieldDTO : formFieldDTOS) {
                if (GeneralFormDataVisibleType.HIDDEN.getCode().equals(generalFormFieldDTO.getVisibleType())) {
                    continue;
                }
                titleNames.add(generalFormFieldDTO.getFieldDisplayName());
                titleSizes.add(20);

            }
            excelSettings(excelUtils, form);
        }else {
            titleNames.add("手机");
            titleSizes.add(20);
            List<Integer> mandatoryTitle = new ArrayList<>();
            mandatoryTitle.add(1);
            excelUtils.setNeedMandatoryTitle(true);
            excelUtils.setMandatoryTitle(mandatoryTitle);
            excelUtils.setTitleRemark(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE, ActivityLocalStringCode.ACTIVITY_IMPORT_TEMPLATE_TITLE_REMARK+"", "zh_CN", "ActivitySignupImportRemark"), (short) 18, (short) 4480);
            excelUtils.setNeedSequenceColumn(false);
            excelUtils.setNeedTitleRemark(true);
        }
        excelUtils.writeExcel(propertyNames, titleNames, titleSizes, propertyNames);
    }

//    @Override
//    public void exportActivitySignupTemplate(ExportActivitySignupTemplateCommand cmd, HttpServletResponse httpResponse) {
//        GetTemplateBySourceIdCommand getTemplateBySourceIdCommand = ConvertHelper.convert(cmd, GetTemplateBySourceIdCommand.class);
//        getTemplateBySourceIdCommand.setOwnerId(cmd.getActivityId());
//        GeneralFormDTO form = this.generalFormService.getTemplateBySourceId(getTemplateBySourceIdCommand);
//        String fileName = "活动报名模板";
//        String head = localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE, ActivityLocalStringCode.ACTIVITY_IMPORT_TEMPLATE_TITLE_REMARK+"", "zh_CN", "ActivitySignupImportRemark");
//
//        List<String> title = new ArrayList<>();
//        if (form != null && !CollectionUtils.isEmpty(form.getFormFields())) {
//            title.addAll(form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList()));
//        }else {
//            title.add("手机号码");
//        }
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet(fileName);
//        sheet.createFreezePane(1,2,1,1);
//
//        //  1.set the header
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1 > 2? title.size() -1 : 5));
//        Row headRow = sheet.createRow(0);
//        headRow.setHeight((short) (150 * 20));
//        createExcelHead(workbook, headRow, head);
//
//        //  2.set the title
//        Row titleRow = sheet.createRow(1);
//        createExcelTitle(workbook, sheet, titleRow, title);
//
//        writeHttpExcel(workbook, httpResponse, fileName);
//
//    }

    private void writeHttpExcel(Workbook workbook, HttpServletResponse httpResponse, String fileName) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            DownloadUtil.download(out, httpResponse, fileName);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
    }

    private XSSFCellStyle mandatoryTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.RED.index);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private void createExcelHead(XSSFWorkbook workbook, Row headRow, String head) {
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // font.setBold(true);
        font.setFontName("微软雅黑");
        headStyle.setFont(font);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); //  find it in the IndexedColors
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setWrapText(true);

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    @Override
    public void exportActivitySignupNew(ExportSignupInfoCommand cmd) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityId",cmd.getActivityId());
        String fileName = String.format("报名信息_%s.xlsx", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ActivitySignupExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }

    @Override
    public OutputStream getActivitySignupExportStream(ExportSignupInfoCommand cmd, Long taskId) {
        Activity activity = checkActivityExist(cmd.getActivityId());
        List<ActivityRoster> rosters = new ArrayList<ActivityRoster>();
        List<ActivityRoster> rostersConfirms = activityProvider.listActivityRoster(cmd.getActivityId(), null, null, 1, 0, 100000);
        List<ActivityRoster> rostersRejects = activityProvider.listActivityRoster(cmd.getActivityId(), null, null, 2, 0, 100000);
        List<ActivityRoster> rostersUnConfirms = activityProvider.listActivityRoster(cmd.getActivityId(), null, null, 0, 0, 100000);

        rosters.addAll(rostersConfirms);
        rosters.addAll(rostersRejects);
        rosters.addAll(rostersUnConfirms);

        if (rosters.size() > 0) {
            ActivityRoster creator = new ActivityRoster();
            for (ActivityRoster roster : rosters) {
                if (activity.getCreatorUid().equals(roster.getUid())) {
                    creator = roster;
                    break;
                }
            }
            GeneralForm form = this.generalFormProvider.findGeneralFormById(creator.getFormId());
            List<SignupInfoDTO> signupInfoDTOs = rosters.stream().map(r->convertActivityRosterForExcel(r, activity)).collect(Collectors.toList());
            List<String> titleNames = new ArrayList<String>(Arrays.asList("序号", "手机号", "用户昵称", "性别", "项目名称"));
            List<GeneralFormFieldDTO> itemList = new ArrayList<>();
            if (form != null) {
                itemList = JSONObject.parseArray(form.getTemplateText(),
                        GeneralFormFieldDTO.class);
                for (GeneralFormFieldDTO generalFormFieldDTO : itemList) {
                    titleNames.add(generalFormFieldDTO.getFieldDisplayName());
                }
            }
            titleNames.addAll(Arrays.asList("报名时间", "报名来源", "报名状态"));
            if(activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()){
                titleNames.add("已付金额");
                titleNames.add("已退金额");
            }


            if (CheckInStatus.fromCode(activity.getSignupFlag()) == CheckInStatus.CHECKIN) {
                titleNames.add("是否签到");
            }
            taskService.updateTaskProcess(taskId, 20);
            List<ExportActivitySignupDTO> values = new ArrayList<>();
            for (int i = 0; i<signupInfoDTOs.size();i++) {
                SignupInfoDTO signupInfoDTO = signupInfoDTOs.get(i);
                if (signupInfoDTO == null) {
                    continue;
                }
                ExportActivitySignupDTO exportActivitySignupDTO = new ExportActivitySignupDTO();
                List<String> signupValue = new ArrayList<>();
                if (signupInfoDTO.getCreateFlag().equals((byte)1)) {
                    signupValue.add("创建者");
                }else {
                    signupValue.add(i+"");
                }
                signupValue.add(signupInfoDTO.getPhone());
                signupValue.add(signupInfoDTO.getNickName());
                if (UserGender.fromCode(signupInfoDTO.getGender()) == null) {
                    signupValue.add("-");
                }else {
                    signupValue.add(UserGender.fromCode(signupInfoDTO.getGender()).getText());
                }
                signupValue.add(signupInfoDTO.getCommunityName());
                if (signupInfoDTO.getCreateFlag().equals((byte)1)) {
                    if (!CollectionUtils.isEmpty(itemList)) {
                        for (GeneralFormFieldDTO s : itemList) {
                            signupValue.add("");
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(signupInfoDTO.getValues()) && !CollectionUtils.isEmpty(itemList)) {
                    for (GeneralFormFieldDTO generalFormFieldDTO : itemList) {
                        String value = "";
                        for (PostApprovalFormItem postApprovalFormItem : signupInfoDTO.getValues()) {
                            if (postApprovalFormItem.getFieldName().equals(generalFormFieldDTO.getFieldName())) {
                                value = postApprovalFormItem.getFieldValue();
                            }
                        }
                        signupValue.add(value);
                    }
                }
                signupValue.add(signupInfoDTO.getSignupTime());
                signupValue.add(signupInfoDTO.getSourceFlagText());
                signupValue.add(signupInfoDTO.getSignupStatusText());
                if(activity.getChargeFlag() != null && activity.getChargeFlag().byteValue() == ActivityChargeFlag.CHARGE.getCode()){
                    if (signupInfoDTO.getPayAmount() == null) {
                        signupValue.add(String.valueOf(new BigDecimal(0)));
                    }else {
                        signupValue.add(String.valueOf(signupInfoDTO.getPayAmount()));
                    }
                    if (signupInfoDTO.getRefundAmount() == null) {
                        signupValue.add(String.valueOf(new BigDecimal(0)));
                    }else {
                        signupValue.add(String.valueOf(signupInfoDTO.getRefundAmount()));

                    }
                }
                if (CheckInStatus.fromCode(activity.getSignupFlag()) == CheckInStatus.CHECKIN) {
                    signupValue.add(signupInfoDTO.getCheckinFlagText());
                }
                exportActivitySignupDTO.setVals(signupValue);
                values.add(exportActivitySignupDTO);
            }
            taskService.updateTaskProcess(taskId, 75);

            //  write excel
            XSSFWorkbook workbook = exportActivitySignupFiles(titleNames, values);
            taskService.updateTaskProcess(taskId, 95);

            return writeExcel(workbook);
        }else {
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_NO_ROSTER, "no roster in this activity");
        }
    }

    @Override
    public ListGeneralFormResponse listActivitySignupGeneralForms(ListGeneralFormsCommand cmd) {
        GeneralForm initActivitySignupForm = this.activitySignupFormHandler.getDefaultGeneralForm(cmd.getOwnerType());
        GeneralFormDTO dto = ConvertHelper.convert(initActivitySignupForm, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(dto.getTemplateText(), GeneralFormFieldDTO.class);
        dto.setFormFields(fieldDTOs);
        List<GeneralFormDTO> list = new ArrayList<>();
        list.add(dto);
        ListGeneralFormResponse response = this.generalFormService.listGeneralForms(cmd);
        list.addAll(response.getForms());
        response.setForms(list);
        return response;
    }

    @Override
    public GeneralFormDTO updateGeneralForm(UpdateActivityFormCommand cmd) {
        UpdateApprovalFormCommand updateApprovalFormCommand = ConvertHelper.convert(cmd, UpdateApprovalFormCommand.class);
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getFormOriginId());

        GeneralFormDTO generalFormDTO = this.generalFormService.updateGeneralForm(updateApprovalFormCommand);
        if (!form.getId().equals(generalFormDTO.getId())) {
            List<CommunityGeneralForm> list = this.communityFormProvider.listCommunityGeneralFormByFormId(form.getId());
            if (!CollectionUtils.isEmpty(list)) {
                for(CommunityGeneralForm communityGeneralForm : list) {
                    communityGeneralForm.setFormOriginId(generalFormDTO.getId());
                    this.communityFormProvider.updateCommunityGeneralForm(communityGeneralForm);
                }
            }
        }
        return generalFormDTO;
    }

    @Override
    public void deleteActivityFormById(ActivityFormIdCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getFormOriginId());
        List<CommunityGeneralForm> list = this.communityFormProvider.listCommunityGeneralFormByFormId(form.getId());
        if (!CollectionUtils.isEmpty(list)) {
            LOGGER.error("cann't delete this form");
            throw RuntimeErrorException.errorWith(CommunityFormErrorCode.SCOPE,
                    CommunityFormErrorCode.ERROR_CANNOT_DELETE, "cannot delete this form, formId = " + form.getId());
        }
        ApprovalFormIdCommand approvalFormIdCommand = ConvertHelper.convert(cmd,ApprovalFormIdCommand.class);
        generalApprovalService.deleteApprovalFormById(approvalFormIdCommand);
    }

    private XSSFWorkbook exportActivitySignupFiles(List<String> title, List<ExportActivitySignupDTO> value) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("报名信息");
        sheet.createFreezePane(1,2,1,1);
        //  2.title
        Row titleRow = sheet.createRow(0);
        createExcelTitle(workbook, sheet, titleRow, title);
        //  3.data
        for (int rowIndex = 1; rowIndex <= value.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex);
            createActivityFileData(workbook, dataRow, value.get(rowIndex - 1).getVals());
        }
        return workbook;
    }
    private void createActivityFileData(XSSFWorkbook workbook, Row dataRow, List<String> list) {
        //  设置样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("微软雅黑");
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = dataRow.createCell(i);
            cell.setCellStyle(contentStyle);
            cell.setCellValue(list.get(i));
        }
    }
    private void createExcelTitle(XSSFWorkbook workbook, Sheet sheet, Row titleRow, List<String> title) {
        XSSFCellStyle commonStyle = commonTitleStyle(workbook);
        for (int i = 0; i < title.size(); i++) {
            Cell cell = titleRow.createCell(i);
            sheet.setColumnWidth( i,18 * 256);
            cell.setCellStyle(commonStyle);
            cell.setCellValue(title.get(i));
        }
    }
    private XSSFCellStyle commonTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }
    private void excelSettings(ExcelUtils excelUtils, GeneralFormDTO form) {
        List<Integer> mandatoryTitle = new ArrayList<>();
        for (GeneralFormFieldDTO formFieldDTO : form.getFormFields()) {
            if (TrueOrFalseFlag.TRUE.getCode().equals(formFieldDTO.getRequiredFlag())) {
                mandatoryTitle.add(1);
            }else {
                mandatoryTitle.add(0);
            }
        }
        excelUtils.setNeedMandatoryTitle(true);
        excelUtils.setMandatoryTitle(mandatoryTitle);
        excelUtils.setTitleRemark(localeStringService.getLocalizedString(ActivityLocalStringCode.SCOPE, ActivityLocalStringCode.ACTIVITY_IMPORT_TEMPLATE_TITLE_REMARK+"", "zh_CN", "ActivitySignupImportRemark"), (short) 18, (short) 4480);
        excelUtils.setNeedSequenceColumn(false);
        excelUtils.setNeedTitleRemark(true);
    }

    private ByteArrayOutputStream writeExcel(XSSFWorkbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
        return out;
    }
//	@Override
//	public void exportErrorInfo(ExportErrorInfoCommand cmd, HttpServletResponse response) {
//		List<ActivityRosterError> dtos = activityProvider.listActivityRosterErrorByJobId(cmd.getJobId());
//		String fileName = String.format("异常信息_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
//		ExcelUtils excelUtils = new ExcelUtils(response, fileName, "异常信息");
//		List<String> propertyNames = new ArrayList<String>(Arrays.asList("rowNum", "description"));
//		List<String> titleNames = new ArrayList<String>(Arrays.asList( "行号", "异常内容"));
//		List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList( 20, 100));
//
//		excelUtils.setNeedSequenceColumn(false);
//		excelUtils.writeExcel(propertyNames, titleNames, titleSizes, dtos);
//	}


    @Override
    public ListActivitiesByCategoryIdResponse listActivitiesByCategoryId(ListActivitiesByCategoryIdCommand cmd) {

	    if(cmd.getPageSize() == null){
	        cmd.setPageSize(20L);
        }
        Condition condition = Tables.EH_ACTIVITIES.NAMESPACE_ID.eq(cmd.getNamespaceId());
        condition = condition.and(Tables.EH_ACTIVITIES.CATEGORY_ID.eq(cmd.getCategoryId()));
        condition = condition.and(Tables.EH_ACTIVITIES.CLONE_FLAG.in(PostCloneFlag.NORMAL.getCode(), PostCloneFlag.REAL.getCode()));
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Activity> activities = this.activityProvider.listActivities(locator, cmd.getPageSize().intValue(), condition, false, null);

        List<ActivityDTO> dtos = activities.stream().map(r -> ConvertHelper.convert(r, ActivityDTO.class)).collect(Collectors.toList());
        ListActivitiesByCategoryIdResponse response = new ListActivitiesByCategoryIdResponse();
        response.setDtos(dtos);
        return response;

    }
}
