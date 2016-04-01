//
// EvhAppVersionCommand.h
// generated at 2016-03-31 20:15:22 
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

