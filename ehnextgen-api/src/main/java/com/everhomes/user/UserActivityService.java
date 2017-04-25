package com.everhomes.user;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.everhomes.rest.activity.ListActiveStatResponse;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.openapi.GetUserServiceAddressCommand;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.ui.user.UserProfileDTO;
import com.everhomes.rest.user.AddRequestCommand;
import com.everhomes.rest.user.AddUserFavoriteCommand;
import com.everhomes.rest.user.CancelUserFavoriteCommand;
import com.everhomes.rest.user.CommunityStatusResponse;
import com.everhomes.rest.user.ContactDTO;
import com.everhomes.rest.user.FeedbackCommand;
import com.everhomes.rest.user.GetCustomRequestTemplateCommand;
import com.everhomes.rest.user.GetRequestInfoCommand;
import com.everhomes.rest.user.InvitationCommandResponse;
import com.everhomes.rest.user.ListActiveStatCommand;
import com.everhomes.rest.user.ListBusinessTreasureResponse;
import com.everhomes.rest.user.ListFeedbacksCommand;
import com.everhomes.rest.user.ListFeedbacksResponse;
import com.everhomes.rest.user.ListPostResponse;
import com.everhomes.rest.user.ListPostedActivityByOwnerIdCommand;
import com.everhomes.rest.user.ListPostedTopicByOwnerIdCommand;
import com.everhomes.rest.user.ListSignupActivitiesCommand;
import com.everhomes.rest.user.ListTreasureResponse;
import com.everhomes.rest.user.ListUserFavoriteActivityCommand;
import com.everhomes.rest.user.ListUserFavoriteTopicCommand;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.user.RequestTemplateDTO;
import com.everhomes.rest.user.SyncActivityCommand;
import com.everhomes.rest.user.SyncBehaviorCommand;
import com.everhomes.rest.user.SyncInsAppsCommand;
import com.everhomes.rest.user.SyncLocationCommand;
import com.everhomes.rest.user.SyncUserContactCommand;
import com.everhomes.rest.user.UpdateFeedbackCommand;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.util.Tuple;

public interface UserActivityService {
    CommunityStatusResponse listCurrentCommunityStatus();

    void updateLocation(SyncLocationCommand cmd);

    void updateActivity(SyncActivityCommand cmd);

    void updateUserBehavoir(SyncBehaviorCommand cmd);

    void syncInstalledApps(SyncInsAppsCommand installApps);

    void updateContacts(SyncUserContactCommand cmd);

    InvitationCommandResponse listInvitedUsers(Long anchor);

    Tuple<Long, List<ContactDTO>> listUserContacts(Long anchor);

    List<UserContact> listUserRetainIdentifiers(String identifer);

    void addFeedback(FeedbackCommand cmd);
    
    ListFeedbacksResponse ListFeedbacks(ListFeedbacksCommand cmd);
    
    void updateFeedback(UpdateFeedbackCommand cmd);
    
    void cancelFavorite(CancelUserFavoriteCommand cmd);

    void addUserFavorite(AddUserFavoriteCommand cmd);

    ListPostResponse listPostedTopics(ListPostedTopicByOwnerIdCommand cmd);
    
    ListPostResponse listFavoriteTopics(ListUserFavoriteTopicCommand cmd);
    
    ListTreasureResponse getUserTreasure();
    
    List<UserServiceAddressDTO> getUserRelateServiceAddress();
    
    void addUserShop(Long userId);
    
    void cancelShop(Long userId);

    List<UserServiceAddressDTO> getUserServiceAddress(GetUserServiceAddressCommand cmd);
    void receiveCoupon(Long userId);
    void invalidCoupon(Long userId);

	void updateUserProfile(Long userId, String name, String value);
	
	ListActivitiesReponse listActivityFavorite(ListUserFavoriteActivityCommand cmd);
	ListActivitiesReponse listPostedActivities(ListPostedActivityByOwnerIdCommand cmd);
	ListActivitiesReponse listSignupActivities(ListSignupActivitiesCommand cmd);

	UserProfileDTO findUserProfileBySpecialKey(Long userId,String itemName);

	void updateProfileIfNotExist(Long userId, String itemName, Integer itemValue);
	
	RequestTemplateDTO getCustomRequestTemplate(@Valid GetCustomRequestTemplateCommand cmd);
	List<RequestTemplateDTO> getCustomRequestTemplateByNamespace();
	void addCustomRequest(@Valid AddRequestCommand cmd);
	GetRequestInfoResponse getCustomRequestInfo(@Valid GetRequestInfoCommand cmd);

	ListActiveStatResponse listActiveStat(ListActiveStatCommand cmd);

	void addAnyDayActive(Date statDate, Integer namespaceId);
	
	String getBizUrl();

	ListBusinessTreasureResponse getUserBusinessTreasure();
	
	void updateShakeOpenDoor(Byte shakeOpenDoor);
 
}
