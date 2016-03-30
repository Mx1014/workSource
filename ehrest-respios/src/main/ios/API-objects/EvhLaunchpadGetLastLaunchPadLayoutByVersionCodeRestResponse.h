//
// EvhLaunchpadGetLastLaunchPadLayoutByVersionCodeRestResponse.h
// generated at 2016-03-30 10:13:09 
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
