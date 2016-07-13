//
// EvhRentalSiteRulesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteRulesDTO
//
@interface EvhRentalSiteRulesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSNumber* amorpm;

@property(nonatomic, copy) NSNumber* beginTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* counts;

@property(nonatomic, copy) NSNumber* unit;

@property(nonatomic, copy) NSNumber* price;

@property(nonatomic, copy) NSNumber* originalPrice;

@property(nonatomic, copy) NSNumber* halfsitePrice;

@property(nonatomic, copy) NSNumber* halfsiteOriginalPrice;

@property(nonatomic, copy) NSNumber* exclusiveFlag;

@property(nonatomic, copy) NSNumber* autoAssign;

@property(nonatomic, copy) NSNumber* multiUnit;

@property(nonatomic, copy) NSNumber* multiTimeInterval;

@property(nonatomic, copy) NSNumber* rentalStep;

@property(nonatomic, copy) NSNumber* timeStep;

@property(nonatomic, copy) NSNumber* ruleDate;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* siteNumber;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

