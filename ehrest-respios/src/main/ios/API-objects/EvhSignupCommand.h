//
// EvhSignupCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSignupCommand
//
@interface EvhSignupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* type;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSNumber* channel_id;

@property(nonatomic, copy) NSNumber* ifExistsThenOverride;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

