//
// EvhPushMessageCommand.h
// generated at 2016-04-26 18:22:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPushMessageCommand
//
@interface EvhPushMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* deviceId;

@property(nonatomic, copy) NSString* message;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

