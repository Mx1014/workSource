// @formatter:off
package com.everhomes.activity;

import com.everhomes.db.DbProvider;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.hotTag.HotTag;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.activity.*;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.hotTag.HotFlag;
import com.everhomes.rest.hotTag.HotTagServiceType;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.search.HotTagSearcher;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component(ActivityEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_ACTIVITY)
public class ActivityEmbeddedHandler implements ForumEmbeddedHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ActivityEmbeddedHandler.class);

    private final Gson gson;
	{
		gson = new GsonBuilder()
				.disableHtmlEscaping()
				.registerTypeAdapter(Date.class, new GsonJacksonDateAdapter())
				.registerTypeAdapter(Timestamp.class, new GsonJacksonTimestampAdapter())
				.create();
	}
    
    //版本分界线，对于活动来说，此版本之前为旧版本，按旧版本处理，之后为新版本
    private static final String SEPERATE_VERSION = "3.10.4";
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
	private HotTagSearcher hotTagSearcher;
    
    @Autowired
    private LocaleStringProvider localeStringProvider;

	@Autowired
	private ActivityProivider activityProivider;
	
    @Autowired
    private DbProvider dbProvider;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        try{
            ActivityDTO result = activityService.findSnapshotByPostId(post.getId());
            
            populateActivityVersion(result, post);
            processLocation(result);
            if(result!=null) 
                return gson.toJson(result);
        }catch(Exception e){
            LOGGER.error("handle snapshot error",e);
        }
        
        return null;
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        try{
            ActivityListResponse result = activityService.findActivityDetailsByPostId(post);
            if(result!=null){
            	populateActivityVersion(result.getActivity(), post);
            	processLocation(result.getActivity());
                return gson.toJson(result);
            }
        }catch(Exception e){
            LOGGER.error("handle details error",e);
        }
        
        return null;
    }

    private void processLocation(ActivityDTO activityDTO){
    	// 如果安卓没获取到经纬度会传特别小的数字过来，IOS无法解析出来，add by tt, 20161021
    	if (activityDTO != null && activityDTO.getLatitude() != null && activityDTO.getLongitude() != null
    			&& Math.abs(activityDTO.getLatitude().doubleValue()) < 0.000000001 && Math.abs(activityDTO.getLongitude().doubleValue()) < 0.000000001) {
			activityDTO.setLatitude(null);
			activityDTO.setLongitude(null);
		}
    }
    
    private void populateActivityVersion(ActivityDTO activityDTO, Post post){
    	if (activityDTO == null || post == null) {
			return;
		}
    	String userVersion = UserContext.current().getVersion();
    	String dataVersion = activityDTO.getVersion();
    	
    	//如果是旧版本:            
    	//a. 获取旧数据，直接显示post的content；
        //b. 获取新数据，如果为text后台根据活动的信息拼接好post的content返回给客户端，如果是rich_text则返回“抱歉，您的APP版本不支持查看该内容。”；
    	if (isOld(userVersion)) {
			if (isOld(dataVersion)) {
				return;
			}else {
				if (activityDTO.getContentType() != null && activityDTO.getContentType().equals(PostContentType.RICH_TEXT.getCode())) {
					post.setContent(getOldPostContent(activityDTO)+ "\n" + getLocalActivityString(ActivityLocalStringCode.ACTIVITY_INCOMPATIBLE));
				}else {
					post.setContent(getOldPostContent(activityDTO)+ "\n" + activityDTO.getDescription());
				}
			}
		}else {
            //a. 获取旧数据，后台置空post的content，对于列表客户端改用activity的content字段（后台拼接的字段，不存在表中）显示，对于详情客户端改用contentUrl显示；
            //b. 获取新数据，对于列表客户端改用activity的content字段（后台拼接的字段，不存在表中）显示，对于详情客户端改用contentUrl显示；
            //c. Web, 对于列表客户端改用activity的content字段（后台拼接的字段，不存在表中）显示，对于详情客户端改用activity的description显示；
			if (isOld(dataVersion)) {
				activityDTO.setContent(post.getContent());
				post.setContent("");
				// 部分旧数据没有description
				if (activityDTO.getDescription() == null || activityDTO.getDescription().equals("")) {
					activityDTO.setDescription(activityDTO.getContent());
				}
			}else {
				activityDTO.setContent(getOldPostContent(activityDTO));
			}
		}
    }
    
    private String getOldPostContent(ActivityDTO activityDTO){
    	return formatDate(activityDTO) + "\n"
    			+getLocalActivityString(ActivityLocalStringCode.ACTIVITY_LOCATION) + activityDTO.getLocation()+
    			(StringUtils.isNotBlank(activityDTO.getGuest())?"\n" + getLocalActivityString(ActivityLocalStringCode.ACTIVITY_INVITOR) + activityDTO.getGuest():"");

    }
    
    // timestamp格式化后会有一个.0在后面，把它去掉，add by tt, 20170310
    private String formatDate(String time) {
    	if (time.contains(".")) {
			return time.substring(0,time.lastIndexOf("."));
		}
    	return time;
    }
    
	//    1 若活动发布时全天开关开启，则活动时间显示为： 
	//    #活动开始时间# 年-月-日 ~ #活动结束时间# 年-月-日；
	//     1.1 若为同一天，则简化为  年-月-日 
	//     1.2 若为同年，则简化为  年-月-日 ~ 月-日
	//  2. 若活动发布时全天开关关闭，则活动时间显示为： 
	//       #活动开始时间# 年-月-日 时 : 分  ~ #活动结束时间# 年-月-日 时 : 分；
	//     2.1 若开始时间与结束时间为同一天，则简化为：年-月-日  时 : 分  ~ 时 : 分 
	//     2.2 若为同年，则简化  年-月-日  时 : 分  ~ 月-日   时 : 分  
    private String formatDate(ActivityDTO activityDTO){
    	
    	//如果时间字符串有异常，保持原样
    	if(activityDTO.getStartTime() == null || activityDTO.getStartTime().length() < 16 || 
    			activityDTO.getStopTime() == null || activityDTO.getStartTime().length() < 16){
    		
    		return getLocalActivityString(ActivityLocalStringCode.ACTIVITY_START_TIME) + formatDate(activityDTO.getStartTime())+"\n"
    				+getLocalActivityString(ActivityLocalStringCode.ACTIVITY_END_TIME) + formatDate(activityDTO.getStopTime());
    	}
    	
    	
    	String startYear = activityDTO.getStartTime().substring(0, 4);
    	String startMon = activityDTO.getStartTime().substring(5, 7);
    	String startDay = activityDTO.getStartTime().substring(8, 10);
    	String startHourAndMin = activityDTO.getStartTime().substring(11, 16);
    	
    	String stopYear = activityDTO.getStopTime().substring(0, 4);
    	String stopMon = activityDTO.getStopTime().substring(5, 7);
    	String stopDay = activityDTO.getStopTime().substring(8, 10);
    	String stopHourAndMin = activityDTO.getStopTime().substring(11, 16);
    	
    	
    	
    	String res = "";
    	String activityTimeStr = getLocalActivityString(ActivityLocalStringCode.ACTIVITY_TIME);
    	if(activityDTO.getAllDayFlag() != null && activityDTO.getAllDayFlag() == 1){
    		if(startYear.equals(stopYear) && startMon.equals(stopMon) && startDay.equals(stopDay)){
    			res = activityTimeStr + startYear + "-" + startMon + "-" + startDay;
    		}else if(startYear.equals(stopYear)){
    			res = activityTimeStr + startYear + "-" + startMon + "-" + startDay + " ~ " + stopMon + "-" + stopDay;
    		}else{
    			res = getLocalActivityString(ActivityLocalStringCode.ACTIVITY_START_TIME) + startYear + "-" + startMon + "-" + startDay + "\n"
    					+ getLocalActivityString(ActivityLocalStringCode.ACTIVITY_END_TIME) + stopYear + " ~ " + stopMon + "-" + stopDay;
    		}
    	}else{
    		if(startYear.equals(stopYear) && startMon.equals(stopMon) && startDay.equals(stopDay)){
    			res = activityTimeStr + startYear + "-" + startMon + "-" + startDay + " " + startHourAndMin + " ~ " + stopHourAndMin;
    		}else if(startYear.equals(stopYear)){
    			res = activityTimeStr + startYear + "-" + startMon + "-" + startDay + " " + startHourAndMin + " ~ " + stopMon + "-" + stopDay + " " + stopHourAndMin;
    		}else{
    			res = getLocalActivityString(ActivityLocalStringCode.ACTIVITY_START_TIME) + startYear + "-" + startMon + "-" + startDay + " " + startHourAndMin +"\n"
    					+getLocalActivityString(ActivityLocalStringCode.ACTIVITY_END_TIME) + stopYear + "-" + stopMon + "-" + stopDay + " "+ stopHourAndMin;
    		}
    	}

    	return res;
    }

    private boolean isOld(String versionString){
    	if (versionString == null || versionString.equals("")) {
			return true;
		}
    	
    	Version version = Version.fromVersionString(versionString);
    	Version seperateVersion = Version.fromVersionString(SEPERATE_VERSION);
    	if (version.getMajor() < seperateVersion.getMajor()) {
			return true;
		}else if (version.getMajor() == seperateVersion.getMajor() && version.getMinor() < seperateVersion.getMinor()) {
			return true;
		}else if (version.getMajor() == seperateVersion.getMajor() && version.getMinor() == seperateVersion.getMinor() && version.getRevision() < seperateVersion.getRevision()) {
			return true;
		}
    	
    	return false;
    }
    
    private String getLocalActivityString(int code){
    	LocaleString localeString = localeStringProvider.find(ActivityLocalStringCode.SCOPE, String.valueOf(code), UserContext.current().getUser().getLocale());
    	if (localeString != null) {
			return localeString.getText();
		}
    	return "";
    }
    
    @Override
    public Post preProcessEmbeddedObject(Post post) {
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        Long id = this.dbProvider.allocPojoRecordId(EhActivities.class);
        //Long id=shardingProvider.allocShardableContentId(EhActivities.class).second();
        post.setEmbeddedId(id);
        
        ActivityPostCommand cmd = (ActivityPostCommand) gson.fromJson(post.getEmbeddedJson(),
                ActivityPostCommand.class);
        
        if (StringUtils.isNotBlank(cmd.getSignupEndTime()) && cmd.getSignupEndTime().compareTo(cmd.getEndTime()) > 0) {
        	throw RuntimeErrorException.errorWith(ActivityServiceErrorCode.SCOPE, ActivityServiceErrorCode.ERROR_INVALID_ACTIVITY_SIGNUP_END_TIME,
					"signup end time must less or equal to end time");
		}
        
        if(null == cmd.getNamespaceId()){
        	Forum forum = forumProvider.findForumById(post.getForumId());
        	if(null != forum)
        		cmd.setNamespaceId(forum.getNamespaceId());
        	else
				cmd.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
        }
        
        if(StringUtils.isBlank(post.getTag()) && cmd.getTag() != null) {
            post.setTag(cmd.getTag());
        }

        //保证帖子和活动的clone状态是一致的  add by yanjun 20170807
        cmd.setCloneFlag(post.getCloneFlag());

        
//        if (OfficialFlag.fromCode(cmd.getOfficialFlag())!=OfficialFlag.YES) {
//			cmd.setOfficialFlag(OfficialFlag.NO.getCode());
//			post.setOfficialFlag(OfficialFlag.NO.getCode());
//		}else {
//			post.setOfficialFlag(cmd.getOfficialFlag());
//		}
        
        // 旧版本发布的活动只有officialFlag，新版本发布的活动有categoryId，当然更老的版本两者都没有
        // 为了兼容，规定categoryId为0对应发现里的活动（非官方活动），categoryId为1对应原官方活动
        // add by tt, 20170116
//        OfficialFlag officialFlag = OfficialFlag.fromCode(cmd.getOfficialFlag());
//        Long categoryId = cmd.getCategoryId();
//        if (officialFlag != null && officialFlag == OfficialFlag.YES) {
//			categoryId = 1L;
//		}else {
//			if (categoryId == null) {
//				if(officialFlag == null) officialFlag = OfficialFlag.NO;
//				categoryId = officialFlag == OfficialFlag.YES?1L:0L;
//			}else {
//				if (categoryId.longValue() == 1L) {
//					officialFlag = OfficialFlag.YES;
//				}else if (categoryId.longValue() == 0L) {
//					officialFlag = OfficialFlag.NO;
//				}else {
//					// 如果categoryId不是0和1，则表示是新增的入口，不与之前的官方非官方活动对应
//					officialFlag = OfficialFlag.UNKOWN;
//				}
//			}
//		}

		// 旧版本发布的活动只有officialFlag，新版本发布的活动有categoryId，当然更老的版本两者都没有
		// 为了兼容，规定categoryId为0对应发现里的活动（非官方活动），categoryId为1对应原官方活动
		// add by tt, 20170116
		// 因为旧版本的app在其他入口的情况下仍然会传OfficialFlag.YES，因此在上面规则的情况下，使用新的方法
		// 1、OfficialFlag为1时判断categoryId，如果是其他入口则使用OfficialFlag.UNKOWN，不然默认1
		// 2、officialFlag不是1时，根据categoryId给officialFlag赋值
		OfficialFlag officialFlag = OfficialFlag.fromCode(cmd.getOfficialFlag());
		Long categoryId = cmd.getCategoryId();
		if(officialFlag == OfficialFlag.YES){
			if(categoryId != null && categoryId.longValue() == 0) {
				categoryId = 1L;
			}else {
				officialFlag = OfficialFlag.UNKOWN;
			}
		}else{
			if(categoryId == null){
				categoryId = 0L;
				officialFlag = OfficialFlag.NO;
			}else if(categoryId.longValue() == 0){
				officialFlag = OfficialFlag.NO;
			}else if(categoryId.longValue() == 1){
				officialFlag = OfficialFlag.YES;
			}else{
				officialFlag = OfficialFlag.UNKOWN;
			}
		}


        cmd.setOfficialFlag(officialFlag.getCode());
        post.setOfficialFlag(officialFlag.getCode());
        cmd.setCategoryId(categoryId);
        post.setActivityCategoryId(categoryId);
        if (cmd.getContentCategoryId() == null) {
			cmd.setContentCategoryId(0L);
		}
        post.setActivityContentCategoryId(cmd.getContentCategoryId());
        
        
        cmd.setVideoState(VideoState.UN_READY.getCode());
        
        cmd.setForumId(post.getForumId());
        cmd.setCreatorTag(post.getCreatorTag());
        cmd.setTargetTag(post.getTargetTag());
        cmd.setVisibleRegionType(post.getVisibleRegionType());
        cmd.setVisibleRegionId(post.getVisibleRegionId());
        
        if(cmd.getChargeFlag() == null){
        	cmd.setChargeFlag((byte)0);
        }
        
        
        //运营要求：官方活动--如果开始时间早于当前时间，则设置创建时间为开始时间之前一天
		//产品要求：去除“官方活动”这个条件，对所有活动适应    add by yanjun 20170629
		//去除创建时间为开始时间之前一天这个设置 add by yanlong.liang 20180614
/*        try {
        	if(null != cmd.getStartTime()){
        		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		Date startTime= f.parse(cmd.getStartTime());
            	if(startTime.before(DateHelper.currentGMTTime())){
            		post.setCreateTime(new Timestamp(startTime.getTime() - 24*60*60*1000));
            	}
        	}
        	
        } catch (ParseException e) {
        }*/
		post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        //发布活动的时候没有选择是否支持微信，则选择改与空间默认的， add by yanjun 20170627
        if(cmd.getWechatSignup() == null){
			GetRosterOrderSettingCommand getRosterOrderSettingCommand = new GetRosterOrderSettingCommand();
			getRosterOrderSettingCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
			getRosterOrderSettingCommand.setCategoryId(cmd.getCategoryId());
			RosterOrderSettingDTO rosterOrderSettingDTO = activityService.getRosterOrderSetting(getRosterOrderSettingCommand);

			if(rosterOrderSettingDTO != null){
				cmd.setWechatSignup(rosterOrderSettingDTO.getWechatSignup());
			}else{
				cmd.setWechatSignup(WechatSignupFlag.YES.getCode());
			}

		}
        
        post.setEmbeddedJson(gson.toJson(cmd));
        
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post,Long communityId) {
        try{
            ActivityPostCommand cmd = (ActivityPostCommand) gson.fromJson(post.getEmbeddedJson(),
                    ActivityPostCommand.class);
            cmd.setId(post.getEmbeddedId());
            cmd.setMaxQuantity(post.getMaxQuantity());
			cmd.setMinQuantity(post.getMinQuantity());
            //comment by tt, 已经在preProcess里面处理过了
//            if(cmd.getCategoryId() == null) {
//            	cmd.setCategoryId(0L);
//            }
//            if (cmd.getContentCategoryId() == null) {
//				cmd.setContentCategoryId(0L);
//			}
            
            if(activityService.isPostIdExist(post.getId())){
            	activityService.updatePost(cmd, post.getId());
            }
            else{
            	activityService.createPost(cmd, post.getId(),communityId);
            }

            //if 与 try 防止tag保存Elastic异常导致发布活动的失败   add by yanjun
			if(StringUtils.isNotEmpty(cmd.getTag())){
				try{
					HotTag tag = new HotTag();
					Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
					tag.setNamespaceId(namespaceId);
					//TODO 业务类型、入口ID

					tag.setServiceType(HotTagServiceType.ACTIVITY.getCode());
					tag.setName(cmd.getTag());
					tag.setHotFlag(HotFlag.NORMAL.getCode());


					hotTagSearcher.feedDoc(tag);
				}catch (Exception e){
					LOGGER.error("feedDoc activity tag error",e);
				}
			}
            
        }catch(Exception e){
            LOGGER.error("create activity error",e);
        }
  
        return post;
    }

	@Override
	public void beforePostDelete(Post post) {

	}

	@Override
	public void afterPostDelete(Post post) {
		ActivityPostCommand cmd = (ActivityPostCommand) gson.fromJson(post.getEmbeddedJson(),
				ActivityPostCommand.class);
		cmd.setId(post.getEmbeddedId());
		cmd.setMaxQuantity(post.getMaxQuantity());
		activityService.updatePost(cmd, post.getId());


		Activity activity = activityProivider.findActivityById(post.getEmbeddedId());

		if (activity != null) {
			List<ActivityRoster> activityRosters = activityProivider.listRosters(activity.getId(), ActivityRosterStatus.NORMAL);
			for( int i=0; i< activityRosters.size(); i++){
				//如果有退款，先退款再取消订单
				ActivityRoster tempRoster = activityRosters.get(i);
				if(tempRoster.getStatus() != null && tempRoster.getStatus().byteValue() == ActivityRosterStatus.NORMAL.getCode()){
					activityService.signupOrderRefund(activity, tempRoster.getUid());

					tempRoster.setStatus(ActivityRosterStatus.CANCEL.getCode());
					tempRoster.setCancelTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					activityProivider.updateRoster(tempRoster);
				}

			}

			activity.setStatus(PostStatus.INACTIVE.getCode());
			activity.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			activityProivider.updateActivity(activity);
		}

	}
}
