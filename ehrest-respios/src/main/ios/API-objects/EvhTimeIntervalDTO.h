//
// EvhTimeIntervalDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTimeIntervalDTO
//
@interface EvhTimeIntervalDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* beginTime;

@property(nonatomic, copy) NSNumber* endTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

