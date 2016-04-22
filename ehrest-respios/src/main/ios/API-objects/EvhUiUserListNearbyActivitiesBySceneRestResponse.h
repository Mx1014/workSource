//
// EvhUiUserListNearbyActivitiesBySceneRestResponse.h
// generated at 2016-04-22 13:56:52 
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserListNearbyActivitiesBySceneRestResponse
//
@interface EvhUiUserListNearbyActivitiesBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
