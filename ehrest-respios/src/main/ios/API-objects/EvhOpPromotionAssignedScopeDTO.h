//
// EvhOpPromotionAssignedScopeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionAssignedScopeDTO
//
@interface EvhOpPromotionAssignedScopeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* promotionId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

