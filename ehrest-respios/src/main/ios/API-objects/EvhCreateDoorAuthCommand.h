//
// EvhCreateDoorAuthCommand.h
// generated at 2016-03-28 15:56:07 
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

