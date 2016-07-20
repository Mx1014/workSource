//
// EvhrentalBillRuleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhrentalBillRuleDTO
//
@interface EvhrentalBillRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ruleId;

@property(nonatomic, copy) NSNumber* rentalCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

