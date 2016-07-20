//
// EvhOrganizationTaskDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationTaskDTO
//
@interface EvhOrganizationTaskDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* organizationType;

@property(nonatomic, copy) NSString* applyEntityType;

@property(nonatomic, copy) NSNumber* applyEntityId;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* taskType;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* taskStatus;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSNumber* operateTime;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* unprocessedTime;

@property(nonatomic, copy) NSNumber* processingTime;

@property(nonatomic, copy) NSNumber* processedTime;

@property(nonatomic, copy) NSString* taskCategory;

@property(nonatomic, copy) NSString* option;

@property(nonatomic, copy) NSString* entrancePrivilege;

@property(nonatomic, copy) NSString* targetName;

@property(nonatomic, copy) NSString* targetToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

