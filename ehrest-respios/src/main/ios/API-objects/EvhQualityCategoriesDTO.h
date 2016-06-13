//
// EvhQualityCategoriesDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

