package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusTask {
    private String pk_details_id;
    private String details_number;
    private String fk_project_id;
    private String fk_project_name;
    private String details_content;
    private String customer_name;
    private String customer_number;
    private String detailed_address;
    private Integer state;
    private String stateName;
    private String create_time;
    private String create_user_id;
    private String worktask_duty_user_id;
    private String create_user_name;
    private String repair_user_id;
    private String worktask_duty_user_name;
    private String worktask_duty_user_phone;
    private String repair_user_name;
    private String repair_user_phone;
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

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getWorktask_duty_user_id() {
        return worktask_duty_user_id;
    }

    public void setWorktask_duty_user_id(String worktask_duty_user_id) {
        this.worktask_duty_user_id = worktask_duty_user_id;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getRepair_user_id() {
        return repair_user_id;
    }

    public void setRepair_user_id(String repair_user_id) {
        this.repair_user_id = repair_user_id;
    }

    public String getWorktask_duty_user_name() {
        return worktask_duty_user_name;
    }

    public void setWorktask_duty_user_name(String worktask_duty_user_name) {
        this.worktask_duty_user_name = worktask_duty_user_name;
    }

    public String getWorktask_duty_user_phone() {
        return worktask_duty_user_phone;
    }

    public void setWorktask_duty_user_phone(String worktask_duty_user_phone) {
        this.worktask_duty_user_phone = worktask_duty_user_phone;
    }

    public String getRepair_user_name() {
        return repair_user_name;
    }

    public void setRepair_user_name(String repair_user_name) {
        this.repair_user_name = repair_user_name;
    }

    public String getRepair_user_phone() {
        return repair_user_phone;
    }

    public void setRepair_user_phone(String repair_user_phone) {
        this.repair_user_phone = repair_user_phone;
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
