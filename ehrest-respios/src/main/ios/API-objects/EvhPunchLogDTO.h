//
// EvhPunchLogDTO.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchLogDTO
//
@interface EvhPunchLogDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* punchTime;

@property(nonatomic, copy) NSNumber* clockStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

