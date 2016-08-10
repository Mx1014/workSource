//
// EvhRentalBillRuleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBillRuleDTO
//
@interface EvhRentalBillRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ruleId;

@property(nonatomic, copy) NSNumber* rentalCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

