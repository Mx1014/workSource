package com.everhomes.techpark.punch.company;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component
public class CompanyServiceImpl implements CompanyService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private CompanyProvider companyProvider;

	@SuppressWarnings("rawtypes")
	@Override
	public void importContacts(ImportContactsCommand cmd, MultipartFile[] files) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

		ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<GroupContact> list = this.getComPhoneList(resultList);
		if(list != null && !list.isEmpty()){
			this.dbProvider.execute(s ->{
				for(GroupContact r:list){
					String telephone = r.getContactToken();
					if(telephone == null) continue;
					GroupContact comPhone = companyProvider.findComPhoneListByPhone(telephone);
					if(comPhone != null){
						comPhone.setContactName(r.getContactName());
						companyProvider.updateComPhoneList(comPhone);
					}
					else {
						comPhone = new GroupContact();
						this.setComPhoneAtCreate(comPhone,0L,currentTimeStamp,user.getId());
						comPhone.setContactName(r.getContactName());
						comPhone.setContactType(ContactType.MOBILE.getCode());
						comPhone.setContactToken(r.getContactToken());
						companyProvider.createComPhoneList(comPhone);
					}
				}
				return s;
			});
		}
	}

	private void setComPhoneAtCreate(GroupContact comPhone,Long comId, Timestamp currentTimeStamp,Long userId) {
		comPhone.setOwnerType(OwnerType.COMPANY.getCode());
		comPhone.setOwnerId(comId);
		comPhone.setCreateTime(currentTimeStamp);
		comPhone.setCreatorUid(userId);
		comPhone.setContactUid(0L);
	}

	private List<GroupContact> getComPhoneList(ArrayList resultList) {
		List<GroupContact> list = new ArrayList<GroupContact>();
		if(resultList != null && resultList.size() > 0){
			for(int i=1;i<resultList.size();i++){
				GroupContact comPhone = new GroupContact();
				RowResult row = (RowResult) resultList.get(i);

				if(row.getA() == null || row.getA().trim().equals(""))	continue;
				if(row.getB() == null || row.getB().trim().equals("")) continue;

				comPhone.setContactName(row.getA().trim());
				comPhone.setContactToken(row.getB().trim());

				list.add(comPhone);
			}
		}
		return list;
	}

	@Override
	public void updateContacts(UpdateContactsCommand cmd) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		
		this.checkIdIsNull(cmd.getId(), user.getId());
		GroupContact comPhone = this.checkCompanyPhoneList(cmd.getId());
		
		if(cmd.getContactName() != null && !cmd.getContactName().trim().isEmpty())
			comPhone.setContactName(cmd.getContactName().trim());
		if(cmd.getContactToken() != null && !cmd.getContactToken().trim().isEmpty())
			comPhone.setContactToken(cmd.getContactToken().trim());
		this.companyProvider.updateComPhoneList(comPhone);
	}

	private GroupContact checkCompanyPhoneList(Long id) {
		GroupContact comPhone = this.companyProvider.findCompanyPhoneListById(id);
		if(comPhone == null){
			LOGGER.error("companyPhoneList not be found.id="+id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"companyPhoneList not be found.");
		}
		return comPhone;
	}

	private void checkTelephoneIsNull(String telephone,Long userId) {
		if(telephone == null || telephone.trim().isEmpty()){
			LOGGER.error("telephone is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"telephone is empty.");
		}
	}

	private void checkNameIsNull(String name,Long userId) {
		if(name == null || name.trim().isEmpty()){
			LOGGER.error("name is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"name is empty.");
		}
	}

	@Override
	public void deleteContacts(DeleteContactsCommand cmd) {
		User user = UserContext.current().getUser();
		this.checkIdIsNull(cmd.getId(),user.getId());
		this.companyProvider.deleteComPhoneListById(cmd.getId());
	}

	private void checkIdIsNull(Long id,Long userId) {
		if(id == null){
			LOGGER.error("id is empty.userId="+userId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"id is empty.");
		}
	}

	@Override
	public void createContacts(CreateContactsCommand cmd) {
		User user = UserContext.current().getUser();
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		
		this.checkNameIsNull(cmd.getName(), user.getId());
		this.checkTelephoneIsNull(cmd.getTelephone(), user.getId());
		
		GroupContact comPhone = new GroupContact();
		Long comId = cmd.getCompanyId() == null ? 0L:cmd.getCompanyId();
		
		this.setComPhoneAtCreate(comPhone, comId, currentTimeStamp, user.getId());
		comPhone.setContactName(cmd.getName().trim());
		comPhone.setContactToken(cmd.getTelephone().trim());
		this.companyProvider.createComPhoneList(comPhone);
	}

}
