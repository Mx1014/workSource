//
// EvhEntityDescriptor.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEntityDescriptor
//
@interface EvhEntityDescriptor
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* entityType;

@property(nonatomic, copy) NSNumber* entityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

