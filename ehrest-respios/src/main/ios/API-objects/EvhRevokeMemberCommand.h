//
// EvhRevokeMemberCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRevokeMemberCommand
//
@interface EvhRevokeMemberCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* memberUid;

@property(nonatomic, copy) NSString* reason;

@property(nonatomic, copy) NSNumber* operatorRole;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

