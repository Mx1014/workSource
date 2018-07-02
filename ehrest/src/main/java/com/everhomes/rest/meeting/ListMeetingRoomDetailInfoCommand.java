package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>queryDate: 查询日期的时间戳，不包含时分秒，必填</li>
 * <li>pageOffset: 页码，默认为1</li>
 * <li>pageSize: 每页返回记录数</li>
 * </ul>
 */
public class ListMeetingRoomDetailInfoCommand {
    private Long organizationId;
    private Long queryDate;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Long queryDate) {
        this.queryDate = queryDate;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public static void main(String[] args) throws Exception{
        String a="a\r\nb";
        File file=new File("D:\\aaa.txt");
        try(OutputStream out=new FileOutputStream(file)){
            out.write(a.getBytes());
        }
    }
}
