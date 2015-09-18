package com.everhomes.techpark.company;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.company.ContactType;
import com.everhomes.techpark.company.CreateGroupContactCommand;
import com.everhomes.techpark.company.DeleteGroupContactCommand;
import com.everhomes.techpark.company.GroupContact;
import com.everhomes.techpark.company.GroupContactDTO;
import com.everhomes.techpark.company.GroupContactProvider;
import com.everhomes.techpark.company.GroupContactService;
import com.everhomes.techpark.company.ImportGroupContactsCommand;
import com.everhomes.techpark.company.ListGroupContactsCommand;
import com.everhomes.techpark.company.ListGroupContactsCommandResponse;
import com.everhomes.techpark.company.OwnerType;
import com.everhomes.techpark.company.UpdateGroupContactCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component
public class CompanyServiceImpl implements GroupContactService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private GroupContactProvider groupContactProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;

	@SuppressWarnings("rawtypes")
	@Override
	public void importGroupContacts(ImportGroupContactsCommand cmd, MultipartFile[] files) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

		this.checkOwnerIdIsNull(cmd.getOwnerId(),user.getId());
		
		ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<GroupContact> list = this.listGroupContacts(resultList);
		if(list != null && !list.isEmpty()){
			this.dbProvider.execute(s ->{
				for(GroupContact r:list){
					String telephone = r.getContactToken();
					if(telephone == null) continue;
					GroupContact gContact = groupContactProvider.findGroupContactByToken(telephone);
					if(gContact != null){
						gContact.setContactName(r.getContactName());
						groupContactProvider.updateGroupContact(gContact);
					}
					else {
						gContact = new GroupContact();
						this.setGroupContactAtCreate(gContact,cmd.getOwnerId(),currentTimeStamp,user.getId());
						gContact.setContactName(r.getContactName());
						gContact.setContactType(ContactType.MOBILE.getCode());
						gContact.setContactToken(r.getContactToken());
						groupContactProvider.createGroupContact(gContact);
					}
				}
				return s;
			});
		}
	}

	private void setGroupContactAtCreate(GroupContact gContact,Long comId, Timestamp currentTimeStamp,Long userId) {
		gContact.setOwnerType(OwnerType.COMPANY.getCode());
		gContact.setOwnerId(comId);
		gContact.setCreateTime(currentTimeStamp);
		gContact.setCreatorUid(userId);
		gContact.setContactUid(0L);
	}

	private List<GroupContact> listGroupContacts(ArrayList resultList) {
		List<GroupContact> list = new ArrayList<GroupContact>();
		if(resultList != null && resultList.size() > 0){
			for(int i=1;i<resultList.size();i++){
				GroupContact gContact = new GroupContact();
				RowResult row = (RowResult) resultList.get(i);

				if(row.getA() == null || row.getA().trim().equals(""))	continue;
				if(row.getB() == null || row.getB().trim().equals("")) continue;

				gContact.setContactName(row.getA().trim());
				gContact.setContactToken(row.getB().trim());

				list.add(gContact);
			}
		}
		return list;
	}

	@Override
	public void updateGroupContact(UpdateGroupContactCommand cmd) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		
		this.checkIdIsNull(cmd.getId(), user.getId());
		GroupContact gContact = this.checkGroupContact(cmd.getId());
		
		if(cmd.getContactName() != null && !cmd.getContactName().trim().isEmpty())
			gContact.setContactName(cmd.getContactName().trim());
		if(cmd.getContactToken() != null && !cmd.getContactToken().trim().isEmpty())
			gContact.setContactToken(cmd.getContactToken().trim());
		this.groupContactProvider.updateGroupContact(gContact);
	}

	private GroupContact checkGroupContact(Long id) {
		GroupContact comPhone = this.groupContactProvider.findGroupContactById(id);
		if(comPhone == null){
			LOGGER.error("companyPhoneList not be found.id="+id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"companyPhoneList not be found.");
		}
		return comPhone;
	}

	private void checkContactTokenIsNull(String telephone,Long userId) {
		if(telephone == null || telephone.trim().isEmpty()){
			LOGGER.error("telephone is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"telephone is empty.");
		}
	}

	private void checkContactNameIsNull(String name,Long userId) {
		if(name == null || name.trim().isEmpty()){
			LOGGER.error("name is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"name is empty.");
		}
	}

	@Override
	public void deleteGroupContact(DeleteGroupContactCommand cmd) {
		User user = UserContext.current().getUser();
		this.checkIdIsNull(cmd.getId(),user.getId());
		this.groupContactProvider.deleteGroupContactById(cmd.getId());
	}

	private void checkIdIsNull(Long id,Long userId) {
		if(id == null){
			LOGGER.error("id is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"id is empty.");
		}
	}

	@Override
	public void createGroupContact(CreateGroupContactCommand cmd) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		
		this.checkOwnerIdIsNull(cmd.getOwnerId(),user.getId());
		this.checkContactNameIsNull(cmd.getContactName(), user.getId());
		this.checkContactTokenIsNull(cmd.getContactToken(), user.getId());
		
		GroupContact gContact = new GroupContact();
		this.setGroupContactAtCreate(gContact, cmd.getOwnerId(), currentTimeStamp, user.getId());
		gContact.setContactName(cmd.getContactName().trim());
		gContact.setContactToken(cmd.getContactToken().trim());
		this.groupContactProvider.createGroupContact(gContact);
	}

	@Override
	public ListGroupContactsCommandResponse listGroupContacts(ListGroupContactsCommand cmd) {
		User user = UserContext.current().getUser();
		
		this.checkOwnerIdIsNull(cmd.getOwnerId(),user.getId());
		if(cmd.getPageOffset() == null)	cmd.setPageOffset(1);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long offset = PaginationHelper.offsetFromPageOffset((long)cmd.getPageOffset(), pageSize);
		
		ListGroupContactsCommandResponse result = new ListGroupContactsCommandResponse();
		List<GroupContactDTO> listDto = new ArrayList<GroupContactDTO>();
		
		List<GroupContact> list = this.groupContactProvider.listGroupContactsByKeword(cmd.getOwnerId(),cmd.getKeyword(),pageSize+1,offset);
		if(list != null && !list.isEmpty()){
			if(list.size() == pageSize+1){
				result.setNextPageOffset(cmd.getPageOffset()+1);
				list.remove(list.get(list.size()-1));
			}
			if(!list.isEmpty()){
				list.stream().map(r -> {
					GroupContactDTO dto = ConvertHelper.convert(r, GroupContactDTO.class);
					dto.setCreateTime(r.getCreateTime() == null ? null:r.getCreateTime().getTime());
					dto.setUpdateTime(r.getUpdateTime() == null ? null:r.getUpdateTime().getTime());
					listDto.add(dto);
					return null;
				}).toArray();
			}
		}
		
		result.setList(listDto);
		return result;
	}

	private void checkOwnerIdIsNull(Long ownerId,Long userId) {
		if(ownerId == null){
			LOGGER.error("ownerId is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"ownerId is empty.");
		}
		
	}

}
