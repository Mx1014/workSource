//
// EvhVerifyPurchaseAuthorityResponse.h
// generated at 2016-04-22 13:56:45 
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

@property(nonatomic, copy) NSNumber* enterpriseAccountCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

