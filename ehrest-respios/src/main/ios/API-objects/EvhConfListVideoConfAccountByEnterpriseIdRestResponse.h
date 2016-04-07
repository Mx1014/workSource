//
// EvhConfListVideoConfAccountByEnterpriseIdRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseVideoConfAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListVideoConfAccountByEnterpriseIdRestResponse
//
@interface EvhConfListVideoConfAccountByEnterpriseIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseVideoConfAccountResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
