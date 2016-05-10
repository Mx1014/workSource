//
// EvhListStatisticsByCityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByCityDTO
//
@interface EvhListStatisticsByCityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* registerConut;

@property(nonatomic, copy) NSNumber* activeCount;

@property(nonatomic, copy) NSNumber* regRatio;

@property(nonatomic, copy) NSNumber* addressCount;

@property(nonatomic, copy) NSNumber* addrRatio;

@property(nonatomic, copy) NSNumber* cityActiveRatio;

@property(nonatomic, copy) NSNumber* cityRegRatio;

@property(nonatomic, copy) NSNumber* cityAddrRatio;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

