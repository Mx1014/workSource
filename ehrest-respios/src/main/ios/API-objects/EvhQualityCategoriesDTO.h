//
// EvhQualityCategoriesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityCategoriesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityCategoriesDTO
//
@interface EvhQualityCategoriesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* path;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* score;

@property(nonatomic, copy) NSString* description_;

// item type EvhQualityCategoriesDTO*
@property(nonatomic, strong) NSMutableArray* childrens;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

