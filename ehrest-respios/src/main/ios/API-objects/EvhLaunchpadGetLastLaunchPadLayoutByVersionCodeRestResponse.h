//
// EvhLaunchpadGetLastLaunchPadLayoutByVersionCodeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhLaunchPadLayoutDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchpadGetLastLaunchPadLayoutByVersionCodeRestResponse
//
@interface EvhLaunchpadGetLastLaunchPadLayoutByVersionCodeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadLayoutDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
