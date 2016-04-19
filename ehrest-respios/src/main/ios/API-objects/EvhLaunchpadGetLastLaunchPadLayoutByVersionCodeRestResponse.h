//
// EvhLaunchpadGetLastLaunchPadLayoutByVersionCodeRestResponse.h
// generated at 2016-04-19 12:41:55 
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
