//
// EvhCreateDoorAuthCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:52 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDoorAuthCommand
//
@interface EvhCreateDoorAuthCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* approveUserId;

@property(nonatomic, copy) NSNumber* authType;

@property(nonatomic, copy) NSNumber* validFromMs;

@property(nonatomic, copy) NSNumber* validEndMs;

@property(nonatomic, copy) NSString* organization;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

