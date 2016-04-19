//
// EvhEnterpriseFindEnterpriseByAddressRestResponse.h
// generated at 2016-04-19 14:25:58 
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
