//
// EvhUiLaunchpadGetLastLaunchPadLayoutBySceneRestResponse.h
// generated at 2016-03-31 10:18:21 
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
