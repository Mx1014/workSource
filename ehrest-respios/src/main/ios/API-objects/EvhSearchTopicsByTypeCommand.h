//
// EvhSearchTopicsByTypeCommand.h
// generated at 2016-03-25 17:08:10 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTopicsByTypeCommand
//
@interface EvhSearchTopicsByTypeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* taskStatus;

@property(nonatomic, copy) NSString* taskType;

@property(nonatomic, copy) NSString* flag;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

