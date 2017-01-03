package com.everhomes.general_approval;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.approval.ApprovalExceptionContent;
import com.everhomes.rest.flow.FlowButtonStatus;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormTemplateType;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class GeneralApprovalServiceImpl implements GeneralApprovalService {
	@Autowired
	private GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;

	@Autowired
	private DbProvider dbProvider;
	
	@Override
	public GetTemplateByApprovalIdResponse getTemplateByApprovalId(
			GetTemplateByApprovalIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralFormDTO createApprovalForm(CreateApprovalFormCommand cmd) {

		GeneralForm form = ConvertHelper.convert(cmd, GeneralForm.class);
		form.setTemplateType(GeneralFormTemplateType.DEFAULT_JSON.getCode());
		form.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		form.setStatus(GeneralFormStatus.CONFIG.getCode());
		form.setNamespaceId(UserContext.getCurrentNamespaceId());
		form.setFormVersion(0L);
		form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
		this.generalFormProvider.createGeneralForm(form);
		return processGeneralFormDTO(form);
	}

	public GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		dto.setFormFields(fieldDTOs);
		return dto;
	}

	@Override
	public GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd) {
		return this.dbProvider.execute((TransactionStatus status) -> {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormOriginId() );
			if(null == form || form.getStatus().equals(GeneralFormStatus.INVALID.getCode()))
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
						"form not found");
			form.setFormName(cmd.getFormName());
			form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
			form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			if(form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())){
				//如果是config状态的直接改
				this.generalFormProvider.updateGeneralForm(form);
			}else if(form.getStatus().equals(GeneralFormStatus.RUNNING.getCode())){
				//如果是RUNNING状态的,置原form为失效,重新create一个版本+1的config状态的form
				form.setStatus(GeneralFormStatus.INVALID.getCode());
				this.generalFormProvider.updateGeneralForm(form);
				form.setFormVersion(form.getFormVersion()+1);
				form.setStatus(GeneralFormStatus.CONFIG.getCode());
				this.generalFormProvider.createGeneralForm(form);
			}
			return processGeneralFormDTO(form);
		});
	}

	/**
	 * 取状态不为失效的form
	 * */
	@Override
	public ListGeneralFormResponse listApprovalForms(ListApprovalFormsCommand cmd) {
		
		List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(null, Integer.MAX_VALUE-1,  new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(cmd.getOwnerId()));
				query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(cmd.getOwnerType())); 
				query.addConditions(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode())); 
				return query;
			}
    	});
		ListGeneralFormResponse resp = new ListGeneralFormResponse();
		resp.setForms(forms.stream().map((r)->{return processGeneralFormDTO(r);}).collect(Collectors.toList()));
		return resp;
	}

	@Override
	public void deleteApprovalFormById(ApprovalFormIdCommand cmd) {
		// 删除是状态置为invalid
		this.generalFormProvider.invalidForms(cmd.getFormOriginId());
	}

	@Override
	public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralApprovalDTO deleteGeneralApproval(GeneralApprovalIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
