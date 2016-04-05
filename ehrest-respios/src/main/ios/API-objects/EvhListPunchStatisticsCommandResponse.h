//
// EvhListPunchStatisticsCommandResponse.h
// generated at 2016-04-05 13:45:26 
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

