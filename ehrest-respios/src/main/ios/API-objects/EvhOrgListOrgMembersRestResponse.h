//
// EvhOrgListOrgMembersRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListOrgMembersRestResponse
//
@interface EvhOrgListOrgMembersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
