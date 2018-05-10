// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>moduleId: 模块id</li>
 *     <li>contentId: 内容Id。此处为String，因为可能某些业务的内容id是String，例如新闻快讯</li>
 *     <li>contentOne: 一级内容</li>
 *     <li>contentTwo: 二级内容</li>
 *     <li>contentThree: 三级内容</li>
 *     <li>contentFour: 四级内容</li>
 *     <li>contentFive: 五级内容</li>
 *     <li>contentSix: 六级内容</li>
 *     <li>contentSeven: 七级内容，要多于七级内容的话，恕运营板块容不下，请另找高就</li>
 * </ul>
 */
public class OPPushCard {

    private Long moduleId;
    private String contentId;

    private String contentOne;
    private String contentTwo;
    private String contentThree;
    private String contentFour;
    private String contentFive;
    private String contentSix;
    private String contentSeven;


    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentOne() {
        return contentOne;
    }

    public void setContentOne(String contentOne) {
        this.contentOne = contentOne;
    }

    public String getContentTwo() {
        return contentTwo;
    }

    public void setContentTwo(String contentTwo) {
        this.contentTwo = contentTwo;
    }

    public String getContentThree() {
        return contentThree;
    }

    public void setContentThree(String contentThree) {
        this.contentThree = contentThree;
    }

    public String getContentFour() {
        return contentFour;
    }

    public void setContentFour(String contentFour) {
        this.contentFour = contentFour;
    }

    public String getContentFive() {
        return contentFive;
    }

    public void setContentFive(String contentFive) {
        this.contentFive = contentFive;
    }

    public String getContentSix() {
        return contentSix;
    }

    public void setContentSix(String contentSix) {
        this.contentSix = contentSix;
    }

    public String getContentSeven() {
        return contentSeven;
    }

    public void setContentSeven(String contentSeven) {
        this.contentSeven = contentSeven;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
