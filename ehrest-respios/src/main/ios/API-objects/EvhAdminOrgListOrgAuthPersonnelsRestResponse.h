//
// EvhAdminOrgListOrgAuthPersonnelsRestResponse.h
// generated at 2016-03-25 11:43:34 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrgAuthPersonnelsRestResponse
//
@interface EvhAdminOrgListOrgAuthPersonnelsRestResponse : EvhRestResponseBase

// array of EvhOrganizationMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
