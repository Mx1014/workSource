//
// EvhDecodeContentPathCommand.h
// generated at 2016-04-18 14:48:51 
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

