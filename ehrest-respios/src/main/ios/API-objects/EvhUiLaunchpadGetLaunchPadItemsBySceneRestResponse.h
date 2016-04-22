//
// EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse.h
// generated at 2016-04-22 13:56:52 
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
