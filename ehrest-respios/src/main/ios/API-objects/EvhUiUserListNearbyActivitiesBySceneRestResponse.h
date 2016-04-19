//
// EvhUiUserListNearbyActivitiesBySceneRestResponse.h
// generated at 2016-04-19 12:41:55 
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
