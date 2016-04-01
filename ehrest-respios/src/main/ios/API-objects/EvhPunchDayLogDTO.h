//
// EvhPunchDayLogDTO.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchDayLogDTO
//
@interface EvhPunchDayLogDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* punchTime;

@property(nonatomic, copy) NSNumber* workTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* approvalStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

