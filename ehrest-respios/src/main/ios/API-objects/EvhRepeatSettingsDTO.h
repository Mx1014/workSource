//
// EvhRepeatSettingsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRepeatSettingsDTO
//
@interface EvhRepeatSettingsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* foreverFlag;

@property(nonatomic, copy) NSNumber* repeatCount;

@property(nonatomic, copy) NSNumber* startDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSString* timeRanges;

@property(nonatomic, copy) NSNumber* repeatType;

@property(nonatomic, copy) NSNumber* repeatInterval;

@property(nonatomic, copy) NSNumber* workDayFlag;

@property(nonatomic, copy) NSString* expression;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

