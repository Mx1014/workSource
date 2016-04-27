//
// EvhTopicFilterDTO.h
// generated at 2016-04-26 18:22:55 
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

@property(nonatomic, copy) NSNumber* defaultFlag;

@property(nonatomic, copy) NSNumber* leafFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

