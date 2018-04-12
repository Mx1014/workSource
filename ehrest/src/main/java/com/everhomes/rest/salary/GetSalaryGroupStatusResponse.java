package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>creatorName: 创建人</li>
 * <li>createTime: 创建时间</li>
 * <li>filerName: 归档人</li>
 * <li>fileTime: 归档时间</li>
 * <li>isFile: 是否归档</li>
 * <li>month: 月份</li>
 * <li>exportExcels: 导出表列表 {@link ExportExcelDTO}</li>
 * </ul>
 */
public class GetSalaryGroupStatusResponse {
    private String creatorName;
    private Long createTime;
    private String filerName;
    private Long fileTime;
    private Byte isFile;
    private String month;
    @ItemType(ExportExcelDTO.class)
    private List<ExportExcelDTO> exportExcels;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<ExportExcelDTO> getExportExcels() {
        return exportExcels;
    }

    public void setExportExcels(List<ExportExcelDTO> exportExcels) {
        this.exportExcels = exportExcels;
    }

    public String getFilerName() {
        return filerName;
    }

    public void setFilerName(String filerName) {
        this.filerName = filerName;
    }

    public Long getFileTime() {
        return fileTime;
    }

    public void setFileTime(Long fileTime) {
        this.fileTime = fileTime;
    }

    public Byte getIsFile() {
        return isFile;
    }

    public void setIsFile(Byte isFile) {
        this.isFile = isFile;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
