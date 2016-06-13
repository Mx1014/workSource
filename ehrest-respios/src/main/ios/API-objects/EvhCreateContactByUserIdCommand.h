//
// EvhCreateContactByUserIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateContactByUserIdCommand
//
@interface EvhCreateContactByUserIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* applyGroup;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

