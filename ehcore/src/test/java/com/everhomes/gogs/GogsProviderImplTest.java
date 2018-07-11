package com.everhomes.gogs;

import com.everhomes.util.RuntimeErrorException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

public class GogsProviderImplTest {

    private GogsProviderImpl gogs;

    {
        gogs = new GogsProviderImpl();
        gogs.init();
    }

    @Test
    public void createRepository() {
        String reponame = "repo-"+System.nanoTime();
        CreateGogsRepoParam param = new CreateGogsRepoParam();
        param.setReadme("Default");
        param.setDescription("desc");
        param.setPrivateFlag(true);
        param.setName(reponame);
        param.setAutoInit(true);

        GogsRawRepo repository = gogs.createRepo(param, GogsRawRepo.class);
        assertNotNull(repository);
    }

    @Test
    public void listContents() {
        String repoName = "repo-"+System.nanoTime();
        CreateGogsRepoParam repoparam = new CreateGogsRepoParam();
        repoparam.setReadme("Default");
        repoparam.setDescription("desc");
        repoparam.setPrivateFlag(true);
        repoparam.setName(repoName);
        repoparam.setAutoInit(true);

        GogsRawRepo repository = gogs.createRepo(repoparam, GogsRawRepo.class);
        assertNotNull(repository);

        String content = "content 1";
        String path0 = System.currentTimeMillis() + ".txt";
        String path1 = System.nanoTime() + ".md";

        String commitMessage = "first commit:" + System.nanoTime();

        GogsRawFileParam param = new GogsRawFileParam();
        param.setContent(content);
        param.setCommitMessage(commitMessage);
        param.setCommitSummary("User: xq.tian, uid: 1");
        param.setLastCommit("");
        param.setNewFile(true);
        param.setTreePath(path0);
        gogs.commitFile(repoName, path0, param);
        gogs.commitFile(repoName, path1, param);

        List<GogsObject> objects = gogs.listObjects(repoName, "", null, new TypeToken<List<GogsObject>>(){}.getType());
        assertNotNull(objects);

        for (GogsObject object : objects) {
            assertTrue(
                    object.getName().equalsIgnoreCase(path0)
                    || object.getName().equalsIgnoreCase(path1)
                    || object.getName().equalsIgnoreCase("README.md")
            );
        }
    }

    @Test
    public void listCommits() {
        String repoName = "repo-"+System.nanoTime();
        CreateGogsRepoParam repoparam = new CreateGogsRepoParam();
        repoparam.setReadme("Default");
        repoparam.setDescription("desc");
        repoparam.setPrivateFlag(true);
        repoparam.setName(repoName);
        repoparam.setAutoInit(true);

        GogsRawRepo repository = gogs.createRepo(repoparam, GogsRawRepo.class);
        assertNotNull(repository);
        String content = "content 1";
        String path = System.currentTimeMillis() + ".txt";
        String commitMessage = "first commit:" + System.nanoTime();

        GogsRawFileParam param = new GogsRawFileParam();
        param.setContent(content);
        param.setCommitMessage(commitMessage);
        param.setCommitSummary("User: xq.tian, uid: 1");
        param.setLastCommit("");
        param.setNewFile(true);
        param.setTreePath(path);
        gogs.commitFile(repoName, path, param);

        List<GogsCommit> commits = gogs.listCommits(repoName, path, new TypeToken<List<GogsCommit>>() {
        }.getType());

        assertNotNull(commits);
        assertTrue(commits.size() > 0);
        assertEquals(commits.iterator().next().getCommitMessage().trim(), commitMessage);
    }

    @Test
    public void commitFile() {
        String repoName = "repo-"+System.nanoTime();
        CreateGogsRepoParam repoparam = new CreateGogsRepoParam();
        repoparam.setReadme("Default");
        repoparam.setDescription("desc");
        repoparam.setPrivateFlag(true);
        repoparam.setName(repoName);
        repoparam.setAutoInit(true);

        GogsRawRepo repository = gogs.createRepo(repoparam, GogsRawRepo.class);
        assertNotNull(repository);

        String content = "aaadasdasdasdas:"+System.nanoTime();
        String path = System.nanoTime() + ".txt";

        GogsRawFileParam param = new GogsRawFileParam();
        param.setContent(content);
        param.setCommitMessage("first commit");
        param.setCommitSummary("User: xq.tian, uid: 1");
        param.setLastCommit("");
        param.setNewFile(true);
        param.setTreePath("a.txt");
        GogsCommit commit = gogs.commitFile(repoName, path, param);
        assertNotNull(commit);

        byte[] file = gogs.getFile(repoName, path, param);
        assertEquals(new String(file), content);
    }

    @Test
    public void deleteFile() {
        String repoName = "repo-"+System.nanoTime();
        CreateGogsRepoParam repoparam = new CreateGogsRepoParam();
        repoparam.setReadme("Default");
        repoparam.setDescription("desc");
        repoparam.setPrivateFlag(true);
        repoparam.setName(repoName);
        repoparam.setAutoInit(true);

        GogsRawRepo repository = gogs.createRepo(repoparam, GogsRawRepo.class);
        assertNotNull(repository);

        String content = "content 1";
        String path = System.currentTimeMillis() + ".txt";
        String commitMessage = "first commit:" + System.nanoTime();

        GogsRawFileParam param = new GogsRawFileParam();
        param.setContent(content);
        param.setCommitMessage(commitMessage);
        param.setCommitSummary("User: xq.tian, uid: 1");
        param.setLastCommit("");
        param.setNewFile(true);
        param.setTreePath(path);
        gogs.commitFile(repoName, path, param);
        List<GogsCommit> data = gogs.listCommits(repoName, path, new TypeToken<List<GogsCommit>>(){}.getType());
        assertNotNull(data);
        assertTrue(data.size() > 0);

        param = new GogsRawFileParam();
        param.setLastCommit(data.iterator().next().getId());
        param.setCommitMessage("delete file");
        gogs.deleteFile(repoName, path, param);

        try {
            gogs.getFile(repoName, path, param);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeErrorException);
            assertEquals("file not exist", e.getMessage());
        }
    }

    @Test
    public void getFile() {

    }

    @Test
    public void downloadArchive() {
        String repoName = "repo-"+System.nanoTime();
        CreateGogsRepoParam repoparam = new CreateGogsRepoParam();
        repoparam.setReadme("Default");
        repoparam.setDescription("desc");
        repoparam.setPrivateFlag(true);
        repoparam.setName(repoName);
        repoparam.setAutoInit(true);

        GogsRawRepo repository = gogs.createRepo(repoparam, GogsRawRepo.class);
        assertNotNull(repository);

        String content = "aaadasdasdasdas:"+System.nanoTime();
        String path = System.nanoTime() + ".txt";

        GogsRawFileParam param = new GogsRawFileParam();
        param.setContent(content);
        param.setCommitMessage("first commit");
        param.setCommitSummary("User: xq.tian, uid: 1");
        param.setLastCommit("");
        param.setNewFile(true);
        param.setTreePath(path);
        gogs.commitFile(repoName, path, param);

        byte[] archive = gogs.downloadArchive(repoName);
        assertNotNull(archive);

        ZipInputStream stream = new ZipInputStream(new ByteArrayInputStream(archive));

        try {
            IOUtils.write(archive, new FileOutputStream("/home/xx/Desktop/"+repoName+".zip"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ZipEntry nextEntry = null;
        try {
            nextEntry = stream.getNextEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = nextEntry.getName();
        assert name.equalsIgnoreCase(repoName+"/");// is directory
    }
}