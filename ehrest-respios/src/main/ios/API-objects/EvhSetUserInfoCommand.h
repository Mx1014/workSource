//
// EvhSetUserInfoCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

