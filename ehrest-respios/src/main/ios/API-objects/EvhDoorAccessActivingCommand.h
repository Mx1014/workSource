//
// EvhDoorAccessActivingCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessActivingCommand
//
@interface EvhDoorAccessActivingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSString* firwareVer;

@property(nonatomic, copy) NSString* rsaAclinkPub;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

