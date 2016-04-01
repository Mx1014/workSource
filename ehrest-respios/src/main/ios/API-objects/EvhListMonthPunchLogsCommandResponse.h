//
// EvhListMonthPunchLogsCommandResponse.h
// generated at 2016-03-31 20:15:28 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchLogsMonthList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListMonthPunchLogsCommandResponse
//
@interface EvhListMonthPunchLogsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* punchYear;

// item type EvhPunchLogsMonthList*
@property(nonatomic, strong) NSMutableArray* punchLogsMonthList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

