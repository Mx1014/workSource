//
// EvhPusherRegistDeviceRestResponse.h
// generated at 2016-04-07 15:16:54 
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
