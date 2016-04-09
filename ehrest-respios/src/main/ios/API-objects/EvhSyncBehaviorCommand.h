//
// EvhSyncBehaviorCommand.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncBehaviorCommand
//
@interface EvhSyncBehaviorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* collectTimeMs;

@property(nonatomic, copy) NSNumber* reportTimeMs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

