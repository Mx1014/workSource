package com.everhomes.relocation;

import com.everhomes.rest.relocation.GetRelocationConfigCommand;
import com.everhomes.rest.relocation.RelocationConfigDTO;
import com.everhomes.rest.relocation.SetRelocationConfigCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelocationConfigServiceImpl implements RelocationConfigService {

    @Autowired
    RelocationConfigProvider relocationConfigProvider;

    @Override
    public RelocationConfigDTO setRelocationConfig(SetRelocationConfigCommand cmd) {
        User user = UserContext.current().getUser();
        RelocationConfig result = relocationConfigProvider.searchRelocationConfigById(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getId());
        if(null != result){
            if(null != cmd.getAgreementFlag())
                result.setAgreementFlag(cmd.getAgreementFlag());
            if(StringUtils.isNotEmpty(cmd.getAgreementContent()))
                result.setAgreementContent(cmd.getAgreementContent());
            if(null != cmd.getTipsFlag())
                result.setTipsFlag(cmd.getTipsFlag());
            if(StringUtils.isNotEmpty(cmd.getTipsContent())){
                result.setTipsContent(cmd.getTipsContent());
            }
            result.setOperatorUid(user.getId());
            relocationConfigProvider.updateRelocationConfig(result);
        } else {
            result = ConvertHelper.convert(cmd, RelocationConfig.class);
            result.setCreatorUid(user.getId());
            relocationConfigProvider.createRelocationConfig(result);
        }
        return ConvertHelper.convert(result,RelocationConfigDTO.class);
    }

    @Override
    public RelocationConfigDTO searchRelocationConfigById(GetRelocationConfigCommand cmd) {
        RelocationConfig result = this.relocationConfigProvider.searchRelocationConfigById(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getId());
        return ConvertHelper.convert(result,RelocationConfigDTO.class);
    }
}
