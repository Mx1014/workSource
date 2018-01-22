package com.everhomes.message;

import com.everhomes.organization.Organization;

import java.util.List;

public interface MessageProvider {
    void createMessage(Message message);
    void updateMessage(Message message);
    void deleteMessageById(Long id);
    List<Organization> listMessage(Integer namespaceId);

}
