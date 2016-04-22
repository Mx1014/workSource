//
// EvhFactorsDTO.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFactorsDTO
//
@interface EvhFactorsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* categoryName;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSNumber* weight;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

