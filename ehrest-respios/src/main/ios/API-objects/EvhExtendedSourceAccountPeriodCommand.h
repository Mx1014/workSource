//
// EvhExtendedSourceAccountPeriodCommand.h
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

