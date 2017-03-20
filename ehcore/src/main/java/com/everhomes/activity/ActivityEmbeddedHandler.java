// @formatter:off
package com.everhomes.activity;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.hotTag.HotTags;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityListResponse;
import com.everhomes.rest.activity.ActivityLocalStringCode;
import com.everhomes.rest.activity.ActivityPostCommand;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.rest.activity.VideoState;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.forum.PostContentType;
import com.everhomes.rest.hotTag.HotTagServiceType;
import com.everhomes.rest.hotTag.HotTagStatus;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.search.HotTagSearcher;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Version;

@Component(ActivityEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_ACTIVITY)
public class ActivityEmbeddedHandler implements ForumEmbeddedHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ActivityEmbeddedHandler.class);
    
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

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        try{
            ActivityDTO result = activityService.findSnapshotByPostId(post.getId());
            
            populateActivityVersion(result, post);
            processLocation(result);
            if(result!=null) 
                return StringHelper.toJsonString(result);
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
                return StringHelper.toJsonString(result);
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
    	return getLocalActivityString(ActivityLocalStringCode.ACTIVITY_START_TIME) + formatDate(activityDTO.getStartTime())+"\n"
    				+getLocalActivityString(ActivityLocalStringCode.ACTIVITY_END_TIME) + formatDate(activityDTO.getStopTime())+"\n"
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
        Long id=shardingProvider.allocShardableContentId(EhActivities.class).second();
        post.setEmbeddedId(id);
        
        ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
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
        
        if(cmd.getTag() != null) {
            post.setTag(cmd.getTag());
        }
        
//        if (OfficialFlag.fromCode(cmd.getOfficialFlag())!=OfficialFlag.YES) {
//			cmd.setOfficialFlag(OfficialFlag.NO.getCode());
//			post.setOfficialFlag(OfficialFlag.NO.getCode());
//		}else {
//			post.setOfficialFlag(cmd.getOfficialFlag());
//		}
        
        // 旧版本发布的活动只有officialFlag，新版本发布的活动有categoryId，当然更老的版本两者都没有
        // 为了兼容，规定categoryId为0对应发现里的活动（非官方活动），categoryId为1对应原官方活动
        // add by tt, 20170116
        OfficialFlag officialFlag = OfficialFlag.fromCode(cmd.getOfficialFlag());
        Long categoryId = cmd.getCategoryId();
        if (officialFlag != null && officialFlag == OfficialFlag.YES) {
			categoryId = 1L;
		}else {
			if (categoryId == null) {
				if(officialFlag == null) officialFlag = OfficialFlag.NO;
				categoryId = officialFlag == OfficialFlag.YES?1L:0L;
			}else {
				if (categoryId.longValue() == 1L) {
					officialFlag = OfficialFlag.YES;
				}else if (categoryId.longValue() == 0L) {
					officialFlag = OfficialFlag.NO;
				}else {
					// 如果categoryId不是0和1，则表示是新增的入口，不与之前的官方非官方活动对应
					officialFlag = OfficialFlag.UNKOWN;
				}
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
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        try{
            ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                    ActivityPostCommand.class);
            cmd.setId(post.getEmbeddedId());
            cmd.setMaxQuantity(post.getMaxQuantity());
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
            	activityService.createPost(cmd, post.getId()); 
            }
            
            HotTags tag = new HotTags();
            tag.setName(cmd.getTag());
            tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
            tag.setServiceType(HotTagServiceType.ACTIVITY.getCode());
            hotTagSearcher.feedDoc(tag);
            
        }catch(Exception e){
            LOGGER.error("create activity error",e);
        }
  
        return post;
    }

}
