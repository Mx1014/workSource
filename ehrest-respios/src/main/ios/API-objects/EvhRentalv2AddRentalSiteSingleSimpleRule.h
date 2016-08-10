//
// EvhRentalv2AddRentalSiteSingleSimpleRule.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentConfigDTO.h"
#import "EvhTimeIntervalDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AddRentalSiteSingleSimpleRule
//
@interface EvhRentalv2AddRentalSiteSingleSimpleRule
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* beginTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* exclusiveFlag;

@property(nonatomic, copy) NSNumber* unit;

@property(nonatomic, copy) NSNumber* autoAssign;

@property(nonatomic, copy) NSNumber* multiUnit;

@property(nonatomic, copy) NSNumber* needPay;

@property(nonatomic, copy) NSNumber* multiTimeInterval;

// item type EvhAttachmentConfigDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSNumber* rentalEndTime;

@property(nonatomic, copy) NSNumber* rentalStartTime;

@property(nonatomic, copy) NSNumber* timeStep;

// item type EvhTimeIntervalDTO*
@property(nonatomic, strong) NSMutableArray* timeIntervals;

@property(nonatomic, copy) NSNumber* beginDate;

@property(nonatomic, copy) NSNumber* endDate;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* openWeekday;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* closeDates;

@property(nonatomic, copy) NSNumber* workdayPrice;

@property(nonatomic, copy) NSNumber* weekendPrice;

@property(nonatomic, copy) NSNumber* siteCounts;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* siteNumbers;

@property(nonatomic, copy) NSNumber* cancelTime;

@property(nonatomic, copy) NSNumber* refundFlag;

@property(nonatomic, copy) NSNumber* refundRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

