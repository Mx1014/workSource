//
// EvhListYearPunchLogsCommandResponse.h
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

