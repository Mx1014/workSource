//
// EvhPunchLogDTO.h
// generated at 2016-03-31 10:18:20 
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

