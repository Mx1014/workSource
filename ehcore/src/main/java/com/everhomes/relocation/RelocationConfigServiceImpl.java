package com.everhomes.relocation;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.relocation.GetRelocationConfigCommand;
import com.everhomes.rest.relocation.RelocationConfigDTO;
import com.everhomes.rest.relocation.SetRelocationConfigCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelocationConfigServiceImpl implements RelocationConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelocationConfigServiceImpl.class);

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
        if(null == cmd.getId() && (null == cmd.getNamespaceId() && null == cmd.getOwnerId())){
            LOGGER.error("Invalid parameter, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter.");
        }
        RelocationConfig result = this.relocationConfigProvider.searchRelocationConfigById(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getId());
        return ConvertHelper.convert(result,RelocationConfigDTO.class);
    }
}
