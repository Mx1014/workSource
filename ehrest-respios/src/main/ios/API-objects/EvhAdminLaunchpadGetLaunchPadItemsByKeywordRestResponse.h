//
// EvhAdminLaunchpadGetLaunchPadItemsByKeywordRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhGetLaunchPadItemsByKeywordAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminLaunchpadGetLaunchPadItemsByKeywordRestResponse
//
@interface EvhAdminLaunchpadGetLaunchPadItemsByKeywordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetLaunchPadItemsByKeywordAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
