//
// EvhRegionDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionDTO
//
@interface EvhRegionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* path;

@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSString* isoCode;

@property(nonatomic, copy) NSString* telCode;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* pinyinName;

@property(nonatomic, copy) NSString* pinyinPrefix;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

