// @formatter:off
package com.everhomes.parking.guangdawegu;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/1/30 19:02
 */
public class GuangDaWeGuData {
    private String carnum;
    private Integer carattr;
    private String username;
    private Long begin1;
    private Long end1;
    private Integer expired1;
    private Long begin2;
    private Long end2;
    private Integer expired2;

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public Integer getCarattr() {
        return carattr;
    }

    public void setCarattr(Integer carattr) {
        this.carattr = carattr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getBegin1() {
        return begin1;
    }

    public void setBegin1(Long begin1) {
        this.begin1 = begin1;
    }

    public Long getEnd1() {
        return end1;
    }

    public void setEnd1(Long end1) {
        this.end1 = end1;
    }

    public Integer getExpired1() {
        return expired1;
    }

    public void setExpired1(Integer expired1) {
        this.expired1 = expired1;
    }

    public Long getBegin2() {
        return begin2;
    }

    public void setBegin2(Long begin2) {
        this.begin2 = begin2;
    }

    public Long getEnd2() {
        return end2;
    }

    public void setEnd2(Long end2) {
        this.end2 = end2;
    }

    public Integer getExpired2() {
        return expired2;
    }

    public void setExpired2(Integer expired2) {
        this.expired2 = expired2;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
