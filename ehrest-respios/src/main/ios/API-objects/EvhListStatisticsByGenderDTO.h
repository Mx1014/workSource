//
// EvhListStatisticsByGenderDTO.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByGenderDTO
//
@interface EvhListStatisticsByGenderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* gender;

@property(nonatomic, copy) NSNumber* registerConut;

@property(nonatomic, copy) NSNumber* addressCount;

@property(nonatomic, copy) NSNumber* addrRatio;

@property(nonatomic, copy) NSNumber* genderRegRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

