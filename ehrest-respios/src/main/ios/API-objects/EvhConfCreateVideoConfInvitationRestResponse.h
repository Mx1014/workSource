//
// EvhConfCreateVideoConfInvitationRestResponse.h
// generated at 2016-04-26 18:22:56 
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
