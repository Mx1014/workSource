//
// EvhListQualityInspectionTasksCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityInspectionTasksCommand
//
@interface EvhListQualityInspectionTasksCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* taskType;

@property(nonatomic, copy) NSNumber* executeFlag;

@property(nonatomic, copy) NSNumber* isReview;

@property(nonatomic, copy) NSNumber* startDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* executeStatus;

@property(nonatomic, copy) NSNumber* reviewStatus;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

