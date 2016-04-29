//
// EvhCreatePushMessageCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatePushMessageCommand
//
@interface EvhCreatePushMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* messageType;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSString* deviceType;

@property(nonatomic, copy) NSString* deviceTag;

@property(nonatomic, copy) NSString* appVersion;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

