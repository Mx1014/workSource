//
// EvhCreateOpPromotionCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOpPromotionAssignedScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOpPromotionCommand
//
@interface EvhCreateOpPromotionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* pushMessage;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* policyType;

@property(nonatomic, copy) NSString* policyData;

@property(nonatomic, copy) NSNumber* endTime;

// item type EvhOpPromotionAssignedScopeDTO*
@property(nonatomic, strong) NSMutableArray* assignedScopes;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

