//
// EvhBusinessVisibleScopeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessVisibleScopeDTO
//
@interface EvhBusinessVisibleScopeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* scopeType;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSString* scopeName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

