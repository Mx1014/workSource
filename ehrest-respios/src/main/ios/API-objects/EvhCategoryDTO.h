//
// EvhCategoryDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryDTO
//
@interface EvhCategoryDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* path;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* iconUri;

@property(nonatomic, copy) NSString* iconUrl;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* deleteTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

