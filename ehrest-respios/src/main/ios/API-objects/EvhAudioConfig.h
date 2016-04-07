//
// EvhAudioConfig.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAudioConfig
//
@interface EvhAudioConfig
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* format;

@property(nonatomic, copy) NSNumber* bit;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

