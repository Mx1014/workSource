//
// EvhAdminMessageTestRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSendMessageTestResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminMessageTestRestResponse
//
@interface EvhAdminMessageTestRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSendMessageTestResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
