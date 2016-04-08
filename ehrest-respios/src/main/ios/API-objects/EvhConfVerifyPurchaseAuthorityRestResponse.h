//
// EvhConfVerifyPurchaseAuthorityRestResponse.h
// generated at 2016-04-08 20:09:23 
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
