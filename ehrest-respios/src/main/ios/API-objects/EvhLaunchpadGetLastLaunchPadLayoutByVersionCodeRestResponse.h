//
// EvhLaunchpadGetLastLaunchPadLayoutByVersionCodeRestResponse.h
// generated at 2016-04-07 15:16:54 
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
