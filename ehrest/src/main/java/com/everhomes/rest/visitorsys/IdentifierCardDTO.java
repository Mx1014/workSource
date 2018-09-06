package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.util.Date;

public class IdentifierCardDTO {
    private String name;
    private String people;
    private Date birthday;
    private String address;
    private String number;
    private String dept;
    private Date validtermOfStart;
    private Date validtermOfEnd;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Date getValidtermOfStart() {
        return validtermOfStart;
    }

    public void setValidtermOfStart(Date validtermOfStart) {
        this.validtermOfStart = validtermOfStart;
    }

    public Date getValidtermOfEnd() {
        return validtermOfEnd;
    }

    public void setValidtermOfEnd(Date validtermOfEnd) {
        this.validtermOfEnd = validtermOfEnd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
