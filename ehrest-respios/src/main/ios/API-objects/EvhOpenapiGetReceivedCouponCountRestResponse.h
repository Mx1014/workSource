//
// EvhOpenapiGetReceivedCouponCountRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhUserProfileDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetReceivedCouponCountRestResponse
//
@interface EvhOpenapiGetReceivedCouponCountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserProfileDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
