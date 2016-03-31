//
// EvhGetPmPayStatisticsCommand.h
// generated at 2016-03-31 13:49:13 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPmPayStatisticsCommand
//
@interface EvhGetPmPayStatisticsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

