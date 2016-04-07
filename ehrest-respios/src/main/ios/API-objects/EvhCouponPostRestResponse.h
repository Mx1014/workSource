//
// EvhCouponPostRestResponse.h
// generated at 2016-04-07 14:16:31 
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
