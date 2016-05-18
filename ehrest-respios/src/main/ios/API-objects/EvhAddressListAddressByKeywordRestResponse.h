//
// EvhAddressListAddressByKeywordRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListAddressByKeywordCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListAddressByKeywordRestResponse
//
@interface EvhAddressListAddressByKeywordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListAddressByKeywordCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
