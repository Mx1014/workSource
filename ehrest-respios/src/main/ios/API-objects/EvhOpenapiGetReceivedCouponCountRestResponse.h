//
// EvhOpenapiGetReceivedCouponCountRestResponse.h
// generated at 2016-04-19 13:40:01 
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
