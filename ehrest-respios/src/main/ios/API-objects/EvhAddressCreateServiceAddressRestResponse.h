//
// EvhAddressCreateServiceAddressRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"
#import "EvhUserServiceAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressCreateServiceAddressRestResponse
//
@interface EvhAddressCreateServiceAddressRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserServiceAddressDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
