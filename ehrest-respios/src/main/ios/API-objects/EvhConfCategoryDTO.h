//
// EvhConfCategoryDTO.h
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

@property(nonatomic, copy) NSNumber* minPeriod;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

