//
// EvhUserListRecipientRestResponse.h
// generated at 2016-04-07 14:16:32 
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
