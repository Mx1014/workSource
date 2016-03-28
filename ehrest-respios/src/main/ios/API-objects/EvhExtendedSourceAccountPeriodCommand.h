//
// EvhExtendedSourceAccountPeriodCommand.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExtendedSourceAccountPeriodCommand
//
@interface EvhExtendedSourceAccountPeriodCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* sourceAccountId;

@property(nonatomic, copy) NSNumber* validDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

