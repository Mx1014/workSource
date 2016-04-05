//
// EvhGetVideoConfAccountStatisticsCommand.h
// generated at 2016-04-05 13:45:24 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetVideoConfAccountStatisticsCommand
//
@interface EvhGetVideoConfAccountStatisticsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

