//
// EvhPunchLogsDay.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchLogDTO.h"
#import "EvhPunchExceptionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchLogsDay
//
@interface EvhPunchLogsDay
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* punchDay;

@property(nonatomic, copy) NSNumber* punchStatus;

@property(nonatomic, copy) NSNumber* morningPunchStatus;

@property(nonatomic, copy) NSNumber* afternoonPunchStatus;

@property(nonatomic, copy) NSNumber* approvalStatus;

@property(nonatomic, copy) NSNumber* morningApprovalStatus;

@property(nonatomic, copy) NSNumber* afternoonApprovalStatus;

@property(nonatomic, copy) NSNumber* exceptionStatus;

@property(nonatomic, copy) NSNumber* punchTimesPerDay;

// item type EvhPunchLogDTO*
@property(nonatomic, strong) NSMutableArray* punchLogs;

// item type EvhPunchExceptionDTO*
@property(nonatomic, strong) NSMutableArray* punchExceptionDTOs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

