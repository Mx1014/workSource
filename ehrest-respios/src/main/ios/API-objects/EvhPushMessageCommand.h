//
// EvhPushMessageCommand.h
// generated at 2016-03-31 15:43:23 
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

