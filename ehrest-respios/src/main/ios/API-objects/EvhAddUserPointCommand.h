//
// EvhAddUserPointCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddUserPointCommand
//
@interface EvhAddUserPointCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSString* pointType;

@property(nonatomic, copy) NSNumber* point;

@property(nonatomic, copy) NSNumber* uid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

