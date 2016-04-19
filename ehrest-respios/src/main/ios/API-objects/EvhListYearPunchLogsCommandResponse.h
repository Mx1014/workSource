//
// EvhListYearPunchLogsCommandResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

