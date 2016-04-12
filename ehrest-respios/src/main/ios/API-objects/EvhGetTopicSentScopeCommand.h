//
// EvhGetTopicSentScopeCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetTopicSentScopeCommand
//
@interface EvhGetTopicSentScopeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSString* scopeType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

