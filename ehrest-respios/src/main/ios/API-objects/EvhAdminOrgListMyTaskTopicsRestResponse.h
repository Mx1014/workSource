//
// EvhAdminOrgListMyTaskTopicsRestResponse.h
// generated at 2016-03-31 10:18:21 
//
#import "RestResponseBase.h"
#import "EvhListPostCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListMyTaskTopicsRestResponse
//
@interface EvhAdminOrgListMyTaskTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
