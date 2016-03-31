//
// EvhUiLaunchpadGetLaunchPadItemsBySceneRestResponse.h
// generated at 2016-03-31 11:07:27 
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
