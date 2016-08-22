package com.everhomes.journal;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.journal.CreateJournalCommand;
import com.everhomes.rest.journal.DeleteJournalCommand;
import com.everhomes.rest.journal.GetJournalCommand;
import com.everhomes.rest.journal.GetJournalConfigCommand;
import com.everhomes.rest.journal.JournalConfigDTO;
import com.everhomes.rest.journal.JournalDTO;
import com.everhomes.rest.journal.JournalStatus;
import com.everhomes.rest.journal.ListJournalsCommand;
import com.everhomes.rest.journal.ListJournalsResponse;
import com.everhomes.rest.journal.UpdateJournalCommand;
import com.everhomes.rest.journal.UpdateJournalConfigCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class JournalServiceImpl implements JournalService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalServiceImpl.class);

	@Autowired
	private JournalProvider journalProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
    private ContentServerService contentServerService;
	
	@Override
	public ListJournalsResponse listJournals(ListJournalsCommand cmd) {
		ListJournalsResponse response = new ListJournalsResponse();
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		if(null == cmd.getNamespaceId()) {
        	LOGGER.error("NamespaceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"NamespaceId cannot be null.");
        }
		List<Journal> ret = journalProvider.listJournals(cmd.getNamespaceId(), cmd.getKeyword(), cmd.getPageAnchor(), cmd.getPageSize());
		if(ret.size() > 0){
    		response.setJournals(ret.stream().map(r -> {
    			Long userId = r.getCreatorUid();
    			User user = userProvider.findUserById(userId);
    			JournalDTO dto = ConvertHelper.convert(r, JournalDTO.class);
    			dto.setCreatorName(user.getNickName());
    			String coverUrl = getResourceUrlByUir(r.getCoverUri(), 
    	                EntityType.USER.getCode(), r.getCreatorUid());
    			dto.setCoverUrl(coverUrl);
    			return dto;
    		}).collect(Collectors.toList()));
    		if(pageSize != null && ret.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(ret.get(ret.size()-1).getCreateTime().getTime());
        	}
    	}
		
		return response;
	}

	@Override
	public JournalDTO getJournal(GetJournalCommand cmd) {
		checkParam(cmd.getNamespaceId(), cmd.getId());
		Journal journal = journalProvider.findJournal(cmd.getId());
		User user = userProvider.findUserById(journal.getCreatorUid());
		JournalDTO dto = ConvertHelper.convert(journal, JournalDTO.class);
		dto.setCreatorName(user.getNickName());
		String coverUrl = getResourceUrlByUir(journal.getCoverUri(), 
                EntityType.USER.getCode(), journal.getCreatorUid());
		dto.setCoverUrl(coverUrl);
		return dto;
	}

	@Override
	public void updateJournal(UpdateJournalCommand cmd) {
		checkParam(cmd.getNamespaceId(), cmd.getId());
		Journal journal = journalProvider.findJournal(cmd.getId());
		if(null == journal){
			LOGGER.error("Journal not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Journal not found");
		}
		if(!cmd.getNamespaceId().equals(journal.getNamespaceId())){
			LOGGER.error("Journal not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"Journal not found");
		}
		journal.setTitle(cmd.getTitle());
		journal.setContent(cmd.getContent());
		journal.setCoverUri(cmd.getCoverUri());
		journalProvider.updateJournal(journal);		
	}

	@Override
	public void deleteJournal(DeleteJournalCommand cmd) {
		checkParam(cmd.getNamespaceId(), cmd.getId());
		Journal journal = journalProvider.findJournal(cmd.getId());
		if(null == journal){
			LOGGER.error("Journal not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Journal not found");
		}
		journal.setStatus(JournalStatus.INACTIVE.getCode());
		journalProvider.updateJournal(journal);
	}

	@Override
	public void updateJournalConfig(UpdateJournalConfigCommand cmd) {
		if(null == cmd.getNamespaceId()) {
        	LOGGER.error("NamespaceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"NamespaceId cannot be null.");
        }
		JournalConfig journalConfig = journalProvider.findJournalConfig(cmd.getNamespaceId());
		long userId = UserContext.current().getUser().getId();
		if(null == journalConfig){
			journalConfig = new JournalConfig();
			journalConfig.setPosterPath(cmd.getPosterPath());
			journalConfig.setDescription(cmd.getDescription());
			journalConfig.setCreatorUid(userId);
			journalConfig.setNamespaceId(cmd.getNamespaceId());
			journalConfig.setCreateTime(new Timestamp(System.currentTimeMillis()));
			journalProvider.createJournalConfig(journalConfig);
		}else{
			journalConfig.setPosterPath(cmd.getPosterPath());
			journalConfig.setDescription(cmd.getDescription());
			journalProvider.updateJournalConfig(journalConfig);
		}
	}

	@Override
	public JournalConfigDTO getJournalConfig(GetJournalConfigCommand cmd) {
		if(null == cmd.getNamespaceId()) {
        	LOGGER.error("NamespaceId cannot be null, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"NamespaceId cannot be null.");
        }
		JournalConfig ret = journalProvider.findJournalConfig(cmd.getNamespaceId());
		JournalConfigDTO dto = ConvertHelper.convert(ret, JournalConfigDTO.class);
		if(null != dto) {
			
			String posterPath = ret.getPosterPath();
			if(StringUtils.isBlank(ret.getPosterPath())){
				posterPath = configProvider.getValue("journal.posterPath", "");
			}
			String posterPathUrl = getResourceUrlByUir(posterPath, 
	                EntityType.USER.getCode(), ret.getCreatorUid());
			dto.setPosterPath(posterPath);
			dto.setPosterPathUrl(posterPathUrl);
        }else{
        	dto = new JournalConfigDTO();
        	long userId = UserContext.current().getUser().getId();
        	String posterPath = configProvider.getValue("journal.posterPath", "");
			String posterPathUrl = getResourceUrlByUir(posterPath, 
	                EntityType.USER.getCode(), userId);
			dto.setPosterPath(posterPath);
			dto.setPosterPathUrl(posterPathUrl);
        }
		
		return dto;
	}
	
	private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }
        
        return url;
    }
	 private void checkParam(Integer namespaceId, Long id){
	    	if(id == null ) {
	        	LOGGER.error("Id cannot be null.");
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	    				"Id cannot be null.");
	        }
	    	
	    	if(null == namespaceId) {
	        	LOGGER.error("NamespaceId cannot be null.");
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	    				"NamespaceId cannot be null.");
	        }
	  }

	@Override
	public void createJournal(CreateJournalCommand cmd) {
		if(null == cmd.getNamespaceId()) {
        	LOGGER.error("NamespaceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"NamespaceId cannot be null.");
        }
		Journal journal = new Journal();
		journal.setNamespaceId(cmd.getNamespaceId());
		journal.setStatus(JournalStatus.ACTIVE.getCode());
		journal.setTitle(cmd.getTitle());
		journal.setContent(cmd.getContent());
		journal.setContentType(cmd.getContentType());
		journal.setCoverUri(cmd.getCoverUri());
		journal.setCreateTime(new Timestamp(System.currentTimeMillis()));
		journal.setCreatorUid(UserContext.current().getUser().getId());
		journalProvider.createJournal(journal);
	}
}
