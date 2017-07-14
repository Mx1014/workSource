// @formatter:off
package com.everhomes.techpark.expansion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.talent.TalentRequest;
import com.everhomes.talent.TalentRequestProvider;
import com.everhomes.util.Tuple;

@Component
public class TalentApplyEntryFlowListener implements FlowModuleListener {

    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private TalentRequestProvider talentRequestProvider;

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return flowCase.getContent();
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
    	TalentRequest talentRequest = talentRequestProvider.findTalentRequestById(flowCase.getReferId());
    	GetGeneralFormValuesCommand cmd2 = new GetGeneralFormValuesCommand();
        cmd2.setSourceType(EntityType.TALENT_REQUEST.getCode());
        cmd2.setSourceId(talentRequest.getId());
        List<FlowCaseEntity> formEntities = generalFormService.getGeneralFormFlowEntities(cmd2, true);
        append(formEntities, talentRequest.getContent());
    	return formEntities;
    }

    private void append(List<FlowCaseEntity> formEntities, String content) {
    	String[] contentSplit = content.split("\n");
    	String name = contentSplit[0];
    	String[] nameSplit = name.split("：");
    	String position = contentSplit[1];
    	String[] positionSplit = position.split("：");
    	
    	formEntities.add(new FlowCaseEntity(nameSplit[0], nameSplit[1], "list"));
    	formEntities.add(new FlowCaseEntity(positionSplit[0], positionSplit[1], "list"));
	}

	@Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        moduleInfo.setModuleId(FlowConstants.TALENT_REQUEST);
        moduleInfo.setModuleName("企业人才");
        return moduleInfo;
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
	}
}
