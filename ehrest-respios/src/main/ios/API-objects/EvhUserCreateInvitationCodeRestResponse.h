//
// EvhUserCreateInvitationCodeRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhUserInvitationsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserCreateInvitationCodeRestResponse
//
@interface EvhUserCreateInvitationCodeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInvitationsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
