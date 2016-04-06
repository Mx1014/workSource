//
// EvhExtendedVideoConfAccountPeriodCommand.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExtendedVideoConfAccountPeriodCommand
//
@interface EvhExtendedVideoConfAccountPeriodCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accountId;

@property(nonatomic, copy) NSNumber* validDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

