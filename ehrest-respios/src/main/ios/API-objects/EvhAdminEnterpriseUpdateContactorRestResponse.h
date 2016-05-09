//
// EvhAdminEnterpriseUpdateContactorRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminEnterpriseUpdateContactorRestResponse
//
@interface EvhAdminEnterpriseUpdateContactorRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
