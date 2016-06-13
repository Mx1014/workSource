//
// EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetLaunchPadItemsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse
//
@interface EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetLaunchPadItemsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
