package com.everhomes.activity.ruian;

import com.everhomes.activity.ActivityServiceImpl;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.ruian.ActivityCategoryList;
import com.everhomes.rest.activity.ruian.ActivityCategoryModel;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 对接瑞安新天地活动处理类　add by huangliangming 20181016
 */
@Service
public class ActivityButtServiceImpl implements ActivityButtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityButtServiceImpl.class);

    @Autowired
    private ConfigurationProvider configProvider;

    /**
     * 获取活动分类
     * @param namespaceId
     * @param level1Id　入参有值即查一级分类，有值即查二级分类
     * @return
     */
    @Override
    public List<ActivityCategoryModel> getCategoryList(Integer namespaceId , Long level1Id) {

        namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
        String url = configProvider.getValue(namespaceId,"activity.butt.url.getcategorylist", "") ;
        if(StringUtils.isBlank(url)){
            LOGGER.error("the config activity.butt.url.getcategorylist is null .");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_URL_NOTEXIST, "the config activity.butt.url.getcategorylist is null ." );
        }
        JSONObject object = new JSONObject();
        if(level1Id != null){
            object.put("ParentID",level1Id);
        }
        String result = WebApiRuianHelper.getIns().doPost(url, object.toString());
        //解析结果
        ActivityCategoryList resultObject = (ActivityCategoryList) StringHelper.fromJsonString(result,ActivityCategoryList.class);
        if(resultObject.getCode()!=null && "1".equals(resultObject.getCode())){//code 为1表示调用成功
            return resultObject.getData();
        }
        List<ActivityCategoryModel>  returnList = new ArrayList<ActivityCategoryModel>();
        return returnList;
    }


}
