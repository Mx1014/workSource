//
// EvhPusherRecentMessagesRestResponse.h
// generated at 2016-03-31 10:18:21 
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
