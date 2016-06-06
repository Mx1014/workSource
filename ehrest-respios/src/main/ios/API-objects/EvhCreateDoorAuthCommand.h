//
// EvhCreateDoorAuthCommand.h
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

@property(nonatomic, copy) NSNumber* rightOpen;

@property(nonatomic, copy) NSNumber* rightVisitor;

@property(nonatomic, copy) NSNumber* rightRemote;

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

