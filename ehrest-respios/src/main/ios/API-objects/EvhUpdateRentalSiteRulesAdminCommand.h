//
// EvhUpdateRentalSiteRulesAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRentalSiteRulesAdminCommand
//
@interface EvhUpdateRentalSiteRulesAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ruleId;

@property(nonatomic, copy) NSNumber* beginTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* originalPrice;

@property(nonatomic, copy) NSNumber* price;

@property(nonatomic, copy) NSNumber* halfsitePrice;

@property(nonatomic, copy) NSNumber* halfsiteOriginalPrice;

@property(nonatomic, copy) NSNumber* counts;

@property(nonatomic, copy) NSNumber* loopType;

@property(nonatomic, copy) NSNumber* beginDate;

@property(nonatomic, copy) NSNumber* endDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

