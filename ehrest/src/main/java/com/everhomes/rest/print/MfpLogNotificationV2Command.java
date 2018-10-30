// @formatter:off
package com.everhomes.rest.print;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * </ul>
 */
public class MfpLogNotificationV2Command {
    private Integer namespaceId;
    private String ownerType = "community";
    private Long ownerId;
    private String phone;

    private String color_surface = "0";
    private String duplex = "1";
    private String job_id;
    private String job_name;
    private String job_type = "PRINT";
    private String location;
    private String mono_surface="1";
    private String paper_size="A4";
    private String print_time;
    private String serial_number="TC101154727022";
    private String user_id="12323";
    private String user_name;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getColor_surface() {
        return color_surface;
    }

    public void setColor_surface(String color_surface) {
        this.color_surface = color_surface;
    }

    public String getDuplex() {
        return duplex;
    }

    public void setDuplex(String duplex) {
        this.duplex = duplex;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMono_surface() {
        return mono_surface;
    }

    public void setMono_surface(String mono_surface) {
        this.mono_surface = mono_surface;
    }

    public String getPaper_size() {
        return paper_size;
    }

    public void setPaper_size(String paper_size) {
        this.paper_size = paper_size;
    }

    public String getPrint_time() {
        return print_time;
    }

    public void setPrint_time(String print_time) {
        this.print_time = print_time;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
