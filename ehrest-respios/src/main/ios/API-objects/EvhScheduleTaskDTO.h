//
// EvhScheduleTaskDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleTaskDTO
//
@interface EvhScheduleTaskDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* progressData;

@property(nonatomic, copy) NSString* resourceType;

@property(nonatomic, copy) NSNumber* resourceId;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* progress;

@property(nonatomic, copy) NSNumber* processCount;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

