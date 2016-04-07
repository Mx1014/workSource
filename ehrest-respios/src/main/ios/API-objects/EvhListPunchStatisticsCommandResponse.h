//
// EvhListPunchStatisticsCommandResponse.h
// generated at 2016-04-07 15:16:52 
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

