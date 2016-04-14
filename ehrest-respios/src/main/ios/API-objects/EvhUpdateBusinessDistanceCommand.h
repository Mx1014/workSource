//
// EvhUpdateBusinessDistanceCommand.h
// generated at 2016-04-12 19:00:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateBusinessDistanceCommand
//
@interface EvhUpdateBusinessDistanceCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* visibleDistance;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

