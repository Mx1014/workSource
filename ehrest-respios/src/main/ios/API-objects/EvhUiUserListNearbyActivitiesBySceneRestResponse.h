//
// EvhUiUserListNearbyActivitiesBySceneRestResponse.h
// generated at 2016-04-29 18:56:04 
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
