//
// EvhQrcodeGetQRCodeInfoRestResponse.h
// generated at 2016-04-08 20:09:24 
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
