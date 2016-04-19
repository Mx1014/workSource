//
// EvhListPunchStatisticsCommandResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchStatisticsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchStatisticsCommandResponse
//
@interface EvhListPunchStatisticsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhPunchStatisticsDTO*
@property(nonatomic, strong) NSMutableArray* punchList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

