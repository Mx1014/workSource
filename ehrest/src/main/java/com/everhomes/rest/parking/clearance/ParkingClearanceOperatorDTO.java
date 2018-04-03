// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 名字</li>
 *     <li>department: 部门</li>
 *     <li>identifierToken: 手机</li>
 * </ul>
 */
public class ParkingClearanceOperatorDTO {

    private Long id;
    private String name;
    private String department;
    private String identifierToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
