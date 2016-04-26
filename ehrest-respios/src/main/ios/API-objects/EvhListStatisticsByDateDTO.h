//
// EvhListStatisticsByDateDTO.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByDateDTO
//
@interface EvhListStatisticsByDateDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* registerConut;

@property(nonatomic, copy) NSNumber* activeCount;

@property(nonatomic, copy) NSNumber* regRatio;

@property(nonatomic, copy) NSNumber* addressCount;

@property(nonatomic, copy) NSNumber* totalRegisterCount;

@property(nonatomic, copy) NSNumber* addrRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

