//
// EvhConfVerifyPurchaseAuthorityRestResponse.h
// generated at 2016-04-05 13:45:27 
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
