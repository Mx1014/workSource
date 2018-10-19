package com.everhomes.activity.ruian;

import com.everhomes.activity.ActivityServiceImpl;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.ruian.*;
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
        return new ArrayList<ActivityCategoryModel>();
    }

    /**
     * 获取活动列表
     * @param namespaceId 域空间ＩＤ
     * @param activityCategoryID　　一级分类ＩＤ
     * @param activitySubCategoryID　二级分类　ＩＤ
     * @param state　状态
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @Override
    public List<ActivityModel> getActivityList(Integer namespaceId, Long activityCategoryID, Long activitySubCategoryID, Byte state, Integer pageSize, Integer pageIndex) {
        namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
        String url = configProvider.getValue(namespaceId,"activity.butt.url.getactivitylist", "") ;
        if(StringUtils.isBlank(url)){
            LOGGER.error("the config activity.butt.url.getactivitylist is null .");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_URL_NOTEXIST, "the config activity.butt.url.getactivitylist is null ." );
        }

        JSONObject object = new JSONObject();
        if(activityCategoryID != null){
            object.put("ActivityCategoryID",activityCategoryID);
        }
        if(activitySubCategoryID != null){
            object.put("ActivitySubCategoryID",activitySubCategoryID);
        }
        if(state != null){
            object.put("State",state);
        }
        if(pageSize != null){
            object.put("PageSize",pageSize);
        }
        if(pageIndex != null){
            object.put("PageIndex",pageIndex);
        }
        String result = WebApiRuianHelper.getIns().doPost(url, object.toString());
        //解析结果
        ActivityModelList resultObject = (ActivityModelList) StringHelper.fromJsonString(result,ActivityModelList.class);
        if(resultObject.getCode()!=null && "1".equals(resultObject.getCode())){//code 为1表示调用成功
            return resultObject.getData();
        }
        return new ArrayList<ActivityModel>();
    }

    /**
     * 获取活动详情
     * @param namespaceId
     * @param activityId
     * @return
     */
    @Override
    public ActivityDetailModel getActivityDetail(Integer namespaceId,Long activityId) {
        namespaceId = UserContext.getCurrentNamespaceId(namespaceId);
        String url = configProvider.getValue(namespaceId,"activity.butt.url.getactivity ", "") ;
        if(StringUtils.isBlank(url)){
            LOGGER.error("the config activity.butt.url.getactivity  is null .");
            throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE,
                    ActivityServiceErrorCode.ERROR_URL_NOTEXIST, "the config activity.butt.url.getactivity  is null ." );
        }
        JSONObject object = new JSONObject();
        if(activityId != null){
            object.put("ActivityID",activityId);
        }
        String result = WebApiRuianHelper.getIns().doPost(url, object.toString());
        //解析结果
        ActivityDetailDTO resultObject = (ActivityDetailDTO) StringHelper.fromJsonString(result,ActivityDetailDTO.class);
        if(resultObject.getCode()!=null && "1".equals(resultObject.getCode())){//code 为1表示调用成功
            return resultObject.getData();
        }
        return null;
    }
}
