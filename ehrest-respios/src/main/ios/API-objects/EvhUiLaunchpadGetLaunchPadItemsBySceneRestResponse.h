//
// EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse.h
// generated at 2016-04-07 17:33:50 
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
