//
// EvhPmCreatePmBillOrderDemoRestResponse.h
// generated at 2016-04-19 14:25:58 
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
