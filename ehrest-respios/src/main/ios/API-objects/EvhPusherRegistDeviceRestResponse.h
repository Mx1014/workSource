//
// EvhPusherRegistDeviceRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhDeviceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPusherRegistDeviceRestResponse
//
@interface EvhPusherRegistDeviceRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDeviceDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
