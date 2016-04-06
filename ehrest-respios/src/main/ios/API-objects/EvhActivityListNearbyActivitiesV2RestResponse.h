//
// EvhActivityListNearbyActivitiesV2RestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhListNearbyActivitiesResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListNearbyActivitiesV2RestResponse
//
@interface EvhActivityListNearbyActivitiesV2RestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNearbyActivitiesResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
