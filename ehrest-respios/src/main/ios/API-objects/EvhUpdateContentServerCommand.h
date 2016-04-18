//
// EvhUpdateContentServerCommand.h
// generated at 2016-04-18 14:48:51 
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

