//
// EvhIgnoreRecommendCommand.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhIgnoreRecommandItem.h"

///////////////////////////////////////////////////////////////////////////////
// EvhIgnoreRecommendCommand
//
@interface EvhIgnoreRecommendCommand
    : NSObject<EvhJsonSerializable>


// item type EvhIgnoreRecommandItem*
@property(nonatomic, strong) NSMutableArray* recommendItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

