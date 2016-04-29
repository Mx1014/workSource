//
// EvhListPropTopicStatisticCommandResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

