//
// EvhCreateGroupCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateGroupCommand
//
@interface EvhCreateGroupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSNumber* visibilityScope;

@property(nonatomic, copy) NSNumber* visibilityScopeId;

@property(nonatomic, copy) NSNumber* privateFlag;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* postFlag;

@property(nonatomic, copy) NSNumber* visibleRegionType;

@property(nonatomic, copy) NSNumber* visibleRegionId;

@property(nonatomic, copy) NSString* explicitRegionDescriptorsJson;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

