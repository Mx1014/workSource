//
// EvhListStatisticsByCityActiveDTO.h
// generated at 2016-03-31 11:07:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByCityActiveDTO
//
@interface EvhListStatisticsByCityActiveDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSNumber* activeCount;

@property(nonatomic, copy) NSString* cityName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

