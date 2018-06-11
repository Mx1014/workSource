// @formatter:off
package com.everhomes.welfare;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.welfare.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.jooq.tools.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.techpark.punch.PunchServiceImpl;
import com.google.zxing.Result;

@Component
public class WelfareServiceImpl implements WelfareService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WelfareServiceImpl.class);
    @Autowired
    private WelfareProvider welfareProvider;
    @Autowired
    private WelfareItemProvider welfareItemProvider;
    @Autowired
    private WelfareReceiverProvider welfareReceiverProvider;
    @Autowired
    private ContentServerService contentServerService;

    @Override
    public ListWelfaresResponse listWelfares(ListWelfaresCommand cmd) {
        ListWelfaresResponse response = new ListWelfaresResponse();
        List<Welfare> results = welfareProvider.listWelfare(cmd.getOwnerId());
        if (null == results) {
            return response;
        }
        response.setWelfares(results.stream().map(r -> processWelfaresDTO(r)).collect(Collectors.toList()));
        return response;
    }

    private WelfaresDTO processWelfaresDTO(Welfare r) {
        WelfaresDTO dto = ConvertHelper.convert(r, WelfaresDTO.class);
        dto.setUpdateTime(r.getUpdateTime().getTime());
        dto.setAttachmentImgUrl(contentServerService.parserUri(r.getAttachmentImgUri(),
                EntityType.USER.getCode(), UserContext.currentUserId()));
        dto.setItems(new ArrayList<>());
        dto.setReceivers(new ArrayList<>());
        List<WelfareItem> items = welfareItemProvider.listWelfareItem(r.getId());
        if (null != items) {
            for (WelfareItem item : items) {
                WelfareItemDTO itemDTO = ConvertHelper.convert(item, WelfareItemDTO.class);
                dto.getItems().add(itemDTO);
            }
        }
        List<WelfareReceiver> receivers = welfareReceiverProvider.listWelfareReceiver(r.getId());
        if (null != receivers) {
            for (WelfareReceiver receiver : receivers) {
                WelfareReceiverDTO reDTO = ConvertHelper.convert(receiver, WelfareReceiverDTO.class);
                dto.getReceivers().add(reDTO);
            }
        }
        return dto;
    }

    @Override
    public void draftWelfare(DraftWelfareCommand cmd) {
        cmd.getWelfare().setStatus(WelfareStatus.DRAFT.getCode());
        saveWelfare(cmd.getWelfare());
    }

    private void saveWelfare(WelfaresDTO welfareDTO) {
        Welfare welfare = ConvertHelper.convert(welfareDTO, Welfare.class);

    }

    @Override
    public void sendWelfare(SendWelfareCommand cmd) {
        cmd.getWelfare().setStatus(WelfareStatus.SENDED.getCode());
        saveWelfare(cmd.getWelfare());
        //发消息
    }

    @Override
    public GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd) {

        return new GetUserWelfareResponse();
    }

}