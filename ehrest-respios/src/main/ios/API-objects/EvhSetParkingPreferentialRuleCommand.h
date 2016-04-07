//
// EvhSetParkingPreferentialRuleCommand.h
// generated at 2016-04-07 10:47:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetParkingPreferentialRuleCommand
//
@interface EvhSetParkingPreferentialRuleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* endTime;

@property(nonatomic, copy) NSString* range;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

