//
// EvhWifiListWifiSettingRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWifiListWifiSettingRestResponse
//
@interface EvhWifiListWifiSettingRestResponse : EvhRestResponseBase

// array of EvhListWifiSettingResponse* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
