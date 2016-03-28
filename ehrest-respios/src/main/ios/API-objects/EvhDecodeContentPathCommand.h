//
// EvhDecodeContentPathCommand.h
// generated at 2016-03-25 19:05:21 
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

