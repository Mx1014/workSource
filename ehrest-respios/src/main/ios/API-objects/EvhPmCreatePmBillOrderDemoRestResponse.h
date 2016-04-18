//
// EvhPmCreatePmBillOrderDemoRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhCommonOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmCreatePmBillOrderDemoRestResponse
//
@interface EvhPmCreatePmBillOrderDemoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommonOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
