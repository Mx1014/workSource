//
// EvhConfUpdateInvoiceRestResponse.h
// generated at 2016-04-18 14:48:52 
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
