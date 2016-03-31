//
// EvhConfCategoryDTO.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfCategoryDTO
//
@interface EvhConfCategoryDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* confCapacity;

@property(nonatomic, copy) NSNumber* singleAccountPrice;

@property(nonatomic, copy) NSNumber* multipleAccountThreshold;

@property(nonatomic, copy) NSNumber* multipleAccountPrice;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

