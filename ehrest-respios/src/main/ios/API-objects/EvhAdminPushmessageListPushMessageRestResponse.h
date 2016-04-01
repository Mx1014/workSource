//
// EvhAdminPushmessageListPushMessageRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhListPushMessageResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPushmessageListPushMessageRestResponse
//
@interface EvhAdminPushmessageListPushMessageRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPushMessageResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
