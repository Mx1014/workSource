//
// EvhConfCreateVideoConfInvitationRestResponse.h
// generated at 2016-04-19 14:25:57 
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
