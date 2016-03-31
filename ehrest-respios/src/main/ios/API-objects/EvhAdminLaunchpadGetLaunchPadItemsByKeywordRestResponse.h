//
// EvhAdminLaunchpadGetLaunchPadItemsByKeywordRestResponse.h
// generated at 2016-03-31 10:18:21 
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
