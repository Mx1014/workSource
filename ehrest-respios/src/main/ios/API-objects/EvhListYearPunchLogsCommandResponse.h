//
// EvhListYearPunchLogsCommandResponse.h
// generated at 2016-03-25 09:26:39 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchLogsMonthList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListYearPunchLogsCommandResponse
//
@interface EvhListYearPunchLogsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* punchYear;

// item type EvhPunchLogsMonthList*
@property(nonatomic, strong) NSMutableArray* punchLogsMonthList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

