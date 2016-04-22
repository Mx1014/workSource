//
// EvhAclinkSyncWebsocketMessagesRestResponse.h
// generated at 2016-04-22 13:56:49 
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
