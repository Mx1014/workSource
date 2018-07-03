package com.everhomes.gogs;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface GogsRepoProvider {

    void createRepo(GogsRepo repo);

    List<GogsRepo> listRepos(int pageSize, ListingLocator locator, ListingQueryBuilderCallback callback);

    GogsRepo findById(Long repoId);

    GogsRepo getAnyRepo(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId);
}
