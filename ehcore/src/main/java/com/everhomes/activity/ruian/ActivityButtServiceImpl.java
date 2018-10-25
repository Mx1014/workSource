package com.everhomes.activity.ruian;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.ruian.*;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.ui.activity.ruian.ListRuianActivityBySceneReponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;
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
    public List<ActivityCategoryModel> getCategoryList(Integer namespaceId , Long communityId ,  Long level1Id) {

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
        String result = WebApiRuianHelper.getIns().doPost(url, object.toString(),communityId);
        //解析结果
        ActivityCategoryList resultObject = (ActivityCategoryList) StringHelper.fromJsonString(result,ActivityCategoryList.class);
        if(resultObject.getCode()!=null && resultObject.getCode()== 1){//code 为1表示调用成功
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
    public List<ActivityModel> getActivityList(Integer namespaceId,Long communityId ,  Long activityCategoryID, Long activitySubCategoryID, Byte state, Integer pageSize, Integer pageIndex) {
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
        String result = WebApiRuianHelper.getIns().doPost(url, object.toString(),communityId);
        //解析结果
        ActivityModelList resultObject = (ActivityModelList) StringHelper.fromJsonString(result,ActivityModelList.class);
        if(resultObject.getCode()!=null && resultObject.getCode()== 1){//code 为1表示调用成功
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
    public ActivityDetailModel getActivityDetail(Integer namespaceId,Long communityId , Long activityId) {
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
        String result = WebApiRuianHelper.getIns().doPost(url, object.toString(),communityId);
        //解析结果
        ActivityDetailDTO resultObject = (ActivityDetailDTO) StringHelper.fromJsonString(result,ActivityDetailDTO.class);
        if(resultObject.getCode()!=null && resultObject.getCode() == 1){//code 为1表示调用成功
            return resultObject.getData();
        }
        return null;
    }

    @Override
    public ListRuianActivityBySceneReponse listActivityRuiAnEntitiesByScene() {
        //先查询出所有分类
        //List<ActivityCategoryModel> cateGorys = this.getCategoryList(null , null);
        //获取活动列表
        AppContext appContext = UserContext.current().getAppContext();
        Long communityId = null ;
        if(appContext != null){
            communityId = appContext.getCommunityId() ;
        }
        List<ActivityModel> activitys = this.getActivityList(null, communityId,null, null, null, 10, 1);
        ListRuianActivityBySceneReponse res = new ListRuianActivityBySceneReponse();
        res.setEntities(new ArrayList<>());
        if(CollectionUtils.isNotEmpty(activitys)){
            List<ActivityRuianDetail> entitys = transfer2LocalEntity(activitys,communityId);
            res.setEntities(entitys);
        }
        return res;
    }

    /**
     * 将对方的活动转成本地的活动
     * @param activitys
     * @return
     */
    private List<ActivityRuianDetail> transfer2LocalEntity(List<ActivityModel> activitys ,Long communityId){
        List<ActivityRuianDetail> entities = new ArrayList<ActivityRuianDetail>();
        if(CollectionUtils.isEmpty(activitys)){
            return entities;
        }
        for(ActivityModel model : activitys){
            ActivityRuianDetail dto = new ActivityRuianDetail();

            ActivityDetailModel detail = this.getActivityDetail(null,communityId,model.getActivityID());
            ActivityRuianDetail rdetail = ConvertHelper.convert(detail, ActivityRuianDetail.class);
            rdetail.setState(model.getState());

            entities.add(rdetail);
        }
        return entities;
    }



}
