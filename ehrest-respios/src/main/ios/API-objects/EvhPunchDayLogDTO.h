//
// EvhPunchDayLogDTO.h
// generated at 2016-04-08 20:09:23 
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

