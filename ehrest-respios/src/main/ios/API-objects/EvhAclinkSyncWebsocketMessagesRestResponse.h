//
// EvhAclinkSyncWebsocketMessagesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhAclinkWebSocketMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkSyncWebsocketMessagesRestResponse
//
@interface EvhAclinkSyncWebsocketMessagesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAclinkWebSocketMessage* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
