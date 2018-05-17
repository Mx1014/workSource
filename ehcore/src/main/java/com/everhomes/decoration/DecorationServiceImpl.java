package com.everhomes.decoration;

import com.everhomes.rest.decoration.DecorationIllustrationDTO;
import com.everhomes.rest.decoration.GetIlluStrationCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DecorationServiceImpl implements  DecorationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationServiceImpl.class);

    @Autowired
    private DecorationProvider decorationProvider;
    @Override
    public DecorationIllustrationDTO getIllustration(GetIlluStrationCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        DecorationSetting setting =  decorationProvider.getDecorationSetting(namespaceId,cmd.getCommunityId(),
                cmd.getOwnerType(),cmd.getOwnerId());

        DecorationIllustrationDTO dto = ConvertHelper.convert(setting,DecorationIllustrationDTO.class);
        return dto;
    }
}
