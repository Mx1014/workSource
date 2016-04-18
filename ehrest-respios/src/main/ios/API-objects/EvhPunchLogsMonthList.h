//
// EvhPunchLogsMonthList.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchLogsDay.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchLogsMonthList
//
@interface EvhPunchLogsMonthList
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* punchMonth;

// item type EvhPunchLogsDay*
@property(nonatomic, strong) NSMutableArray* punchLogsDayList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

