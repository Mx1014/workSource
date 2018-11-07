package com.everhomes.pmtask.archibus;

import com.everhomes.util.StringHelper;

public class ArchibusEvaluation {
    private String pk_details_id;
    private String fk_details_id;
    private String yjq_remarks;
    private Integer yjq_evaluate;
    private Integer yjq_level;
    private Integer yjq_level2;
    private Integer yjq_level3;
    private Integer yjq_level4;
    private String question_source;
    private String details_number;
    private String detailed_address;
    private String details_content;
    private String fk_project_id;
    private String create_time;
    private String update_time;
    private String create_user_id;
    private String update_user_id;
    private String customer_name;
    private String customer_number;
    private Integer state;
    private String pk_crop;
    private Integer yjq_cancel;
    private Integer whether_repair;

    public String getPk_details_id() {
        return pk_details_id;
    }

    public void setPk_details_id(String pk_details_id) {
        this.pk_details_id = pk_details_id;
    }

    public String getFk_details_id() {
        return fk_details_id;
    }

    public void setFk_details_id(String fk_details_id) {
        this.fk_details_id = fk_details_id;
    }

    public String getYjq_remarks() {
        return yjq_remarks;
    }

    public void setYjq_remarks(String yjq_remarks) {
        this.yjq_remarks = yjq_remarks;
    }

    public Integer getYjq_evaluate() {
        return yjq_evaluate;
    }

    public void setYjq_evaluate(Integer yjq_evaluate) {
        this.yjq_evaluate = yjq_evaluate;
    }

    public Integer getYjq_level() {
        return yjq_level;
    }

    public void setYjq_level(Integer yjq_level) {
        this.yjq_level = yjq_level;
    }

    public Integer getYjq_level2() {
        return yjq_level2;
    }

    public void setYjq_level2(Integer yjq_level2) {
        this.yjq_level2 = yjq_level2;
    }

    public Integer getYjq_level3() {
        return yjq_level3;
    }

    public void setYjq_level3(Integer yjq_level3) {
        this.yjq_level3 = yjq_level3;
    }

    public Integer getYjq_level4() {
        return yjq_level4;
    }

    public void setYjq_level4(Integer yjq_level4) {
        this.yjq_level4 = yjq_level4;
    }

    public String getQuestion_source() {
        return question_source;
    }

    public void setQuestion_source(String question_source) {
        this.question_source = question_source;
    }

    public String getDetails_number() {
        return details_number;
    }

    public void setDetails_number(String details_number) {
        this.details_number = details_number;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public String getDetails_content() {
        return details_content;
    }

    public void setDetails_content(String details_content) {
        this.details_content = details_content;
    }

    public String getFk_project_id() {
        return fk_project_id;
    }

    public void setFk_project_id(String fk_project_id) {
        this.fk_project_id = fk_project_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(String update_user_id) {
        this.update_user_id = update_user_id;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPk_crop() {
        return pk_crop;
    }

    public void setPk_crop(String pk_crop) {
        this.pk_crop = pk_crop;
    }

    public Integer getYjq_cancel() {
        return yjq_cancel;
    }

    public void setYjq_cancel(Integer yjq_cancel) {
        this.yjq_cancel = yjq_cancel;
    }

    public Integer getWhether_repair() {
        return whether_repair;
    }

    public void setWhether_repair(Integer whether_repair) {
        this.whether_repair = whether_repair;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
