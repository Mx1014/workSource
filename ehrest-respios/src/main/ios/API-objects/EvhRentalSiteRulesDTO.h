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

@property(nonatomic, copy) NSNumber* rentalStep;

@property(nonatomic, copy) NSNumber* timeStep;

@property(nonatomic, copy) NSNumber* ruleDate;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

