//
// EvhPropAptStatisticDTO.h
// generated at 2016-04-05 13:45:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropAptStatisticDTO
//
@interface EvhPropAptStatisticDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* aptCount;

@property(nonatomic, copy) NSNumber* familyCount;

@property(nonatomic, copy) NSNumber* userCount;

@property(nonatomic, copy) NSNumber* liveCount;

@property(nonatomic, copy) NSNumber* rentCount;

@property(nonatomic, copy) NSNumber* freeCount;

@property(nonatomic, copy) NSNumber* decorateCount;

@property(nonatomic, copy) NSNumber* unsaleCount;

@property(nonatomic, copy) NSNumber* defaultCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

