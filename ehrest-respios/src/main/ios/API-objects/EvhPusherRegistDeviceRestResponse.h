//
// EvhPusherRegistDeviceRestResponse.h
// generated at 2016-04-12 15:02:21 
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
