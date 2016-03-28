//
// EvhEnterpriseSearchEnterpriseRestResponse.h
// generated at 2016-03-28 15:56:09 
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
