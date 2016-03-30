//
// EvhUserCreateInvitationCodeRestResponse.h
// generated at 2016-03-30 10:13:10 
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
