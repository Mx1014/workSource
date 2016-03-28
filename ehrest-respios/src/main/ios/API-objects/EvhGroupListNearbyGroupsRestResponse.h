//
// EvhGroupListNearbyGroupsRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhListNearbyGroupCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListNearbyGroupsRestResponse
//
@interface EvhGroupListNearbyGroupsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNearbyGroupCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
