//
// EvhAdminLaunchpadGetLaunchPadItemsByKeywordRestResponse.h
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
