//
// EvhTimeInterval.h
// generated at 2016-04-08 20:09:23 
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

