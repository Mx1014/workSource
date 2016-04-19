//
// EvhAssignTopicScopeCommand.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssignTopicScopeCommand
//
@interface EvhAssignTopicScopeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* assignedFlag;

@property(nonatomic, copy) NSNumber* scopeCode;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* scopeIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

