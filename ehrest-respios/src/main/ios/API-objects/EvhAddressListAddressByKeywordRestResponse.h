//
// EvhAddressListAddressByKeywordRestResponse.h
// generated at 2016-04-07 15:16:53 
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
