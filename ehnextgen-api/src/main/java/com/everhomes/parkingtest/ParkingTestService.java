// @formatter:off
package com.everhomes.parkingtest;

import java.util.List;

public interface ParkingTestService {
    //查询
	ParkingLotTestResponse listParkingLotsTest(ListParkingLotsTestCommand cmd);
    //修改
    void listParkingLotsTestUpdate(SetListParkingLotsTestCommand cmd);
    //新增
    ParkingLotTestDTO listParkingLotsTestAdd(AddListParkingLotsTestCommand cmd);
    //删除
	void listParkingLotsTestDelete(DeleteListParkingLotsTestCommand cmd);
}
