//@formatter:off
package com.everhomes.assetPayment;

//import com.everhomes.asset.ZhangJiangGaoKeThirdPartyAssetVendor;
import com.everhomes.rest.asset.ClientIdentityCommand;
import com.everhomes.rest.asset.NoticeObj;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/10.
 */

public class appTest {


    @Test
    public void fun(){
        Gson gson = new Gson();
        String data = "[{\"noticeObjType\":1,\"noticeObjId\":274897,\"noticeObjName\":\"kailin\"}]";
        List<NoticeObj> list = gson.fromJson(data, new TypeToken<List<List<NoticeObj>>>(){}.getType());
    }

}