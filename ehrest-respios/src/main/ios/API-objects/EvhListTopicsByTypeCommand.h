//
// EvhListTopicsByTypeCommand.h
// generated at 2016-03-25 11:43:33 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTopicsByTypeCommand
//
@interface EvhListTopicsByTypeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* taskType;

@property(nonatomic, copy) NSNumber* taskStatus;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* option;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

