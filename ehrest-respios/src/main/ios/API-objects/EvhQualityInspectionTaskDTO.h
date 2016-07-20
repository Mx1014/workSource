//
// EvhQualityInspectionTaskDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityInspectionTaskRecordsDTO.h"
#import "EvhGroupUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskDTO
//
@interface EvhQualityInspectionTaskDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* standardId;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* taskName;

@property(nonatomic, copy) NSString* taskNumber;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* categoryName;

@property(nonatomic, copy) NSNumber* executiveGroupId;

@property(nonatomic, copy) NSNumber* executorId;

@property(nonatomic, copy) NSString* executorName;

@property(nonatomic, copy) NSNumber* operatorId;

@property(nonatomic, copy) NSString* operatorName;

@property(nonatomic, copy) NSNumber* executiveStartTime;

@property(nonatomic, copy) NSNumber* executiveExpireTime;

@property(nonatomic, copy) NSNumber* processExpireTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* result;

@property(nonatomic, copy) NSNumber* processResult;

@property(nonatomic, copy) NSNumber* reviewResult;

@property(nonatomic, copy) NSNumber* reviewerId;

@property(nonatomic, copy) NSString* reviewerName;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, strong) EvhQualityInspectionTaskRecordsDTO* record;

// item type EvhGroupUserDTO*
@property(nonatomic, strong) NSMutableArray* groupUsers;

@property(nonatomic, copy) NSNumber* taskFlag;

@property(nonatomic, copy) NSNumber* manualFlag;

@property(nonatomic, copy) NSString* standardDescription;

@property(nonatomic, copy) NSString* categoryDescription;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

