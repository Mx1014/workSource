//
// EvhAdminPushmessageListPushMessageResultRestResponse.h
// generated at 2016-04-19 12:41:54 
//
#import "RestResponseBase.h"
#import "EvhListPushMessageResultResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPushmessageListPushMessageResultRestResponse
//
@interface EvhAdminPushmessageListPushMessageResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPushMessageResultResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
