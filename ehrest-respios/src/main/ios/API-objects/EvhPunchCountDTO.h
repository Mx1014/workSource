//
// EvhPunchCountDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchCountDTO
//
@interface EvhPunchCountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSNumber* workDayCount;

@property(nonatomic, copy) NSNumber* workCount;

@property(nonatomic, copy) NSNumber* belateCount;

@property(nonatomic, copy) NSNumber* leaveEarlyCount;

@property(nonatomic, copy) NSNumber* unPunchCount;

@property(nonatomic, copy) NSNumber* blandleCount;

@property(nonatomic, copy) NSNumber* absenceCount;

@property(nonatomic, copy) NSNumber* sickCount;

@property(nonatomic, copy) NSNumber* exchangeCount;

@property(nonatomic, copy) NSNumber* outworkCount;

@property(nonatomic, copy) NSNumber* overTimeSum;

@property(nonatomic, copy) NSNumber* punchTimesPerDay;

@property(nonatomic, copy) NSString* userEnterpriseGroup;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

