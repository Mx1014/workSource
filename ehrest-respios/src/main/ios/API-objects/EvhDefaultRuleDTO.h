//
// EvhDefaultRuleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentDTO.h"
#import "EvhTimeIntervalDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDefaultRuleDTO
//
@interface EvhDefaultRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* launchPadItemId;

@property(nonatomic, copy) NSNumber* exclusiveFlag;

@property(nonatomic, copy) NSNumber* unit;

@property(nonatomic, copy) NSNumber* autoAssign;

@property(nonatomic, copy) NSNumber* multiUnit;

@property(nonatomic, copy) NSNumber* needPay;

@property(nonatomic, copy) NSNumber* multiTimeInterval;

// item type EvhAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSNumber* rentalEndTime;

@property(nonatomic, copy) NSNumber* rentalStartTime;

@property(nonatomic, copy) NSNumber* rentalStep;

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

@property(nonatomic, copy) NSNumber* cancelTime;

@property(nonatomic, copy) NSNumber* refundFlag;

@property(nonatomic, copy) NSNumber* refundRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

