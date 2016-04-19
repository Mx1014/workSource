//
// EvhOpenapiNotifyDoorLockRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhNotifyDoorMessageResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiNotifyDoorLockRestResponse
//
@interface EvhOpenapiNotifyDoorLockRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNotifyDoorMessageResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
