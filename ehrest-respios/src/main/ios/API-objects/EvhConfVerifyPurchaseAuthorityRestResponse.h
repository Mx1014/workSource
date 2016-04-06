//
// EvhConfVerifyPurchaseAuthorityRestResponse.h
// generated at 2016-04-06 19:10:43 
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
