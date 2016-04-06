//
// EvhUserCreateInvitationCodeRestResponse.h
// generated at 2016-04-06 19:10:44 
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
