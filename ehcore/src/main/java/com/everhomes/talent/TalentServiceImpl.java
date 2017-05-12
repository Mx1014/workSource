// @formatter:off
package com.everhomes.talent;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.talent.CreateOrUpdateTalentCategoryCommand;
import com.everhomes.rest.talent.CreateOrUpdateTalentCommand;
import com.everhomes.rest.talent.DeleteTalentCategoryCommand;
import com.everhomes.rest.talent.DeleteTalentCommand;
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
import com.everhomes.rest.talent.TalentQueryHistoryDTO;
import com.everhomes.rest.talent.TopTalentCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhTalents;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

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
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private TalentQueryHistoryProvider talentQueryHistoryProvider;
	
	@Override
	public ListTalentCategoryResponse listTalentCategory(ListTalentCategoryCommand cmd) {
		List<TalentCategory> talentCategories = talentCategoryProvider.listTalentCategoryByNamespace(namespaceId());
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
					if (t.getId() == null) {
						createTalentCategory(t);
					}else {
						updateTalentCategory(t);
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
			if (talentCategory != null && (t.getId() == null || t.getId() != talentCategory.getId())) {
				throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
						TalentServiceErrorCode.DUPLICATED_NAME, "duplicated name");
			}
		});
	}

	private void createTalentCategory(TalentCategoryDTO talentCategoryDTO) {
		TalentCategory talentCategory = ConvertHelper.convert(talentCategoryDTO, TalentCategory.class);
		talentCategory.setNamespaceId(namespaceId());
		talentCategory.setStatus(CommonStatus.ACTIVE.getCode());
		talentCategoryProvider.createTalentCategory(talentCategory);
	}

	private void updateTalentCategory(TalentCategoryDTO talentCategoryDTO) {
		TalentCategory talentCategory = findTalentCategoryById(talentCategoryDTO.getId());
		talentCategory.setName(talentCategoryDTO.getName());
		talentCategoryProvider.updateTalentCategory(talentCategory);
	}

	private TalentCategory findTalentCategoryById(Long id) {
		TalentCategory talentCategory = talentCategoryProvider.findTalentCategoryById(id);
		if (talentCategory == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		return talentCategory;
	}
	
	@Override
	public void deleteTalentCategory(DeleteTalentCategoryCommand cmd) {
		TalentCategory talentCategory = findTalentCategoryById(cmd.getId());
		talentCategory.setStatus(CommonStatus.INACTIVE.getCode());
		talentCategoryProvider.updateTalentCategory(talentCategory);
	}

	@Override
	public ListTalentResponse listTalent(ListTalentCommand cmd) {
	
		return new ListTalentResponse();
	}

	@Override
	public void createOrUpdateTalent(CreateOrUpdateTalentCommand cmd) {
	

	}

	@Override
	public void enableTalent(EnableTalentCommand cmd) {
		if (TrueOrFalseFlag.fromCode(cmd.getEnabled()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		Talent talent = findTalentById(cmd.getId());
		talent.setEnabled(cmd.getEnabled());
		talentProvider.updateTalent(talent);
	}

	private Talent findTalentById(Long id) {
		Talent talent = talentProvider.findTalentById(id);
		if (talent == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		return talent;
	}
	
	@Override
	public void deleteTalent(DeleteTalentCommand cmd) {
		Talent talent = findTalentById(cmd.getId());
		talent.setStatus(CommonStatus.INACTIVE.getCode());
		talentProvider.updateTalent(talent);
	}

	@Override
	public void topTalent(TopTalentCommand cmd) {
		// 因为是按id倒序，所以直接把id改成最大的一个就可以了
		Talent talent = findTalentById(cmd.getId());
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTalents.class));
		talent.setId(id);
		talentProvider.updateTalent(talent);
	}

	@Override
	public void importTalent(ImportTalentCommand cmd, MultipartFile[] attachment) {
		

	}

	@Override
	public GetTalentDetailResponse getTalentDetail(GetTalentDetailCommand cmd) {
		Talent talent = findTalentById(cmd.getId());
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

	private Long userId() {
		return UserContext.current().getUser().getId();
	}
	
	private Integer namespaceId() {
		return UserContext.getCurrentNamespaceId();
	} 
}