//
// EvhEnterpriseListEnterpriseByPhoneRestResponse.h
// generated at 2016-04-26 18:22:56 
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
