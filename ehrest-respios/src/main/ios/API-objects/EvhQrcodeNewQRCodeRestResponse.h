//
// EvhQrcodeNewQRCodeRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"
#import "EvhQRCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQrcodeNewQRCodeRestResponse
//
@interface EvhQrcodeNewQRCodeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQRCodeDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
