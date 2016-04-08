//
// EvhConfCreateVideoConfInvitationRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"
#import "EvhVideoConfInvitationResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfCreateVideoConfInvitationRestResponse
//
@interface EvhConfCreateVideoConfInvitationRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVideoConfInvitationResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
