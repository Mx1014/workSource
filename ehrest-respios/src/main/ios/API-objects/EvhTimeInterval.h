//
// EvhTimeInterval.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTimeInterval
//
@interface EvhTimeInterval
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* beginTime;

@property(nonatomic, copy) NSNumber* endTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

