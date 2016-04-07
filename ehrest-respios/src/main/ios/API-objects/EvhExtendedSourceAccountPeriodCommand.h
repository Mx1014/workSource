//
// EvhExtendedSourceAccountPeriodCommand.h
// generated at 2016-04-07 15:16:51 
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

