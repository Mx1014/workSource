//
// EvhOpPromotionActivityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOpPromotionAssignedScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionActivityDTO
//
@interface EvhOpPromotionActivityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* pushCount;

@property(nonatomic, copy) NSString* policyData;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* pushMessage;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* processStatus;

@property(nonatomic, copy) NSNumber* policyType;

@property(nonatomic, copy) NSString* iconUri;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* processCount;

// item type EvhOpPromotionAssignedScopeDTO*
@property(nonatomic, strong) NSMutableArray* assignedScopes;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

