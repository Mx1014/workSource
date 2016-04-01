//
// EvhConfUpdateInvoiceRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhInvoiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfUpdateInvoiceRestResponse
//
@interface EvhConfUpdateInvoiceRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhInvoiceDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
