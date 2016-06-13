//
// EvhUiLaunchpadGetLastLaunchPadLayoutBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhLaunchPadLayoutDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiLaunchpadGetLastLaunchPadLayoutBySceneRestResponse
//
@interface EvhUiLaunchpadGetLastLaunchPadLayoutBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadLayoutDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
