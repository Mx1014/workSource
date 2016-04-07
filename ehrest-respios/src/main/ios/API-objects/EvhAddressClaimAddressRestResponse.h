//
// EvhAddressClaimAddressRestResponse.h
// generated at 2016-04-07 15:16:53 
//
#import "RestResponseBase.h"
#import "EvhClaimedAddressInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressClaimAddressRestResponse
//
@interface EvhAddressClaimAddressRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhClaimedAddressInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
