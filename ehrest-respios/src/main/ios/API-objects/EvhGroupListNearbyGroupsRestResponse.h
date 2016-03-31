//
// EvhGroupListNearbyGroupsRestResponse.h
// generated at 2016-03-28 15:56:09 
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
