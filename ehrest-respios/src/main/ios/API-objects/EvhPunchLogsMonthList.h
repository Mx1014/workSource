//
// EvhPunchLogsMonthList.h
// generated at 2016-03-31 19:08:53 
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

