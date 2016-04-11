//
// EvhSourceVideoConfAccountStatistics.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSourceVideoConfAccountStatistics
//
@interface EvhSourceVideoConfAccountStatistics
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* monitoringPoints;

@property(nonatomic, copy) NSNumber* ratio;

@property(nonatomic, copy) NSNumber* warningLine;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

