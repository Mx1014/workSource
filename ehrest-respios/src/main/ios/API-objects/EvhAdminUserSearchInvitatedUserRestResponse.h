//
// EvhAdminUserSearchInvitatedUserRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"
#import "EvhListInvitatedUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserSearchInvitatedUserRestResponse
//
@interface EvhAdminUserSearchInvitatedUserRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListInvitatedUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
