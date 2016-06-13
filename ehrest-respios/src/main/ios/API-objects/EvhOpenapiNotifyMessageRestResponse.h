//
// EvhOpenapiNotifyMessageRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNotifyDoorMessageResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiNotifyMessageRestResponse
//
@interface EvhOpenapiNotifyMessageRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNotifyDoorMessageResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
