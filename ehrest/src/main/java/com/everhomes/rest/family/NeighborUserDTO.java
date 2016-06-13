// @formatter:off
package com.everhomes.rest.family;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 邻居用户Id</li>
 * <li>userName: 邻居用户名称</li>
 * <li>userAvatarUri: 邻居用户头像Id</li>
 * <li>userAvatarUrl: 邻居用户头像url</li>
 * <li>userStatusLine: 个性签名</li>
 * <li>neighborhoodRelation: 邻居关系参考，{@link com.everhomes.rest.family.NeighborhoodRelation}</li>
 * <li>distance: 邻居用户与自己的距离</li>
 * <li>occupation: 邻居用户职业</li>
 * <li>fullPinyin: 名字全拼</li>
 * <li>fullInitial: 名字全首字母</li>
 * <li>initial: 首字母</li>
 * </ul>
 */
public class NeighborUserDTO implements Comparable<NeighborUserDTO>{
    private Long userId;
    private String userName;
    private String userAvatarUri;
    private String userAvatarUrl;
    private String userStatusLine;
    private Byte neighborhoodRelation;
    private Double distance;
    private String occupation;
    private String initial;
    private String fullPinyin;
    private String fullInitial;

    public NeighborUserDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUri() {
        return userAvatarUri;
    }

    public void setUserAvatarUri(String userAvatarUri) {
        this.userAvatarUri = userAvatarUri;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserStatusLine() {
        return userStatusLine;
    }

    public void setUserStatusLine(String userStatusLine) {
        this.userStatusLine = userStatusLine;
    }

    public Byte getNeighborhoodRelation() {
        return neighborhoodRelation;
    }

    public void setNeighborhoodRelation(Byte neighborhoodRelation) {
        this.neighborhoodRelation = neighborhoodRelation;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    
    
    public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	
	public String getFullPinyin() {
		return fullPinyin;
	}

	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	public String getFullInitial() {
		return fullInitial;
	}

	public void setFullInitial(String fullInitial) {
		this.fullInitial = fullInitial;
	}

	public int compareTo(NeighborUserDTO neighborUserDTO) {
	    return this.initial.compareTo(neighborUserDTO.getInitial());
	}
	
	@Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
