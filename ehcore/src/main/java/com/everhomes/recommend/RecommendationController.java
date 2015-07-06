package com.everhomes.recommend;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.EtagHelper;

/**
 * <p>用户推荐</p>
 * @author janson
 *
 */
@RestDoc(value="Recommendation", site="ehcore")
@RestController
@RequestMapping("/recommend")
public class RecommendationController extends ControllerBase{
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private RecommendationService recommendationService;
   
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private UserProvider userProvider;
   
    
    /**
     * <b>URL: /recommend/recommendUsers</b>
     * <p>获取推荐的用户</p>
     */
  @RequestMapping("recommendUsers")
  @RestReturn(RecommendUserResponse.class)
  public RestResponse getRecommendUsers(@RequestParam(value="userId")Long userId
                      , HttpServletResponse response
                      , HttpServletRequest request) {
      //userActivityProvider.updateUserProfile(userId, UserProfileContstant.RecommendName, content);
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
            recommendRes.setUsers(new ArrayList<UserInfo>());
            List<Recommendation> recommends = recommendationService.getRecommendsByUserId(userId
                    , RecommendSourceType.USER.getCode().intValue()
                    , PaginationConfigHelper.getPageSize(configProvider, null));
            
            for(Recommendation rec : recommends) {
                User userInfo = userProvider.findUserById(rec.getSourceId());
                recommendRes.getUsers().add(ConvertHelper.convert(userInfo, UserInfo.class));
            }
            res.setResponseObject(recommendRes); 
        }
        
        return res;
    }
  
  @RequestMapping("ignoreRecommend")
  @RestReturn(String.class)
  public RestResponse ignoreRecommend(@Valid IgnoreRecommendCommand cmd) {
      RestResponse res = new RestResponse();
      recommendationService.ignoreRecommend(cmd.getUserId(), cmd.getSuggestType().intValue(), cmd.getSourceId(), cmd.getSourceType().intValue());
      UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(cmd.getUserId(), UserProfileContstant.RecommendName);
      //Update the modify time for ETAG
      if(null != profile) {
          profile.setItemValue(Long.toString(System.currentTimeMillis()));
      }
      res.setErrorCode(ErrorCodes.SUCCESS);
      res.setErrorDescription("OK");
      
      return res;
  }
}
