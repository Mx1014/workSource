//
// EvhGetVideoConfAccountStatisticsCommand.h
// generated at 2016-04-22 13:56:45 
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

