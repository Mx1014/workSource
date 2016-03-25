//
// EvhPushJumpObject.h
// generated at 2016-03-25 11:43:33 
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

