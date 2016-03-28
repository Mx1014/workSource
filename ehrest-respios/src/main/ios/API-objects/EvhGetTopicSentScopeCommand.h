//
// EvhGetTopicSentScopeCommand.h
// generated at 2016-03-28 15:56:08 
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

