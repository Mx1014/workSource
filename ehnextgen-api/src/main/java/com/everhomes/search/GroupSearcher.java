package com.everhomes.search;

import java.util.List;

import com.everhomes.group.Group;
//import com.everhomes.group.GroupDTO;
import com.everhomes.rest.group.SearchGroupCommand;
import com.everhomes.rest.search.GroupQueryResult;

public interface GroupSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Group> groups);
    void feedDoc(Group group);
    void syncFromDb();
    GroupQueryResult query(QueryMaker filter);
    GroupQueryResult query(SearchGroupCommand cmd);
}
