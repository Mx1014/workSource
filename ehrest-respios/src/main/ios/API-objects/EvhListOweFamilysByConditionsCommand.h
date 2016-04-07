//
// EvhListOweFamilysByConditionsCommand.h
// generated at 2016-04-07 17:33:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOweFamilysByConditionsCommand
//
@interface EvhListOweFamilysByConditionsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* lastPayDate;

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

