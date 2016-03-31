//
// EvhPunchLogDTO.h
// generated at 2016-03-28 15:56:08 
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

