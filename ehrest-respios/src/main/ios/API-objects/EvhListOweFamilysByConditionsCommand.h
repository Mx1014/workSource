//
// EvhListOweFamilysByConditionsCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

