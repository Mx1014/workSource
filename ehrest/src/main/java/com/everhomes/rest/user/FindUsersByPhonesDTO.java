package com.everhomes.rest.user;

/**
 *<ul>
 *<li>userDTO:用户信息</li>
 *<li>phone:手机号</li>
 *<li>result:该号码的查询结果：为1表示有查询结果，为0表示查不到结果</li>
 * </ul>
 */
public class FindUsersByPhonesDTO {
    private String phone ;
    private UserDTO userDTO ;
    private Byte result ;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Byte getResult() {
        return result;
    }

    public void setResult(Byte result) {
        this.result = result;
    }
}
