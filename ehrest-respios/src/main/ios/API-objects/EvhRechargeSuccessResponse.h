//
// EvhRechargeSuccessResponse.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeSuccessResponse
//
@interface EvhRechargeSuccessResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSNumber* validityPeriod;

@property(nonatomic, copy) NSNumber* payStatus;

@property(nonatomic, copy) NSNumber* rechargeStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

