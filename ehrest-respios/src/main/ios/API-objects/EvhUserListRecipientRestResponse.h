//
// EvhUserListRecipientRestResponse.h
// generated at 2016-03-25 09:26:45 
//
#import "RestResponseBase.h"
#import "EvhInvitationCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListRecipientRestResponse
//
@interface EvhUserListRecipientRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhInvitationCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
