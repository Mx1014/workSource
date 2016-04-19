//
// EvhGetUserInfoByIdCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserInfoByIdCommand
//
@interface EvhGetUserInfoByIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* zlAppKey;

@property(nonatomic, copy) NSString* zlSignature;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* randomNum;

@property(nonatomic, copy) NSNumber* timeStamp;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

