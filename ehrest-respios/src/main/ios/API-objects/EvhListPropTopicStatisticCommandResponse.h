//
// EvhListPropTopicStatisticCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropTopicStatisticCommandResponse
//
@interface EvhListPropTopicStatisticCommandResponse
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* todayList;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* yesterdayList;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* weekList;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* monthList;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* dateList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

