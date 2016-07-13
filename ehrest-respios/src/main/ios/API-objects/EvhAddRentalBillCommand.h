//
// EvhAddRentalBillCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhrentalBillRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalBillCommand
//
@interface EvhAddRentalBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalDate;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

// item type EvhrentalBillRuleDTO*
@property(nonatomic, strong) NSMutableArray* rules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

