//
// EvhAdminPushmessageListPushMessageResultRestResponse.h
// generated at 2016-03-31 19:08:54 
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
