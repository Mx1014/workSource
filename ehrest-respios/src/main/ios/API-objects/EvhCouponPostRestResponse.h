//
// EvhCouponPostRestResponse.h
// generated at 2016-04-19 12:41:55 
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
