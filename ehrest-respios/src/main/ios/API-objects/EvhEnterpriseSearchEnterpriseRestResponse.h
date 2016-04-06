//
// EvhEnterpriseSearchEnterpriseRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseSearchEnterpriseRestResponse
//
@interface EvhEnterpriseSearchEnterpriseRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
