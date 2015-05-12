package com.everhomes.search;

import java.util.List;

import com.everhomes.group.GroupDTO;

public interface GroupSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<GroupDTO> groups);
    void feedDoc(GroupDTO group);
}
