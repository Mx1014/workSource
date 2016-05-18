//
// EvhQualityInspectionTaskRecordsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityInspectionTaskAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskRecordsDTO
//
@interface EvhQualityInspectionTaskRecordsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* operatorId;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* targetName;

@property(nonatomic, copy) NSNumber* processType;

@property(nonatomic, copy) NSNumber* processEndTime;

@property(nonatomic, copy) NSNumber* processResult;

@property(nonatomic, copy) NSString* processMessage;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhQualityInspectionTaskAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

