//
// EvhAdminOrgListDepartmentsRestResponse.h
// generated at 2016-04-07 17:03:18 
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
