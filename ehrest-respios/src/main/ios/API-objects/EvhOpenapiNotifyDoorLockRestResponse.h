//
// EvhOpenapiNotifyDoorLockRestResponse.h
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
