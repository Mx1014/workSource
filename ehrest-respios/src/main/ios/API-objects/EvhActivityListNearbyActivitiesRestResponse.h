//
// EvhActivityListNearbyActivitiesRestResponse.h
// generated at 2016-04-26 18:22:56 
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
