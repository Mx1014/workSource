//
// EvhDoorAccessAdminUpdateCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessAdminUpdateCommand
//
@interface EvhDoorAccessAdminUpdateCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

