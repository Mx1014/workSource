//
// EvhAdminUserSearchInvitatedUserRestResponse.h
// generated at 2016-03-31 13:49:15 
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
