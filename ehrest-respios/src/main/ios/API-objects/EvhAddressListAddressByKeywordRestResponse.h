//
// EvhAddressListAddressByKeywordRestResponse.h
// generated at 2016-03-30 10:13:09 
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
