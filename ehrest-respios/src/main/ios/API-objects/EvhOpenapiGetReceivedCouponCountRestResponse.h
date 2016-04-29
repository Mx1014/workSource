//
// EvhOpenapiGetReceivedCouponCountRestResponse.h
// generated at 2016-04-29 18:56:03 
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
