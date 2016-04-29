//
// EvhListFollowersCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFollowersCommand
//
@interface EvhListFollowersCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* pageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

