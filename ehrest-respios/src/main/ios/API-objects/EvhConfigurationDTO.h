//
// EvhConfigurationDTO.h
// generated at 2016-04-05 13:45:24 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfigurationDTO
//
@interface EvhConfigurationDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* value;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

