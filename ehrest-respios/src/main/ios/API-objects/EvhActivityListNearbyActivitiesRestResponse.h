//
// EvhActivityListNearbyActivitiesRestResponse.h
// generated at 2016-03-25 09:26:43 
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
