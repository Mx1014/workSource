//
// EvhWifiListWifiSettingRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListWifiSettingResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWifiListWifiSettingRestResponse
//
@interface EvhWifiListWifiSettingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListWifiSettingResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
