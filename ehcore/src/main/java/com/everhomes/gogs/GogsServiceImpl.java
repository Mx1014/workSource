package com.everhomes.gogs;

import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GogsServiceImpl implements GogsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GogsServiceImpl.class);

    @Autowired
    private GogsProvider gogsProvider;

    @Autowired
    private GogsRepoProvider gogsRepoProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public GogsRepo createRepo(GogsRepo repo) {
        dbProvider.execute(status -> {
            String fullName = String.format(
                    "%s-%s-%s-%s-%s-%s",
                    repo.getNamespaceId(), repo.getModuleType(), repo.getModuleId(),
                    repo.getOwnerType(), repo.getOwnerId(), repo.getName());

            repo.setFullName(fullName);
            gogsRepoProvider.createRepo(repo);

            gogsProvider.createRepo(fullName, repo.getDescription(), GogsRawRepo.class);
            return true;
        });
        return repo;
    }

    @Override
    public List<GogsRepo> listRepos(int pageSize, ListingLocator locator, ListingQueryBuilderCallback callback) {
        return gogsRepoProvider.listRepos(pageSize, locator, callback);
    }

    @Override
    public List<GogsObject> listObjects(GogsRepo repo, String path, String lastCommit) {
        check(repo, path);
        GogsRawFileParam param = new GogsRawFileParam();
        param.setLastCommit(lastCommit);
        return gogsProvider.listObjects(repo.getFullName(), path, param, GogsObject.class);
    }

    @Override
    public GogsRepo getAnyRepo(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId) {
        return gogsRepoProvider.getAnyRepo(namespaceId, moduleType, moduleId, ownerType, ownerId);
    }

    @Override
    public byte[] getFile(GogsRepo repo, String path, String lastCommit) throws GogsFileNotExistException {
        check(repo, path);
        GogsRawFileParam param = new GogsRawFileParam();
        param.setLastCommit(lastCommit);
        return gogsProvider.getFile(repo.getFullName(), path, param);
    }

    @Override
    public GogsCommit commitFile(GogsRepo repo, String path, GogsRawFileParam param) throws GogsConflictException {
        check(repo, path);
        return gogsProvider.commitFile(repo.getFullName(), path, param);
    }

    @Override
    public GogsCommit deleteFile(GogsRepo repo, String path, GogsRawFileParam param) throws GogsConflictException {
        check(repo, path);
        return gogsProvider.deleteFile(repo.getFullName(), path, param);
    }

    private void check(GogsRepo repo, String path) {
        if (repo == null || repo.getFullName() == null || path == null) {
            LOGGER.error("Invalid gogs repo or path, repo={}, path={}", repo, path);
            throw RuntimeErrorException.errorWith(GogsServiceErrorCode.SCOPE, GogsServiceErrorCode.ERROR_REPO_NOT_EXIST,
                    "Invalid gogs repo or path");
        }
    }
}