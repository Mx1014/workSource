//
// EvhPmsyCreatePmBillOrderRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommonOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyCreatePmBillOrderRestResponse
//
@interface EvhPmsyCreatePmBillOrderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommonOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
