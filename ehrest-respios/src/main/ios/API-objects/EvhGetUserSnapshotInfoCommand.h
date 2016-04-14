//
// EvhGetUserSnapshotInfoCommand.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserSnapshotInfoCommand
//
@interface EvhGetUserSnapshotInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSString* uuid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

