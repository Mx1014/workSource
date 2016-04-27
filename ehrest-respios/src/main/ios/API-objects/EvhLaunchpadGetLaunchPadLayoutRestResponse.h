//
// EvhLaunchpadGetLaunchPadLayoutRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhLaunchPadLayoutDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchpadGetLaunchPadLayoutRestResponse
//
@interface EvhLaunchpadGetLaunchPadLayoutRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadLayoutDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
