//
// EvhAdminOrgListEnterpriseByCommunityIdRestResponse.h
// generated at 2016-03-31 19:08:54 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListEnterpriseByCommunityIdRestResponse
//
@interface EvhAdminOrgListEnterpriseByCommunityIdRestResponse : EvhRestResponseBase

// array of EvhOrganizationDetailDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
