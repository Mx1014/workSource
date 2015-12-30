package com.everhomes.rest.point;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>uid:用户ID</li>
 *         <li>userPoints：{@link UserScoreDTO}</li>
 *         <li>nextPageAnchor:下一页的锚点</li>
 *         </ul>
 */
public class GetUserPointResponse {
    private Long uid;
    
    @ItemType(UserScoreDTO.class)
    private List<UserScoreDTO> userPoints;
    
    private Long nextPageAnchor;

    public GetUserPointResponse(Long uid, List<UserScoreDTO> userPoints, Long nextPageAnchor) {
        super();
        this.uid = uid;
        this.userPoints = userPoints;
        this.nextPageAnchor = nextPageAnchor;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<UserScoreDTO> getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(List<UserScoreDTO> userPoints) {
        this.userPoints = userPoints;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

}
