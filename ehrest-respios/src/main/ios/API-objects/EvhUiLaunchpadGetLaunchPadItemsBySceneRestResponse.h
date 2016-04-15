//
// EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse.h
// generated at 2016-04-12 15:02:21 
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
