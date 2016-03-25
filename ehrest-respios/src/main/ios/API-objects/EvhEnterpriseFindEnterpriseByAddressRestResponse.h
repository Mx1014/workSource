//
// EvhEnterpriseFindEnterpriseByAddressRestResponse.h
// generated at 2016-03-25 09:26:44 
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
