//
// EvhScheduleTaskLogDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleTaskLogDTO
//
@interface EvhScheduleTaskLogDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSString* resourceType;

@property(nonatomic, copy) NSNumber* resourceId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSString* resultData;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

