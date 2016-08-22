//
// EvhRentalAdminGetRefundOrderListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetRefundOrderListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetRefundOrderListRestResponse
//
@interface EvhRentalAdminGetRefundOrderListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetRefundOrderListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
