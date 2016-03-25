//
// EvhAdminOrgListDepartmentsRestResponse.h
// generated at 2016-03-25 11:43:34 
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
