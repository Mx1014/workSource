//
// EvhUpdatePushMessageCommand.h
// generated at 2016-03-31 11:07:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePushMessageCommand
//
@interface EvhUpdatePushMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* messageType;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* deviceType;

@property(nonatomic, copy) NSString* deviceTag;

@property(nonatomic, copy) NSString* appVersion;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

