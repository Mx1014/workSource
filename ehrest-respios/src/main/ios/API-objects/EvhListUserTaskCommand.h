//
// EvhListUserTaskCommand.h
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

