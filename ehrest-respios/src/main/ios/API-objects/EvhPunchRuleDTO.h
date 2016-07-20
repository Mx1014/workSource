//
// EvhPunchRuleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchRuleDTO
//
@interface EvhPunchRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* EnterpriseId;

@property(nonatomic, copy) NSNumber* startEarlyTime;

@property(nonatomic, copy) NSNumber* startLateTime;

@property(nonatomic, copy) NSNumber* endEarlyTime;

@property(nonatomic, copy) NSNumber* noonLeaveTime;

@property(nonatomic, copy) NSNumber* afternoonArriveTime;

@property(nonatomic, copy) NSNumber* punchTimesPerDay;

// item type EvhPunchGeoPointDTO*
@property(nonatomic, strong) NSMutableArray* punchGeoPoints;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

