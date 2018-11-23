package com.everhomes.visitorsys;

import java.util.List;

public interface HKWSUserProvider {

    void createUser(HKWSUser user);
    void createUsers(List<HKWSUser> users);
    void deleteUser(Integer personId);
    void deleteUsers(List<Integer> personIds);
    HKWSUser findUserById(Integer personId);
    List<HKWSUser> findUserByPhone(String phone);
}
