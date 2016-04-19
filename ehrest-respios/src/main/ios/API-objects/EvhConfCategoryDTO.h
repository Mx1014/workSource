//
// EvhConfCategoryDTO.h
// generated at 2016-04-19 12:41:54 
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

