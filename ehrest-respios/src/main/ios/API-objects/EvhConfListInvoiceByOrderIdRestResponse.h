//
// EvhConfListInvoiceByOrderIdRestResponse.h
// generated at 2016-04-22 13:56:50 
//
#import "RestResponseBase.h"
#import "EvhInvoiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListInvoiceByOrderIdRestResponse
//
@interface EvhConfListInvoiceByOrderIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhInvoiceDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
