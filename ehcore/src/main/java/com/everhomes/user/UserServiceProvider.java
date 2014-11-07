package com.everhomes.user;

import java.util.List;

import com.everhomes.rest.user.LoginToken;

public interface UserServiceProvider {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    User findUserById(long id);
    
    List<UserDevice> listUserDevicesOfUser(long userId);

    void createDevice(UserDevice userDevice);
    void updateDevice(UserDevice userDevice);
    void deleteDevice(UserDevice userDevice);
    void deleteDevice(long id);
    UserDevice findDeviceById(long id);
    UserDevice findDeviceByDeviceNumberOrEmail(String deviceNumber);
    
    DeviceLogin logon(String deviceNumberOrEmail, String password);
    boolean isValidLoginToken(LoginToken loginToken);
}
