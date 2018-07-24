package com.everhomes.pmtask.zhuzong;

/**
 * <ul>
 * <li>pk_client: 业户ID</li>
 * <li>pk_project: 项目ID</li>
 * <li>pk_house: 房屋ID</li>
 * <li>client_name: 业户名称</li>
 * <li>house_name: 房屋名称</li>
 * </ul>
 */
public class ZhuzongAddress {
    private String pk_client;
    private String pk_project;
    private String pk_house;
    private String client_name;
    private String house_name;

    public String getPk_client() {
        return pk_client;
    }

    public void setPk_client(String pk_client) {
        this.pk_client = pk_client;
    }

    public String getPk_project() {
        return pk_project;
    }

    public void setPk_project(String pk_project) {
        this.pk_project = pk_project;
    }

    public String getPk_house() {
        return pk_house;
    }

    public void setPk_house(String pk_house) {
        this.pk_house = pk_house;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }
}
