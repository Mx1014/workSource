package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>nextPageAnchor:下一页锚点</li>
 *     <li>dtos:门禁日志列表 {@link com.everhomes.rest.aclink.AclinkLogDTO}</li>
 *     <li>若用户无权限，抛出异常"errorCode":100055,"errorDescription":"校验应用权限失败"</li>
 * </ul>
 */
public class AclinkQueryLogResponse {
    private Long nextPageAnchor;
    
    @ItemType(AclinkLogDTO.class)
    private List<AclinkLogDTO> dtos;
    
    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }
    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    public List<AclinkLogDTO> getDtos() {
        return dtos;
    }
    public void setDtos(List<AclinkLogDTO> dtos) {
        this.dtos = dtos;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
