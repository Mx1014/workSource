//
// EvhPushJumpObject.h
// generated at 2016-04-07 17:33:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPushJumpObject
//
@interface EvhPushJumpObject
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* jump;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

