package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusTaskDetail {
    private String pk_details_id;
    private String details_number;
    private String fk_project_id;
    private String fk_project_name;
    private String details_content;
    private String customer_name;
    private String customer_number;
    private String fk_class_id;
    private String class_name;
    private String fk_house_address_id;
    private String detailed_address;
    private String fk_duty_user_id;
    private String duty_user_name;
    private Integer state;
    private String stateName;
    private Integer pay_status;
    private Integer pay_way;
    private Double pay_request_amount;
    private String create_user_id;
    private String repair_user_id;
    private String repair_phone;
    private String repair_user_name;
    private String duty_user_phone;
    private String create_user_name;
    private String yjq_evaluate;
    private String yjq_evaluate_name;

    public String getPk_details_id() {
        return pk_details_id;
    }

    public void setPk_details_id(String pk_details_id) {
        this.pk_details_id = pk_details_id;
    }

    public String getDetails_number() {
        return details_number;
    }

    public void setDetails_number(String details_number) {
        this.details_number = details_number;
    }

    public String getFk_project_id() {
        return fk_project_id;
    }

    public void setFk_project_id(String fk_project_id) {
        this.fk_project_id = fk_project_id;
    }

    public String getFk_project_name() {
        return fk_project_name;
    }

    public void setFk_project_name(String fk_project_name) {
        this.fk_project_name = fk_project_name;
    }

    public String getDetails_content() {
        return details_content;
    }

    public void setDetails_content(String details_content) {
        this.details_content = details_content;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getFk_class_id() {
        return fk_class_id;
    }

    public void setFk_class_id(String fk_class_id) {
        this.fk_class_id = fk_class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getFk_house_address_id() {
        return fk_house_address_id;
    }

    public void setFk_house_address_id(String fk_house_address_id) {
        this.fk_house_address_id = fk_house_address_id;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public String getFk_duty_user_id() {
        return fk_duty_user_id;
    }

    public void setFk_duty_user_id(String fk_duty_user_id) {
        this.fk_duty_user_id = fk_duty_user_id;
    }

    public String getDuty_user_name() {
        return duty_user_name;
    }

    public void setDuty_user_name(String duty_user_name) {
        this.duty_user_name = duty_user_name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getPay_status() {
        return pay_status;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public Integer getPay_way() {
        return pay_way;
    }

    public void setPay_way(Integer pay_way) {
        this.pay_way = pay_way;
    }

    public Double getPay_request_amount() {
        return pay_request_amount;
    }

    public void setPay_request_amount(Double pay_request_amount) {
        this.pay_request_amount = pay_request_amount;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getRepair_user_id() {
        return repair_user_id;
    }

    public void setRepair_user_id(String repair_user_id) {
        this.repair_user_id = repair_user_id;
    }

    public String getRepair_phone() {
        return repair_phone;
    }

    public void setRepair_phone(String repair_phone) {
        this.repair_phone = repair_phone;
    }

    public String getRepair_user_name() {
        return repair_user_name;
    }

    public void setRepair_user_name(String repair_user_name) {
        this.repair_user_name = repair_user_name;
    }

    public String getDuty_user_phone() {
        return duty_user_phone;
    }

    public void setDuty_user_phone(String duty_user_phone) {
        this.duty_user_phone = duty_user_phone;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getYjq_evaluate() {
        return yjq_evaluate;
    }

    public void setYjq_evaluate(String yjq_evaluate) {
        this.yjq_evaluate = yjq_evaluate;
    }

    public String getYjq_evaluate_name() {
        return yjq_evaluate_name;
    }

    public void setYjq_evaluate_name(String yjq_evaluate_name) {
        this.yjq_evaluate_name = yjq_evaluate_name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
