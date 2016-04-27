//
// EvhParkingPreferentialRuleDTO.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingPreferentialRuleDTO
//
@interface EvhParkingPreferentialRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* endTime;

@property(nonatomic, copy) NSString* range;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

