package com.everhomes.scriptengine;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.gogs.GogsRepo;
import com.everhomes.gogs.GogsService;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.scriptengine.nashorn.NashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.util.Assert;

public abstract class GogsNashornScript<O> implements NashornScript<O> {

    private GogsService gogsService;

    public GogsNashornScript() {
        gogsService = PlatformContext.getComponent(GogsService.class);
    }

    @Override
    public String getScript() {
        GogsRepo repo = getGogsRepo();
        String path = getScriptPath();
        String lastCommit = getLastCommit();

        Assert.notNull(repo, "gogs repo should be not null");
        Assert.notNull(path, "gogs script path should be not null");
        Assert.notNull(lastCommit, "gogs script last commit should be not null");

        byte[] file = gogsService.getFile(repo, path, lastCommit);
        return new String(file);
    }

    @Override
    public O process(NashornEngineService input) {
        GogsRepo repo = getGogsRepo();
        String path = getScriptPath();
        String lastCommit = getLastCommit();

        Assert.notNull(repo, "gogs repo should be not null");
        Assert.notNull(path, "gogs script path should be not null");
        Assert.notNull(lastCommit, "gogs script last commit should be not null");

        String key = String.format("%s:%s:%s", repo.getFullName(), path, lastCommit);
        ScriptObjectMirror objectMirror = input.getScriptObjectMirror(key, this);
        return processInternal(objectMirror);
    }

    /**
     * 具体的执行脚本的逻辑写在这里
     * @param objectMirror  脚本编译后的对象
     * @return  返回值
     */
    abstract protected O processInternal(ScriptObjectMirror objectMirror);

    /**
     * @return gogs 仓库
     */
    abstract protected GogsRepo getGogsRepo();

    /**
     * @return 需要执行的脚本在 gogs 仓库中的路径
     */
    abstract protected String getScriptPath();

    /**
     * @return 需要执行的脚本在 gogs 里的最后一次提交的 id
     */
    abstract protected String getLastCommit();
}