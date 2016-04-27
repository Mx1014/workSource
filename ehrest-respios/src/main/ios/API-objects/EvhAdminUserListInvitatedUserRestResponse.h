//
// EvhAdminUserListInvitatedUserRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhListInvitatedUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserListInvitatedUserRestResponse
//
@interface EvhAdminUserListInvitatedUserRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListInvitatedUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
