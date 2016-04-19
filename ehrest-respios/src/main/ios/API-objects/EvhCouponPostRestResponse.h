//
// EvhCouponPostRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"
#import "EvhCouponDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCouponPostRestResponse
//
@interface EvhCouponPostRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCouponDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
