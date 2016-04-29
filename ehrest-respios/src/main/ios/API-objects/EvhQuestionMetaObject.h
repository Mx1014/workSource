//
// EvhQuestionMetaObject.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQuestionMetaObject
//
@interface EvhQuestionMetaObject
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* resourceType;

@property(nonatomic, copy) NSNumber* resourceId;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* requestorUid;

@property(nonatomic, copy) NSString* requestorNickName;

@property(nonatomic, copy) NSString* requestorAvatar;

@property(nonatomic, copy) NSString* requestorAvatarUrl;

@property(nonatomic, copy) NSNumber* requestTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

