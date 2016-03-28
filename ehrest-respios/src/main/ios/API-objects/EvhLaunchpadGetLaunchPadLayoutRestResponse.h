//
// EvhLaunchpadGetLaunchPadLayoutRestResponse.h
// generated at 2016-03-28 15:56:09 
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
