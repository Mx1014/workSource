//
// EvhConfListEnterpriseWithVideoConfAccountRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseWithVideoConfAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListEnterpriseWithVideoConfAccountRestResponse
//
@interface EvhConfListEnterpriseWithVideoConfAccountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseWithVideoConfAccountResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
