//
// EvhAddressClaimAddressRestResponse.h
// generated at 2016-04-07 10:47:32 
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
