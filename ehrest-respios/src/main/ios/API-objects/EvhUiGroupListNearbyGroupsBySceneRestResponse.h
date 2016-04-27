//
// EvhUiGroupListNearbyGroupsBySceneRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhListNearbyGroupCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiGroupListNearbyGroupsBySceneRestResponse
//
@interface EvhUiGroupListNearbyGroupsBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNearbyGroupCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
