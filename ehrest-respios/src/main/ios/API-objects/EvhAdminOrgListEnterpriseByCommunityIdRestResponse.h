//
// EvhAdminOrgListEnterpriseByCommunityIdRestResponse.h
// generated at 2016-04-12 15:02:20 
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
