package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>detailId: 通讯录人员Id</li>
 * </ul>
 */
public class ArchivesIdCommand {

    private Long detailId;

    public ArchivesIdCommand() {
    }

    public ArchivesIdCommand(Long detailId){
        this.detailId = detailId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
}
