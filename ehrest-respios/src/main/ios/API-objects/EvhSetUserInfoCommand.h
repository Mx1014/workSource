//
// EvhSetUserInfoCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetUserInfoCommand
//
@interface EvhSetUserInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* statusLine;

@property(nonatomic, copy) NSNumber* gender;

@property(nonatomic, copy) NSString* birthday;

@property(nonatomic, copy) NSNumber* homeTown;

@property(nonatomic, copy) NSString* company;

@property(nonatomic, copy) NSString* school;

@property(nonatomic, copy) NSString* occupation;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

