//
// EvhWifiVerifyWifiRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhVerifyWifiDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWifiVerifyWifiRestResponse
//
@interface EvhWifiVerifyWifiRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVerifyWifiDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
