//
// EvhActivityListNearbyActivitiesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListNearbyActivitiesResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListNearbyActivitiesRestResponse
//
@interface EvhActivityListNearbyActivitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNearbyActivitiesResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
