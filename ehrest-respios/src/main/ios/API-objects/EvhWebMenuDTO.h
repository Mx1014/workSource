//
// EvhWebMenuDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhWebMenuDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWebMenuDTO
//
@interface EvhWebMenuDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* iconUrl;

@property(nonatomic, copy) NSString* dataType;

@property(nonatomic, copy) NSNumber* leafFlag;

@property(nonatomic, copy) NSNumber* parentId;

// item type EvhWebMenuDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

