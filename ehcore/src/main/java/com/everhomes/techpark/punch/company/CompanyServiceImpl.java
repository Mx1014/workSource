package com.everhomes.techpark.punch.company;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
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

		List<CompanyPhoneList> list = this.getComPhoneList(resultList);
		if(list != null && !list.isEmpty()){
			this.dbProvider.execute(s ->{
				for(CompanyPhoneList r:list){
					String telephone = r.getTelephone();
					if(telephone == null) continue;
					CompanyPhoneList comPhone = companyProvider.findComPhoneListByPhone(telephone);
					if(comPhone != null){
						comPhone.setName(r.getName());
						comPhone.setDepartment(r.getDepartment());
						companyProvider.updateComPhoneList(comPhone);
					}
					else {
						comPhone = new CompanyPhoneList();
						this.setComPhoneAtCreate(comPhone,0L,currentTimeStamp,user.getId());
						comPhone.setDepartment(r.getDepartment());
						comPhone.setName(r.getName());
						comPhone.setTelephone(r.getTelephone());
						companyProvider.createComPhoneList(comPhone);
					}
				}
				return s;
			});
		}
	}

	private void setComPhoneAtCreate(CompanyPhoneList comPhone,Long comId, Timestamp currentTimeStamp,Long userId) {
		comPhone.setCompanyId(comId);
		comPhone.setCreateTime(currentTimeStamp);
		comPhone.setCreateUid(userId);
		comPhone.setUserId(0L);
	}

	private List<CompanyPhoneList> getComPhoneList(ArrayList resultList) {
		List<CompanyPhoneList> list = new ArrayList<CompanyPhoneList>();
		if(resultList != null && resultList.size() > 0){
			for(int i=1;i<resultList.size();i++){
				CompanyPhoneList comPhone = new CompanyPhoneList();
				RowResult row = (RowResult) resultList.get(i);

				if(row.getA() == null || row.getA().trim().equals(""))	continue;
				if(row.getB() == null || row.getB().trim().equals("")) continue;

				comPhone.setName(row.getA().trim());
				comPhone.setTelephone(row.getB().trim());
				comPhone.setDepartment(row.getC() == null ? null:row.getC().trim());

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
		CompanyPhoneList comPhone = this.checkCompanyPhoneList(cmd.getId());
		
		if(cmd.getDepartment() != null && !cmd.getDepartment().trim().isEmpty())
			comPhone.setDepartment(cmd.getDepartment().trim());
		if(cmd.getName() != null && !cmd.getName().trim().isEmpty())
			comPhone.setName(cmd.getName().trim());
		if(cmd.getTelephone() != null && !cmd.getTelephone().trim().isEmpty())
			comPhone.setTelephone(cmd.getTelephone().trim());
		this.companyProvider.updateComPhoneList(comPhone);
	}

	private CompanyPhoneList checkCompanyPhoneList(Long id) {
		CompanyPhoneList comPhone = this.companyProvider.findCompanyPhoneListById(id);
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
		
		CompanyPhoneList comPhone = new CompanyPhoneList();
		Long comId = cmd.getCompanyId() == null ? 0L:cmd.getCompanyId();
		
		this.setComPhoneAtCreate(comPhone, comId, currentTimeStamp, user.getId());
		comPhone.setDepartment(cmd.getDepartment() == null ? "":cmd.getDepartment().trim());
		comPhone.setName(cmd.getName().trim());
		comPhone.setTelephone(cmd.getTelephone().trim());
		this.companyProvider.createComPhoneList(comPhone);
	}

}
