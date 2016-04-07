//
// EvhEnterpriseListEnterpriseByPhoneRestResponse.h
// generated at 2016-04-07 10:47:33 
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
