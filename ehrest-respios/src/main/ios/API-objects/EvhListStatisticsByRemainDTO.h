//
// EvhListStatisticsByRemainDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByRemainDTO
//
@interface EvhListStatisticsByRemainDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* theNewUserCount;

@property(nonatomic, copy) NSNumber* oneDayRemainRatio;

@property(nonatomic, copy) NSNumber* twoDaysRemainRatio;

@property(nonatomic, copy) NSNumber* threeDaysRemainRatio;

@property(nonatomic, copy) NSNumber* fourDaysRemainRatio;

@property(nonatomic, copy) NSNumber* fiveDaysRemainRatio;

@property(nonatomic, copy) NSNumber* sixDaysRemainRatio;

@property(nonatomic, copy) NSNumber* sevenDaysRemainRatio;

@property(nonatomic, copy) NSNumber* fortnightRemainRatio;

@property(nonatomic, copy) NSNumber* thirtyDaysRemainRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

