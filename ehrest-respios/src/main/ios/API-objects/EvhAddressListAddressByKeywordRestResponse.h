//
// EvhAddressListAddressByKeywordRestResponse.h
// generated at 2016-04-12 15:02:20 
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
