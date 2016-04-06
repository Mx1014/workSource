//
// EvhGroupListMembersInRoleRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhListMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListMembersInRoleRestResponse
//
@interface EvhGroupListMembersInRoleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
