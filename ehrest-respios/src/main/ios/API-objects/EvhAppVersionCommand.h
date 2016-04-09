//
// EvhAppVersionCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppVersionCommand
//
@interface EvhAppVersionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* platformType;

@property(nonatomic, copy) NSString* currVersionCode;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

