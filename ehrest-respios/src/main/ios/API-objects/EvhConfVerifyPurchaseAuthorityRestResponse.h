//
// EvhConfVerifyPurchaseAuthorityRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhVerifyPurchaseAuthorityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfVerifyPurchaseAuthorityRestResponse
//
@interface EvhConfVerifyPurchaseAuthorityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVerifyPurchaseAuthorityResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
