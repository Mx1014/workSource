//
// EvhListOweFamilysByConditionsCommand.h
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

