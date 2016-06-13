//
// EvhWifiCreateWifiSettingRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhWifiSettingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWifiCreateWifiSettingRestResponse
//
@interface EvhWifiCreateWifiSettingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhWifiSettingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
