//
// EvhTimeRangeDTO.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTimeRangeDTO
//
@interface EvhTimeRangeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* duration;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

