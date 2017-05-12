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
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.talent.ClearTalentQueryHistoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCategoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteTalentCategoryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
import com.everhomes.rest.talent.DeleteTalentQueryHistoryCommand;
import com.everhomes.rest.talent.EnableTalentCommand;
import com.everhomes.rest.talent.GetTalentDetailCommand;
import com.everhomes.rest.talent.GetTalentDetailResponse;
import com.everhomes.rest.talent.ImportTalentCommand;
import com.everhomes.rest.talent.ListTalentCategoryCommand;
import com.everhomes.rest.talent.ListTalentCategoryResponse;
import com.everhomes.rest.talent.ListTalentCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryCommand;
import com.everhomes.rest.talent.ListTalentQueryHistoryResponse;
import com.everhomes.rest.talent.ListTalentResponse;
import com.everhomes.rest.talent.TalentCategoryDTO;
import com.everhomes.rest.talent.TalentDTO;
import com.everhomes.rest.talent.TalentDegreeEnum;
import com.everhomes.rest.talent.TalentQueryHistoryDTO;
import com.everhomes.rest.talent.TopTalentCommand;
import com.everhomes.rest.user.UserGender;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
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
		talentCategory.setName(talentCategoryDTO.getName());
		talentCategoryProvider.updateTalentCategory(talentCategory);
	}

	private TalentCategory findTalentCategoryById(Long id) {
		if (id.longValue() == TalentCategory.otherId().longValue()) {
			return TalentCategory.other();
		}else {
			TalentCategory talentCategory = talentCategoryProvider.findTalentCategoryById(id);
			if (talentCategory == null || talentCategory.getNamespaceId().intValue() != namespaceId().intValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
			}
			return talentCategory;
		}
	}
	
	@Override
	public void deleteTalentCategory(DeleteTalentCategoryCommand cmd) {
		if (cmd.getId().longValue() != TalentCategory.otherId().longValue()) {
			TalentCategory talentCategory = findTalentCategoryById(cmd.getId());
			talentCategory.setStatus(CommonStatus.INACTIVE.getCode());
			talentCategoryProvider.updateTalentCategory(talentCategory);
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
		
		return new ListTalentResponse(nextPageAnchor, talents.stream().map(this::convertWithoutRemark).collect(Collectors.toList()));
	}
	
	private void addQueryHistory(String keyword) {
		//如果已有,先删除再添加,这样可以保证按倒序排列
		TalentQueryHistory talentQueryHistory = talentQueryHistoryProvider.findTalentQueryHistoryByKeyword(userId(), keyword);
		if (talentQueryHistory != null) {
			talentQueryHistoryProvider.deleteTalentQueryHistory(talentQueryHistory);
		}
		talentQueryHistoryProvider.createTalentQueryHistory(talentQueryHistory);
	}

	private TalentDTO convertWithoutRemark(Talent talent) {
		TalentDTO talentDTO = convert(talent);
		talentDTO.setRemark(null);
		return talentDTO;
	}

	@Override
	public void createOrUpdateTalent(CreateOrUpdateTalentCommand cmd) {
		if (cmd.getId() == null) {
			createTalent(cmd);
		}else {
			updateTalent(cmd);
		}
	}

	private void createTalent(CreateOrUpdateTalentCommand cmd) {
		Talent talent = ConvertHelper.convert(cmd, Talent.class);
		talent.setNamespaceId(namespaceId());
		talent.setEnabled(TrueOrFalseFlag.TRUE.getCode());
		talent.setDefaultOrder(0L);
		talent.setStatus(CommonStatus.ACTIVE.getCode());
		talentProvider.createTalent(talent);
	}

	private void updateTalent(CreateOrUpdateTalentCommand cmd) {
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
				|| !talent.getOwnerType().equals(ownerType) || talent.getOwnerId().longValue() != ownerId.longValue()) {
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
		dbProvider.execute(s->{
			for (int i = 2; i < resultList.size(); i++) {
				RowResult r = resultList.get(i);
				CreateOrUpdateTalentCommand command = new CreateOrUpdateTalentCommand();
				command.setOwnerType(cmd.getOwnerType());
				command.setOwnerId(cmd.getOwnerId());
				command.setOrganizationId(cmd.getOrganizationId());
				command.setName(r.getA().trim());
				command.setGender(getGender(r.getB().trim()));
				command.setPosition(r.getC().trim());
				command.setCategoryId(getCategoryId(namespaceId, r.getD().trim()));
				command.setExperience(Integer.parseInt(r.getE().trim().replace("年", "")));
				command.setGraduateSchool(r.getF());
				command.setDegree(getDegree(r.getG()));
				command.setPhone(r.getH());
				command.setRemark(r.getI());
				ValidatorUtil.validate(command);
				createTalent(command);
			}
			return null;
		});
	}
	
	private Byte getDegree(String degree) {
		TalentDegreeEnum talentDegreeEnum = TalentDegreeEnum.fromName(degree);
		if (talentDegreeEnum == null) {
			talentDegreeEnum = TalentDegreeEnum.UNKNOWN;
		}
		return talentDegreeEnum.getCode();
	}

	private Long getCategoryId(Integer namespaceId, String categoryName) {
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
		return new GetTalentDetailResponse(convert(talent));
	}

	private TalentDTO convert(Talent talent) {
		TalentDTO talentDTO = ConvertHelper.convert(talent, TalentDTO.class);
		talentDTO.setCategoryName(findTalentCategoryById(talent.getCategoryId()).getName());
		talentDTO.setAvatarUrl(contentServerService.parserUri(talent.getAvatarUri()));
		return talentDTO;
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
		if (talentQueryHistory == null || talentQueryHistory.getCreatorUid().longValue() != userId().longValue()) {
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
	
	private Long userId() {
		return UserContext.current().getUser().getId();
	}
	
	private Integer namespaceId() {
		return UserContext.getCurrentNamespaceId();
	} 
	
}