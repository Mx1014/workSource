//
// EvhEnterpriseListEnterpriseByPhoneRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhQueryEnterpriseByPhoneResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseListEnterpriseByPhoneRestResponse
//
@interface EvhEnterpriseListEnterpriseByPhoneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQueryEnterpriseByPhoneResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
