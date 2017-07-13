// @formatter:off
package com.everhomes.talent;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.talent.ClearTalentQueryHistoryCommand;
import com.everhomes.rest.talent.CreateMessageSenderCommand;
import com.everhomes.rest.talent.CreateOrUpdateRequestSettingCommand;
import com.everhomes.rest.talent.CreateOrUpdateRequestSettingResponse;
import com.everhomes.rest.talent.CreateOrUpdateTalentCategoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteMessageSenderCommand;
import com.everhomes.rest.talent.DeleteTalentCategoryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.DeleteTalentQueryHistoryCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.GetTalentRequestDetailCommand;
import com.everhomes.rest.talent.GetTalentRequestDetailResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListMessageSenderCommand;
import com.everhomes.rest.talent.ListMessageSenderResponse;
import com.everhomes.rest.talent.ListTalentCategoryCommand;
import com.everhomes.rest.talent.ListTalentCategoryResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentRequestCommand;
import com.everhomes.rest.talent.ListTalentRequestResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.MessageSenderDTO;
import com.everhomes.rest.talent.TalentCategoryDTO;
import com.everhomes.rest.talent.TalentDTO;
import com.everhomes.rest.talent.TalentDegreeEnum;
import com.everhomes.rest.talent.TalentQueryHistoryDTO;
import com.everhomes.rest.talent.TalentRequestDTO;
import com.everhomes.rest.talent.TopTalentCommand;
import com.everhomes.rest.user.UserGender;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.ValidatorUtil;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

/**
 * 这里的所有public方法都使用TalentServiceAdvice检查是否为管理员权限
 */
@Component
public class TalentServiceImpl implements TalentService {

	private static final String TALENT_REQUEST_NAME = "talent.request.name";
	private static final String TALENT_FORM_ID = "talent.form.id";
	
	@Autowired
	private TalentCategoryProvider talentCategoryProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private TalentProvider talentProvider;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private TalentQueryHistoryProvider talentQueryHistoryProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private TalentMessageSenderProvider talentMessageSenderProvider;
	
	@Autowired
	private TalentRequestProvider talentRequestProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private GeneralFormService generalFormService;
	
	@Override
	public ListTalentCategoryResponse listTalentCategory(ListTalentCategoryCommand cmd) {
		List<TalentCategory> talentCategories = talentCategoryProvider.listTalentCategoryByNamespace(namespaceId());
		talentCategories.add(TalentCategory.other());
		return new ListTalentCategoryResponse(talentCategories.stream().map(this::convert).collect(Collectors.toList()));
	}

	private TalentCategoryDTO convert(TalentCategory talentCategory) {
		return ConvertHelper.convert(talentCategory, TalentCategoryDTO.class);
	}
	
	@Override
	public void createOrUpdateTalentCategory(CreateOrUpdateTalentCategoryCommand cmd) {
		List<TalentCategoryDTO> talentCategoryDTOs = cmd.getTalentCategories();
		if (!talentCategoryDTOs.isEmpty()) {
			checkDuplication(talentCategoryDTOs);
			dbProvider.execute(s->{
				talentCategoryDTOs.forEach(t->{
					if (!TalentCategory.otherName().equals(t.getName())) {
						if (t.getId() == null) {
							createTalentCategory(t);
						}else {
							updateTalentCategory(t);
						}
					}
				});
				return null;
			});
		}
	}

	private void checkDuplication(List<TalentCategoryDTO> talentCategoryDTOs) {
		Set<String> set = talentCategoryDTOs.stream().map(t->t.getName()).collect(Collectors.toSet());
		if (talentCategoryDTOs.size() != set.size()) {
			throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
					TalentServiceErrorCode.DUPLICATED_NAME, "duplicated name");
		}
		Integer namespaceId = namespaceId();
		talentCategoryDTOs.forEach(t->{
			if (TalentCategory.otherName().equals(t.getName()) && (t.getId() == null || t.getId().longValue() != TalentCategory.otherId())) {
				throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
						TalentServiceErrorCode.DUPLICATED_NAME, "duplicated name");
			}
			TalentCategory talentCategory = talentCategoryProvider.findTalentCategoryByName(namespaceId, t.getName());
			if (talentCategory != null && (t.getId() == null || t.getId().longValue() != talentCategory.getId().longValue())) {
				throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
						TalentServiceErrorCode.DUPLICATED_NAME, "duplicated name");
			}
		});
	}

	private TalentCategory createTalentCategory(TalentCategoryDTO talentCategoryDTO) {
		TalentCategory talentCategory = ConvertHelper.convert(talentCategoryDTO, TalentCategory.class);
		talentCategory.setNamespaceId(namespaceId());
		talentCategory.setStatus(CommonStatus.ACTIVE.getCode());
		talentCategoryProvider.createTalentCategory(talentCategory);
		return talentCategory;
	}

	private void updateTalentCategory(TalentCategoryDTO talentCategoryDTO) {
		TalentCategory talentCategory = findTalentCategoryById(talentCategoryDTO.getId());
		if (talentCategory == TalentCategory.other()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		talentCategory.setName(talentCategoryDTO.getName());
		talentCategoryProvider.updateTalentCategory(talentCategory);
	}

	private TalentCategory findTalentCategoryById(Long id) {
		if (id.longValue() != TalentCategory.otherId().longValue()) {
			TalentCategory talentCategory = talentCategoryProvider.findTalentCategoryById(id);
			if (talentCategory != null && talentCategory.getNamespaceId().intValue() == namespaceId().intValue() 
					&& talentCategory.getStatus().byteValue() == CommonStatus.ACTIVE.getCode()) {
				return talentCategory;
			}
		}
		return TalentCategory.other();
	}
	
	@Override
	public void deleteTalentCategory(DeleteTalentCategoryCommand cmd) {
		if (cmd.getIds() != null && !cmd.getIds().isEmpty()) {
			for (Long id : cmd.getIds()) {
				TalentCategory talentCategory = findTalentCategoryById(id);
				if (talentCategory == TalentCategory.other()) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
				}
				talentCategory.setStatus(CommonStatus.INACTIVE.getCode());
				talentCategoryProvider.updateTalentCategory(talentCategory);
				talentProvider.updateToOther(talentCategory.getId());
			}
		}
	}

	@Override
	public ListTalentResponse listTalent(ListTalentCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageSize(pageSize+1);
		List<Talent> talents = talentProvider.listTalent(namespaceId(), cmd);
		Long nextPageAnchor = null;
		if (talents.size() > pageSize) {
			talents.remove(talents.size()-1);
			nextPageAnchor = talents.get(talents.size()-1).getId();
		}
		
		if (TrueOrFalseFlag.fromCode(cmd.getHistoryFlag()) == TrueOrFalseFlag.TRUE && StringUtils.isNotBlank(cmd.getKeyword())) {
			addQueryHistory(cmd.getKeyword().trim());
		}
		
		return new ListTalentResponse(nextPageAnchor, talents.stream().map(t->convertWithoutRemark(t, cmd.getAppFlag())).collect(Collectors.toList()));
	}
	
	private void addQueryHistory(String keyword) {
		//如果已有,先删除再添加,这样可以保证按倒序排列
		TalentQueryHistory talentQueryHistory = talentQueryHistoryProvider.findTalentQueryHistoryByKeyword(userId(), keyword);
		if (talentQueryHistory != null) {
			talentQueryHistoryProvider.deleteTalentQueryHistory(talentQueryHistory);
		}else {
			talentQueryHistory = new TalentQueryHistory();
			talentQueryHistory.setKeyword(keyword);
			talentQueryHistory.setNamespaceId(namespaceId());
			talentQueryHistory.setStatus(CommonStatus.ACTIVE.getCode());
		}
		talentQueryHistoryProvider.createTalentQueryHistory(talentQueryHistory);
	}

	private TalentDTO convertWithoutRemark(Talent talent, Byte appFlag) {
		TalentDTO talentDTO = convert(talent, appFlag);
		talentDTO.setRemark(null);
		return talentDTO;
	}

	@Override
	public TalentDTO createOrUpdateTalent(CreateOrUpdateTalentCommand cmd) {
		Talent talent = null;
		if (cmd.getId() == null) {
			talent = createTalent(cmd);
		}else {
			talent = updateTalent(cmd);
		}
		return convert(talent, TrueOrFalseFlag.FALSE.getCode());
	}

	private Talent createTalent(CreateOrUpdateTalentCommand cmd) {
		Talent talent = ConvertHelper.convert(cmd, Talent.class);
		talent.setNamespaceId(namespaceId());
		talent.setEnabled(TrueOrFalseFlag.TRUE.getCode());
		talent.setDefaultOrder(0L);
		talent.setStatus(CommonStatus.ACTIVE.getCode());
		talentProvider.createTalent(talent);
		return talent;
	}

	private Talent updateTalent(CreateOrUpdateTalentCommand cmd) {
		Talent talent = findTalentById(cmd.getId(), namespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		talent.setName(cmd.getName());
		talent.setAvatarUri(cmd.getAvatarUri());
		talent.setPosition(cmd.getPosition());
		talent.setCategoryId(cmd.getCategoryId());
		talent.setGender(cmd.getGender());
		talent.setExperience(cmd.getExperience());
		talent.setGraduateSchool(cmd.getGraduateSchool());
		talent.setDegree(cmd.getDegree());
		talent.setPhone(cmd.getPhone());
		talent.setRemark(cmd.getRemark());
		talentProvider.updateTalent(talent);
		return talent;
	}
	
	@Override
	public void enableTalent(EnableTalentCommand cmd) {
		if (TrueOrFalseFlag.fromCode(cmd.getEnabled()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		Talent talent = findTalentById(cmd.getId(), namespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		talent.setEnabled(cmd.getEnabled());
		talentProvider.updateTalent(talent);
	}

	private Talent findTalentById(Long id, Integer namespaceId, String ownerType, Long ownerId) {
		Talent talent = talentProvider.findTalentById(id);
		if (talent == null || talent.getNamespaceId().intValue() != namespaceId.intValue() 
				|| !talent.getOwnerType().equals(ownerType) || talent.getOwnerId().longValue() != ownerId.longValue() 
				|| talent.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		return talent;
	}
	
	@Override
	public void deleteTalent(DeleteTalentCommand cmd) {
		Talent talent = findTalentById(cmd.getId(), namespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		talent.setStatus(CommonStatus.INACTIVE.getCode());
		talentProvider.updateTalent(talent);
	}

	@Override
	public void topTalent(TopTalentCommand cmd) {
		// 因为是按id倒序，所以直接把id改成最大的一个就可以了
		Talent talent = findTalentById(cmd.getId(), namespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		// 不能直接用update方法，那个是根据主键更新的
		talentProvider.updateTalentId(talent);
	}

	@Override
	public void importTalent(ImportTalentCommand cmd, MultipartFile[] attachment) {
		Integer namespaceId = namespaceId();
		ArrayList<RowResult> resultList = processorExcel(attachment[0]);
		List<CreateOrUpdateTalentCommand> commands = new ArrayList<>();
		try {
			for (int i = resultList.size() -1 ; i >= 2; i--) {
				RowResult r = resultList.get(i);
				CreateOrUpdateTalentCommand command = new CreateOrUpdateTalentCommand();
				command.setOwnerType(cmd.getOwnerType());
				command.setOwnerId(cmd.getOwnerId());
				command.setOrganizationId(cmd.getOrganizationId());
				command.setName(trim(r.getA()));
				command.setGender(getGender(trim(r.getB())));
				command.setPosition(trim(r.getC()));
				command.setCategoryId(getCategoryId(namespaceId, trim(r.getD())));
				command.setExperience(Integer.parseInt(trim(r.getE()).replace("年", "")));
				command.setGraduateSchool(trim(r.getF()));
				command.setDegree(getDegree(trim(r.getG())));
				command.setPhone(trim(r.getH()));
				command.setRemark(trim(r.getI()));
				ValidatorUtil.validate(command);
				commands.add(command);
			}
		} catch (Exception e) {
			if (e instanceof RuntimeErrorException) {
				throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
						TalentServiceErrorCode.ERROR_EXCEL, ((RuntimeErrorException)e).getMessage());
			}
			throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
					TalentServiceErrorCode.ERROR_EXCEL, "error excel");
		}
		

		dbProvider.execute(s->{
			for (CreateOrUpdateTalentCommand command : commands) {
				createTalent(command);
			}
			return null;
		});
	}

	private String trim(String string) {
		if (string == null) {
			return "";
		}
		return string.trim();
	}

	private Byte getDegree(String degree) {
		TalentDegreeEnum talentDegreeEnum = TalentDegreeEnum.fromName(degree);
		if (talentDegreeEnum == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		return talentDegreeEnum.getCode();
	}

	private Long getCategoryId(Integer namespaceId, String categoryName) {
		if (TalentCategory.otherName().equals(categoryName)) {
			return TalentCategory.otherId();
		}
		TalentCategory talentCategory = talentCategoryProvider.findTalentCategoryByName(namespaceId, categoryName);
		if (talentCategory == null) {
			TalentCategoryDTO talentCategoryDTO = new TalentCategoryDTO();
			talentCategoryDTO.setName(categoryName);
			talentCategory = createTalentCategory(talentCategoryDTO);
		}
		return talentCategory.getId();
	}

	private Byte getGender(String gender) {
		if (StringUtils.isNotBlank(gender)) {
			if (gender.equals("男")) {
				return UserGender.MALE.getCode();
			}else if (gender.equals("女")) {
				return UserGender.FEMALE.getCode();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<RowResult> processorExcel(MultipartFile file) {
		try {
            return PropMrgOwnerHandler.processorExcel(file.getInputStream());
        } catch (IOException e) {
			throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Process excel error.");
        }
	}

	@Override
	public GetTalentDetailResponse getTalentDetail(GetTalentDetailCommand cmd) {
		Talent talent = findTalentById(cmd.getId(), namespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		return new GetTalentDetailResponse(convert(talent, cmd.getAppFlag()));
	}

	private TalentDTO convert(Talent talent, Byte appFlag) {
		TalentDTO talentDTO = ConvertHelper.convert(talent, TalentDTO.class);
		TalentCategory talentCategory = findTalentCategoryById(talent.getCategoryId());
		talentDTO.setCategoryName(talentCategory.getName());
		talentDTO.setCategoryId(talentCategory.getId());
		talentDTO.setAvatarUrl(getAvatarUrl(appFlag, talent.getGender(), talent.getAvatarUri()));
		return talentDTO;
	}
	
	private String getAvatarUrl(Byte appFlag, Byte gender, String avatarUri) {
		if (TrueOrFalseFlag.fromCode(appFlag) == TrueOrFalseFlag.TRUE) {
			avatarUri = getDefaultAvatarUri(gender);
		}
		return contentServerService.parserUri(avatarUri);
	}
	
	private String getDefaultAvatarUri(Byte gender) {
		if (UserGender.fromCode(gender) == UserGender.MALE) {
			return configurationProvider.getValue("talent.male.uri", "");
		}
		return configurationProvider.getValue("talent.female.uri", "");
	}

	@Override
	public ListTalentQueryHistoryResponse listTalentQueryHistory(ListTalentQueryHistoryCommand cmd) {
		List<TalentQueryHistory> talentQueryHistories = talentQueryHistoryProvider.listTalentQueryHistoryByUser(userId());
		return new ListTalentQueryHistoryResponse(talentQueryHistories.stream().map(this::convert).collect(Collectors.toList()));
	}
	
	private TalentQueryHistoryDTO convert(TalentQueryHistory talentQueryHistory) {
		return ConvertHelper.convert(talentQueryHistory, TalentQueryHistoryDTO.class);
	}

	@Override
	public void deleteTalentQueryHistory(DeleteTalentQueryHistoryCommand cmd) {
		deleteTalentQueryHistory(findTalentQueryHistoryById(cmd.getId()));
	}
	
	private TalentQueryHistory findTalentQueryHistoryById(Long id) {
		TalentQueryHistory talentQueryHistory = talentQueryHistoryProvider.findTalentQueryHistoryById(id);
		if (talentQueryHistory == null || talentQueryHistory.getCreatorUid().longValue() != userId().longValue()
				|| talentQueryHistory.getStatus().byteValue() != CommonStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		return talentQueryHistory;
	}
	
	private void deleteTalentQueryHistory(TalentQueryHistory talentQueryHistory) {
		talentQueryHistory.setStatus(CommonStatus.INACTIVE.getCode());
		talentQueryHistoryProvider.updateTalentQueryHistory(talentQueryHistory);
	}
	
	@Override
	public void clearTalentQueryHistory(ClearTalentQueryHistoryCommand cmd) {
		List<TalentQueryHistory> talentQueryHistories = talentQueryHistoryProvider.listTalentQueryHistoryByUser(userId());
		dbProvider.execute(s->{
			talentQueryHistories.forEach(this::deleteTalentQueryHistory);
			return null;
		});
	}
	
	@Override
	public CreateOrUpdateRequestSettingResponse createOrUpdateRequestSetting(CreateOrUpdateRequestSettingCommand cmd) {
		Integer namespaceId = namespaceId();
		TrueOrFalseFlag enable = TrueOrFalseFlag.fromCode(cmd.getEnable());
		if (enable == TrueOrFalseFlag.TRUE) {
			configurationProvider.setValue(namespaceId, TALENT_REQUEST_NAME, cmd.getRequestName());
			configurationProvider.setLongValue(namespaceId, TALENT_FORM_ID, cmd.getFormId());
		}else {
			configurationProvider.deleteValue(namespaceId, TALENT_REQUEST_NAME);
			configurationProvider.deleteValue(namespaceId, TALENT_FORM_ID);
		}
		
		return null;
	}

	@Override
	public CreateOrUpdateRequestSettingResponse findRequestSetting() {
		Integer namespaceId = namespaceId();
		String talentRequestName = configurationProvider.getValue(namespaceId, TALENT_REQUEST_NAME, "");
		Long talentFormId = configurationProvider.getLongValue(namespaceId, TALENT_FORM_ID, 0L);
		if (StringUtils.isEmpty(talentRequestName) || talentFormId == 0L) {
			return new CreateOrUpdateRequestSettingResponse(TrueOrFalseFlag.FALSE.getCode(), null, null);
		}
		return new CreateOrUpdateRequestSettingResponse(TrueOrFalseFlag.TRUE.getCode(), talentRequestName, talentFormId);
	}

	private Long userId() {
		return UserContext.current().getUser().getId();
	}
	
	private Integer namespaceId() {
		return UserContext.getCurrentNamespaceId();
	} 

	@Override
	public void createMessageSender(CreateMessageSenderCommand cmd) {
		if (cmd.getSenders() == null || cmd.getSenders().isEmpty()) {
			return;
		}
		cmd.getSenders().forEach(t->{
			TalentMessageSender talentMessageSender = talentMessageSenderProvider.findTalentMessageSender(namespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), t.getOrganizationMemberId(), t.getUserId());
			if (talentMessageSender == null) {
				talentMessageSender = new TalentMessageSender();
				talentMessageSender.setNamespaceId(namespaceId());
				talentMessageSender.setOwnerType(cmd.getOwnerType());
				talentMessageSender.setOwnerId(cmd.getOwnerId());
				talentMessageSender.setStatus(CommonStatus.ACTIVE.getCode());
				talentMessageSender.setOrganizationMemberId(t.getOrganizationMemberId());
				talentMessageSender.setUserId(t.getUserId());
				talentMessageSenderProvider.createTalentMessageSender(talentMessageSender);
			}else {
				talentMessageSender.setStatus(CommonStatus.ACTIVE.getCode());
				talentMessageSenderProvider.updateTalentMessageSender(talentMessageSender);
			}
		});
	}

	private MessageSenderDTO convert(TalentMessageSender talentMessageSender) {
		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberById(talentMessageSender.getOrganizationMemberId());
		if (organizationMember != null) {
			return new MessageSenderDTO(talentMessageSender.getId(), organizationMember.getContactName(), organizationMember.getContactToken());
		}
		User user = userProvider.findUserById(talentMessageSender.getUserId());
		if (user != null) {
			List<UserIdentifier> userIdentifiers = userProvider.listUserIdentifiersOfUser(user.getId());
			if (userIdentifiers != null && !userIdentifiers.isEmpty()) {
				return new MessageSenderDTO(talentMessageSender.getId(), user.getNickName(), userIdentifiers.get(0).getIdentifierToken());
			}
		}
		return null;
	}

	@Override
	public void deleteMessageSender(DeleteMessageSenderCommand cmd) {
		TalentMessageSender talentMessageSender = talentMessageSenderProvider.findTalentMessageSenderById(cmd.getId());
		talentMessageSender.setStatus(CommonStatus.INACTIVE.getCode());
		talentMessageSenderProvider.updateTalentMessageSender(talentMessageSender);
	}

	@Override
	public ListMessageSenderResponse listMessageSender(ListMessageSenderCommand cmd) {
		List<TalentMessageSender> talentMessageSenders = talentMessageSenderProvider.listTalentMessageSenderByOwner(cmd.getOwnerType(), cmd.getOwnerId());
		return new ListMessageSenderResponse(talentMessageSenders.stream().map(this::convert).collect(Collectors.toList()));
	}
	
	@Override
	public ListTalentRequestResponse listTalentRequest(ListTalentRequestCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		cmd.setPageSize(pageSize+1);
		List<TalentRequest> talentRequests = talentRequestProvider.listTalentRequestByCondition(namespaceId(), cmd);
		Long nextPageAnchor = null;
		if (talentRequests.size() > pageSize) {
			talentRequests.remove(talentRequests.size()-1);
			nextPageAnchor = talentRequests.get(talentRequests.size()-1).getId();
		}
		
		return new ListTalentRequestResponse(nextPageAnchor, talentRequests.stream().map(this::convertWithoutDetail).collect(Collectors.toList()));
	}

	private TalentRequestDTO convertWithoutDetail(TalentRequest talentRequest) {
		TalentRequestDTO talentRequestDTO = new TalentRequestDTO();
		talentRequestDTO.setId(talentRequest.getId());
		talentRequestDTO.setRequestor(talentRequest.getRequestor());
		talentRequestDTO.setPhone(talentRequest.getPhone());
		talentRequestDTO.setOrganizationName(talentRequest.getOrganizationName());
		talentRequestDTO.setCreateTime(talentRequest.getCreateTime().getTime());
		talentRequestDTO.setTalentId(talentRequest.getTalentId());
		Talent talent = talentProvider.findTalentById(talentRequest.getTalentId());
		talentRequestDTO.setTalentName(talent.getName());
		return talentRequestDTO;
	}
	
	@Override
	public GetTalentRequestDetailResponse getTalentRequestDetail(GetTalentRequestDetailCommand cmd) {
		TalentRequest talentRequest = talentRequestProvider.findTalentRequestById(cmd.getId());
		return new GetTalentRequestDetailResponse(convert(talentRequest));
	}

	private TalentRequestDTO convert(TalentRequest talentRequest) {
		TalentRequestDTO talentRequestDTO = convertWithoutDetail(talentRequest);
		GetGeneralFormValuesCommand cmd = new GetGeneralFormValuesCommand();
        cmd.setSourceType(EntityType.TALENT_REQUEST.getCode());
        cmd.setSourceId(talentRequest.getId());
        List<FlowCaseEntity> flowCaseEntities = generalFormService.getGeneralFormFlowEntities(cmd);
		talentRequestDTO.setFlowCaseEntities(flowCaseEntities);
		return talentRequestDTO;
	}

}