//
// EvhUiLaunchpadDeleteLaunchPadItemBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhUserLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiLaunchpadDeleteLaunchPadItemBySceneRestResponse
//
@interface EvhUiLaunchpadDeleteLaunchPadItemBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserLaunchPadItemDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
