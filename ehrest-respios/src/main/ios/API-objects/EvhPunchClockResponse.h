//
// EvhPunchClockResponse.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchClockResponse
//
@interface EvhPunchClockResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* punchCode;

@property(nonatomic, copy) NSString* punchTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

