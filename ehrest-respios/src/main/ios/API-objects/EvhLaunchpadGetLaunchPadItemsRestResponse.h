//
// EvhLaunchpadGetLaunchPadItemsRestResponse.h
// generated at 2016-03-31 20:15:33 
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
