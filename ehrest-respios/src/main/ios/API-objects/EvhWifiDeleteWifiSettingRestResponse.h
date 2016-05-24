//
// EvhWifiDeleteWifiSettingRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhWifiSettingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWifiDeleteWifiSettingRestResponse
//
@interface EvhWifiDeleteWifiSettingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhWifiSettingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
