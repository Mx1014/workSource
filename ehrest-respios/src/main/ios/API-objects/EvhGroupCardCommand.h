//
// EvhGroupCardCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupCardCommand
//
@interface EvhGroupCardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* createTime;

@property(nonatomic, copy) NSNumber* privateFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

