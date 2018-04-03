package com.everhomes.rest.servicehotline;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *  <li>id:用户id</li>
 *  <li>accountName:用户名</li>
 *  <li>nickName:用户昵称</li>
 *  <li>phones:手机</li>
 *  <li>contractName:用户真实姓名</li>
 *  </ul>
 */
public class GetUserInfoByIdResponse {
    private Long id;
    private String accountName;
    private String nickName;
    @ItemType(String.class)
    private List<String> phones;
    private String contractName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
}
