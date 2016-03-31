//
// EvhConfCreateVideoConfInvitationRestResponse.h
// generated at 2016-03-31 15:43:24 
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
