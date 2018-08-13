package com.everhomes.gogs;

import java.lang.reflect.Type;
import java.util.List;

public interface GogsProvider {

    <T> T createRepo(CreateGogsRepoParam param, Class<T> type);

    <T> List<T> listObjects(String repoName, String path, GogsRawFileParam param, Type type);

    <T> List<T> listCommits(String repoName, String path, GogsPaginationParam param, Type type);

    GogsCommit commitFile(String repoName, String path, GogsRawFileParam param);

    GogsCommit deleteFile(String repoName, String path, GogsRawFileParam param);

    byte[] getFile(String repoName, String path, GogsRawFileParam param);

    byte[] downloadArchive(String repoName);

    // POST /repos/:username/:reponame/hooks
    <T> T createHook(String repoName, CreateGogsHookParam param, Class<T> type);
}
