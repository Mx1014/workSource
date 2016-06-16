//
// EvhUiLaunchpadAddLaunchPadItemBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhUserLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiLaunchpadAddLaunchPadItemBySceneRestResponse
//
@interface EvhUiLaunchpadAddLaunchPadItemBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserLaunchPadItemDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
