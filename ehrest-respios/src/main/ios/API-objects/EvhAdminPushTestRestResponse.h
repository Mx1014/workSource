//
// EvhAdminPushTestRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSendMessageTestResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPushTestRestResponse
//
@interface EvhAdminPushTestRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSendMessageTestResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
