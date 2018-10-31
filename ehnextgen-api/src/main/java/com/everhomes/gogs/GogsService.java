package com.everhomes.gogs;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface GogsService {

    GogsRepo createRepo(GogsRepo repo);

    List<GogsRepo> listRepos(int pageSize, ListingLocator locator, ListingQueryBuilderCallback callback);

    List<GogsObject> listObjects(GogsRepo repo, String path, String lastCommit);

    List<GogsCommit> listCommits(GogsRepo repo, String path, Integer page, Integer size);

    GogsRepo getAnyRepo(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId);

    byte[] getFile(GogsRepo repo, String path, String lastCommit) throws GogsNotExistException;

    GogsCommit commitFile(GogsRepo repo, String path, GogsRawFileParam param) throws GogsConflictException;

    GogsCommit deleteFile(GogsRepo repo, String path, GogsRawFileParam param) throws GogsConflictException;
}
