//
// EvhAddressCreateServiceAddressRestResponse.h
// generated at 2016-03-25 11:43:34 
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
