//
// EvhEnterpriseFindEnterpriseByAddressRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseFindEnterpriseByAddressRestResponse
//
@interface EvhEnterpriseFindEnterpriseByAddressRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
