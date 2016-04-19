//
// EvhUserCurrentEntity.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

