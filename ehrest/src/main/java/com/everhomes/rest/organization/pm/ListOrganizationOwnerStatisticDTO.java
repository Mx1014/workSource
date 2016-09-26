package com.everhomes.rest.organization.pm;

/**
 *  <ul>
 *      <li>first: 第一个</li>
 *      <li>second: 第二个</li>
 *      <li>third: 第三个</li>
 *  </ul>
 */
public class ListOrganizationOwnerStatisticDTO {

    private String first;
    private Integer second;
    private String third;

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getFirst() {

        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
