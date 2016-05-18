//
// EvhEnterpriseApplyEntryDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseApplyEntryDTO
//
@interface EvhEnterpriseApplyEntryDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* sourceType;

@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* applyContact;

@property(nonatomic, copy) NSNumber* applyUserId;

@property(nonatomic, copy) NSString* applyUserName;

@property(nonatomic, copy) NSNumber* applyType;

@property(nonatomic, copy) NSNumber* sizeUnit;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* sourceName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

