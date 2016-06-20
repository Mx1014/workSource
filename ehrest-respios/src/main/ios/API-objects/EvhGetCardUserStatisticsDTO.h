//
// EvhGetCardUserStatisticsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCardUserStatisticsDTO
//
@interface EvhGetCardUserStatisticsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* totalCount;

@property(nonatomic, copy) NSNumber* cardUserCount;

@property(nonatomic, copy) NSNumber* normalUserCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

