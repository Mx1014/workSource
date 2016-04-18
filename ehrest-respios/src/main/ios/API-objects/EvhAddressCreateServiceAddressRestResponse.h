//
// EvhAddressCreateServiceAddressRestResponse.h
// generated at 2016-04-18 14:48:52 
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
