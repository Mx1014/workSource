//
// EvhGetUserInfoByIdCommand.h
// generated at 2016-04-22 13:56:45 
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

