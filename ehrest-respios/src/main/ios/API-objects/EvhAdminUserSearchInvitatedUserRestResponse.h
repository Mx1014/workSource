//
// EvhAdminUserSearchInvitatedUserRestResponse.h
// generated at 2016-04-19 14:25:57 
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
