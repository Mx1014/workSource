//
// EvhBusinessVisibleScopeDTO.h
// generated at 2016-04-22 13:56:45 
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

