//
// EvhUpdateContentServerCommand.h
// generated at 2016-03-25 11:43:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAddContentServerCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateContentServerCommand
//
@interface EvhUpdateContentServerCommand
    : EvhAddContentServerCommand


@property(nonatomic, copy) NSNumber* serverId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

