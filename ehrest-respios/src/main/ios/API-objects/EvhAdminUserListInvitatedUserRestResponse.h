//
// EvhAdminUserListInvitatedUserRestResponse.h
// generated at 2016-04-01 15:40:24 
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
