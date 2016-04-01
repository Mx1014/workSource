//
// EvhPusherRecentMessagesRestResponse.h
// generated at 2016-04-01 15:40:24 
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
