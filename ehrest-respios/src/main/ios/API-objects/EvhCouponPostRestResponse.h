//
// EvhCouponPostRestResponse.h
// generated at 2016-03-30 10:13:09 
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
