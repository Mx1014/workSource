package com.everhomes.recommend;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.EtagHelper;

/**
 * <p>用户推荐</p>
 * @author janson
 *
 */
@RestDoc(value="Recommendation", site="core")
@RestController
@RequestMapping("/recommend")
public class RecommendationController extends ControllerBase{
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationController.class);
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private RecommendationService recommendationService;
   
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    RecommendationProvider recommendationProvider;
   
    
    /**
     * <b>URL: /recommend/recommendUsers</b>
     * <p>获取推荐的用户</p>
     */
  @RequestMapping("recommendUsers")
  @RestReturn(RecommendUserResponse.class)
  public RestResponse getRecommendUsers(HttpServletResponse response
                      , HttpServletRequest request) {
      Long userId = UserContext.current().getUser().getId();
        UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, UserProfileContstant.RecommendName);
        long ageSec = 30;
  
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        if(profile == null || null == profile.getItemValue()) {
            return res;
            }
        String s = profile.getItemValue();
        long lastModify = Long.parseLong(s);
      
        if(EtagHelper.checkHeaderCache(ageSec, lastModify, request, response)) {
            RecommendUserResponse recommendRes = new RecommendUserResponse();
            recommendRes.setUsers(new ArrayList<RecommendUserInfo>());
            List<Recommendation> recommends = recommendationService.getRecommendsByUserId(userId
                    , RecommendSourceType.USER.getCode().intValue()
                    , PaginationConfigHelper.getPageSize(configProvider, null));
            
            for(Recommendation rec : recommends) {
                UserInfo userInfo = userService.getUserSnapshotInfo(rec.getSourceId());
                RecommendUserInfo reUser = ConvertHelper.convert(userInfo, RecommendUserInfo.class);
                
                if(rec.getEmbeddedJson() == null) {
                    continue;
                    }
                
                JSONObject jsonObject = (JSONObject) JSONValue.parse(rec.getEmbeddedJson());
                if(jsonObject != null) {
                    reUser.setUserName((String)jsonObject.get("userName"));
                    reUser.setFloorRelation((String)jsonObject.get("floorRelation"));
                    reUser.setUserSourceType((Long)jsonObject.get("userSourceType"));
                    }
                //jsonObject.put("userSourceType",RecommendUserSourceType.CONTACT_USER.getCode());
                recommendRes.getUsers().add(reUser);
            }
            res.setResponseObject(recommendRes); 
        }
        
        return res;
    }
  
  /**
   * 
  * <b>URL: /recommend/ignoreRecommend</b>
  * <p>忽略某一类用户的推荐</p>
   */
  @RequestMapping("ignoreRecommend")
  @RestReturn(String.class)
  public RestResponse ignoreRecommend(@Valid IgnoreRecommendCommand cmd) {
      RestResponse res = new RestResponse();
      Long userId = UserContext.current().getUser().getId();
      for(IgnoreRecommandItem item: cmd.getRecommendItems()) {
          try {
              recommendationService.ignoreRecommend(userId, item.getSuggestType().intValue(), item.getSourceId(), item.getSourceType().intValue());    
          } catch(Exception ex) {
              LOGGER.info("ignoreRecommend: " + ex.getMessage());
          }
      }
      
      UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, UserProfileContstant.RecommendName);
      //Update the modify time for ETAG
      if(null != profile) {
          profile.setItemValue(Long.toString(System.currentTimeMillis()));
      }
      res.setErrorCode(ErrorCodes.SUCCESS);
      res.setErrorDescription("OK");
      
      return res;
  }
  
  @RequestMapping("testAddUser")
  @RestReturn(String.class)
  public RestResponse testAddUser(@RequestParam(value="userId")Long userId, @RequestParam(value="sourceId")Long sourceId) {
//      Recommendation r = new Recommendation();
//      r.setAppid(0l);
//      r.setCreateTime(new Timestamp(System.currentTimeMillis()));
//      r.setExpireTime(new Timestamp(System.currentTimeMillis()+1000));
//      r.setMaxCount(1);
//      r.setScore(1.0);
//      r.setSourceId(sourceId);
//      r.setSourceType(RecommendSourceType.USER.getCode().intValue());
//      r.setStatus(RecommendStatus.OK.getCode());
//      r.setSuggestType(RecommendSourceType.USER.getCode().intValue());
//      r.setUserId(userId);
//      recommendationProvider.createRecommendation(r);
//      
//      //UserInfo user = userService.getUserInfo(userId);
//      UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, UserProfileContstant.RecommendName);
//      if(null != profile) {
//          profile.setItemValue(Long.toString(System.currentTimeMillis()));
//      } else {
//          UserProfile p2 = new UserProfile();
//          p2.setItemName(UserProfileContstant.RecommendName);
//          p2.setItemKind((byte)0);
//          p2.setItemValue(Long.toString(System.currentTimeMillis()));
//          p2.setOwnerId(userId);
//          userActivityProvider.addUserProfile(p2);
//          }
//      
      recommendationService.communityNotify(4l, 6l, 7l);
      
      RestResponse res = new RestResponse();
      res.setErrorCode(ErrorCodes.SUCCESS);
      res.setErrorDescription("OK");
      return res;
  }
  
}
