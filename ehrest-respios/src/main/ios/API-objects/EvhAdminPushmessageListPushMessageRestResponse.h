//
// EvhAdminPushmessageListPushMessageRestResponse.h
// generated at 2016-03-25 09:26:43 
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
