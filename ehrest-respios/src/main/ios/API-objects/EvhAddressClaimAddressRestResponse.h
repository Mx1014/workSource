//
// EvhAddressClaimAddressRestResponse.h
// generated at 2016-03-31 10:18:21 
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
