//
// EvhAdminPushmessageListPushMessageRestResponse.h
// generated at 2016-03-31 13:49:15 
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
