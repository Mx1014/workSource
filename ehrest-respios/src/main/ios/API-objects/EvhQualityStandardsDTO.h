//
// EvhQualityStandardsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRepeatSettingsDTO.h"
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityStandardsDTO
//
@interface EvhQualityStandardsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* standardNumber;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* categoryName;

@property(nonatomic, strong) EvhRepeatSettingsDTO* repeat;

// item type EvhStandardGroupDTO*
@property(nonatomic, strong) NSMutableArray* executiveGroup;

// item type EvhStandardGroupDTO*
@property(nonatomic, strong) NSMutableArray* reviewGroup;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* deleterUid;

@property(nonatomic, copy) NSNumber* deleteTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

