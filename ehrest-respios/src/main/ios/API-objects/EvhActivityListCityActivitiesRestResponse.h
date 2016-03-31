//
// EvhActivityListCityActivitiesRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhListNearbyActivitiesResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListCityActivitiesRestResponse
//
@interface EvhActivityListCityActivitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNearbyActivitiesResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
