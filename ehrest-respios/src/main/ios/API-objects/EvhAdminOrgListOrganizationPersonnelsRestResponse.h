//
// EvhAdminOrgListOrganizationPersonnelsRestResponse.h
// generated at 2016-03-25 09:26:43 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrganizationPersonnelsRestResponse
//
@interface EvhAdminOrgListOrganizationPersonnelsRestResponse : EvhRestResponseBase

// array of EvhOrganizationMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
