//
// EvhQrcodeNewQRCodeRestResponse.h
// generated at 2016-04-12 15:02:21 
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
