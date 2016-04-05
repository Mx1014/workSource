//
// EvhCouponPostRestResponse.h
// generated at 2016-04-05 13:45:27 
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
