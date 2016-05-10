//
// EvhDecodeContentPathCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDecodeContentPathCommand
//
@interface EvhDecodeContentPathCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* path;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

