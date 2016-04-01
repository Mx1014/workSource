//
// EvhEntityDescriptor.h
// generated at 2016-03-31 20:15:32 
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

