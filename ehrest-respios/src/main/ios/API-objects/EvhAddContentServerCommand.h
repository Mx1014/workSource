//
// EvhAddContentServerCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddContentServerCommand
//
@interface EvhAddContentServerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* privateAddress;

@property(nonatomic, copy) NSNumber* privatePort;

@property(nonatomic, copy) NSString* publicAddress;

@property(nonatomic, copy) NSNumber* publicPort;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

