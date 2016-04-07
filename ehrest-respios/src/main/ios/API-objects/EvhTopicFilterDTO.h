//
// EvhTopicFilterDTO.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTopicFilterDTO
//
@interface EvhTopicFilterDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* actionUrl;

@property(nonatomic, copy) NSNumber* isDefault;

@property(nonatomic, copy) NSNumber* isLeaf;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

