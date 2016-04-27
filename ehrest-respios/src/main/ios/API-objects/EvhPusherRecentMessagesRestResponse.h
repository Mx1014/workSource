//
// EvhPusherRecentMessagesRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhDeviceMessages.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPusherRecentMessagesRestResponse
//
@interface EvhPusherRecentMessagesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDeviceMessages* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
