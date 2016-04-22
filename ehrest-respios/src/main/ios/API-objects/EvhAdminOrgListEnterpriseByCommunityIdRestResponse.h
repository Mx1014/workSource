//
// EvhAdminOrgListEnterpriseByCommunityIdRestResponse.h
// generated at 2016-04-22 13:56:49 
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
