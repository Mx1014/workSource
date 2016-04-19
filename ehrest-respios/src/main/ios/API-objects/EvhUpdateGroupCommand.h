//
// EvhUpdateGroupCommand.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateGroupCommand
//
@interface EvhUpdateGroupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSNumber* visibilityScope;

@property(nonatomic, copy) NSNumber* visibilityScopeId;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* tag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

