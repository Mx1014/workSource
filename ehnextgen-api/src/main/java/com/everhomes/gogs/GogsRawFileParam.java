package com.everhomes.gogs;

import com.everhomes.util.StringHelper;

public class GogsRawFileParam {

    private String lastCommit;
    private boolean isNewFile;
    private String content;
    private String commitSummary;
    private String commitMessage;
    private String commitChoice;
    private String treePath;

    public GogsRawFileParam() {
        this.commitChoice = "direct"; // default value
        this.isNewFile = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommitSummary() {
        return commitSummary;
    }

    public void setCommitSummary(String commitSummary) {
        this.commitSummary = commitSummary;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommitChoice() {
        return commitChoice;
    }

    public void setCommitChoice(String commitChoice) {
        this.commitChoice = commitChoice;
    }

    public String getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(String lastCommit) {
        this.lastCommit = lastCommit;
    }

    public boolean isNewFile() {
        return isNewFile;
    }

    public void setNewFile(boolean newFile) {
        isNewFile = newFile;
    }

    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
