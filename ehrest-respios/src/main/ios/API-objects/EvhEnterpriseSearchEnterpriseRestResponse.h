//
// EvhEnterpriseSearchEnterpriseRestResponse.h
// generated at 2016-04-22 13:56:50 
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
