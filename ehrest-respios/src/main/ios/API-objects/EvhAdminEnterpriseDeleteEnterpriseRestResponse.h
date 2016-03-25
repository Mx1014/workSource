//
// EvhAdminEnterpriseDeleteEnterpriseRestResponse.h
// generated at 2016-03-25 09:26:43 
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
