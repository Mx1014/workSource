//
// EvhLaunchpadGetLaunchPadItemsRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhGetLaunchPadItemsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchpadGetLaunchPadItemsRestResponse
//
@interface EvhLaunchpadGetLaunchPadItemsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetLaunchPadItemsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
