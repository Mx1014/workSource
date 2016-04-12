//
// EvhListUserTaskCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserTaskCommand
//
@interface EvhListUserTaskCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* taskType;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* taskStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

