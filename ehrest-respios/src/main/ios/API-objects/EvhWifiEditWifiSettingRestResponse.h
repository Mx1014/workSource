//
// EvhWifiEditWifiSettingRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhWifiSettingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWifiEditWifiSettingRestResponse
//
@interface EvhWifiEditWifiSettingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhWifiSettingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
