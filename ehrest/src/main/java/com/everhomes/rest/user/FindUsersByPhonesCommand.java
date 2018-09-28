package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import java.util.List;

/**

 *<ul>
 *<li>namespaceId:域空间ID</li>
 *<li>phones:手机号</li>
 *<li>clear:表示是否返回结果时清除查询不到用户的结果,1表示清除，0或其他表示不清除</li>
 * </ul>
 */
public class FindUsersByPhonesCommand {
    private Integer namespaceId;
    private List<String> phones ;
    private Byte clear ;


    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getClear() {
        return clear;
    }

    public void setClear(Byte clear) {
        this.clear = clear;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
