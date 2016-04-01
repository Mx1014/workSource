//
// EvhConfListVideoConfAccountByEnterpriseIdRestResponse.h
// generated at 2016-04-01 15:40:24 
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
