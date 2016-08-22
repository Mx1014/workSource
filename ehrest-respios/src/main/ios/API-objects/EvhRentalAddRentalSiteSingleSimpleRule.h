//
// EvhRentalAddRentalSiteSingleSimpleRule.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAddRentalSiteSingleSimpleRule
//
@interface EvhRentalAddRentalSiteSingleSimpleRule
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSNumber* beginDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSNumber* timeStep;

@property(nonatomic, copy) NSNumber* rentalStep;

@property(nonatomic, copy) NSNumber* beginTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* counts;

@property(nonatomic, copy) NSNumber* unit;

@property(nonatomic, copy) NSNumber* workdayPrice;

@property(nonatomic, copy) NSNumber* weekendPrice;

@property(nonatomic, copy) NSNumber* workdayAMPrice;

@property(nonatomic, copy) NSNumber* weekendAMPrice;

@property(nonatomic, copy) NSNumber* workdayPMPrice;

@property(nonatomic, copy) NSNumber* weekendPMPrice;

@property(nonatomic, copy) NSNumber* loopType;

@property(nonatomic, copy) NSNumber* status;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* choosen;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

