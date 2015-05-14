package com.everhomes.search;

import java.util.List;

import com.everhomes.group.Group;

public interface GroupSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Group> groups);
    void feedDoc(Group group);
    void syncFromDb();
    List<Long> query(GroupQueryFilter filter);
}
