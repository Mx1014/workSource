//
// EvhListPropTopicStatisticCommandResponse.h
// generated at 2016-04-26 18:22:55 
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

