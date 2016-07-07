//
// EvhUiActivityListNearbyActivitiesBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiActivityListNearbyActivitiesBySceneRestResponse
//
@interface EvhUiActivityListNearbyActivitiesBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
