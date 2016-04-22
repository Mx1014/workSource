//
// EvhUserCurrentEntity.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserCurrentEntity
//
@interface EvhUserCurrentEntity
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* entityType;

@property(nonatomic, copy) NSString* entityId;

@property(nonatomic, copy) NSString* entityName;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* directlyEnterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

