//
// EvhAdminOrgListDepartmentsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListDepartmentsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListDepartmentsRestResponse
//
@interface EvhAdminOrgListDepartmentsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDepartmentsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
