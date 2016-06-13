//
// EvhQrcodeGetQRCodeInfoRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhQRCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQrcodeGetQRCodeInfoRestResponse
//
@interface EvhQrcodeGetQRCodeInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQRCodeDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
