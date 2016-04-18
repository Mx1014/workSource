//
// EvhAdminEnterpriseDeleteEnterpriseRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminEnterpriseDeleteEnterpriseRestResponse
//
@interface EvhAdminEnterpriseDeleteEnterpriseRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
