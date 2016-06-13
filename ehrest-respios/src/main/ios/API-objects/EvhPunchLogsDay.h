//
// EvhPunchLogsDay.h
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

