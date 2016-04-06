//
// EvhOpenapiNotifyMessageRestResponse.h
// generated at 2016-04-06 19:59:47 
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
