//
// EvhUiLaunchpadGetLastLaunchPadLayoutBySceneRestResponse.h
// generated at 2016-04-26 18:22:57 
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
