//
// EvhVerifyPurchaseAuthorityResponse.h
// generated at 2016-04-05 13:45:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPurchaseAuthorityResponse
//
@interface EvhVerifyPurchaseAuthorityResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* purchaseAuthority;

@property(nonatomic, copy) NSNumber* enterpriseActiveAccountCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

