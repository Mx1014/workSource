//
// EvhRentalAdminGetRefundOrderListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRefundOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetRefundOrderListRestResponse
//
@interface EvhRentalAdminGetRefundOrderListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRefundOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
