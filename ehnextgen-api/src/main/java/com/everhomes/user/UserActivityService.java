package com.everhomes.user;

import com.everhomes.rest.activity.ListActiveStatResponse;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.openapi.GetUserServiceAddressCommand;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.rest.ui.user.UserProfileDTO;
import com.everhomes.rest.user.*;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.util.Tuple;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

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
    
    ListFeedbacksResponse listFeedbacks(ListFeedbacksCommand cmd);

    void exportFeedbacks(ExportFeedbacksCommand cmd);

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

    GetUserTreasureResponse getUserTreasureV2();

    GetUserTreasureNewResponse getUserTreasureNew();

    GetUserTreasureForRuiAnResponse getUserTreasureForRuiAn();
}
