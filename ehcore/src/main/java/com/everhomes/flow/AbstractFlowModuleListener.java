package com.everhomes.flow;

import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.flow.FlowFormDTO;
import com.everhomes.server.schema.Tables;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq.tian on 2017/11/17.
 */
public abstract class AbstractFlowModuleListener implements FlowModuleListener {

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Override
    public List<FlowFormDTO> listFlowForms(Flow flow) {
        List<FlowFormDTO> flowFormDTOS = new ArrayList<>();
        List<GeneralForm> generalForms = generalFormProvider.queryGeneralForms(new ListingLocator(), 1000, (locator, query) -> {
            query.addConditions(Tables.EH_GENERAL_FORMS.NAMESPACE_ID.eq(flow.getNamespaceId()));
            query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_TYPE.eq(flow.getModuleType()));
            query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(flow.getModuleId()));
            return query;
        });

        if (generalForms != null) {
            for (GeneralForm generalForm : generalForms) {
                FlowFormDTO dto = new FlowFormDTO();
                dto.setFormOriginId(generalForm.getFormOriginId());
                dto.setFormVersion(generalForm.getFormVersion());
                dto.setName(generalForm.getFormName());

                flowFormDTOS.add(dto);
            }
        }
        return flowFormDTOS;
    }
}
