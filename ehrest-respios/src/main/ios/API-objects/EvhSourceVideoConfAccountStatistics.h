//
// EvhSourceVideoConfAccountStatistics.h
// generated at 2016-03-31 19:08:53 
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

