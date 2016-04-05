//
// EvhUserCurrentEntity.h
// generated at 2016-04-05 13:45:26 
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

