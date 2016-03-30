//
// EvhQuestionMetaObject.h
// generated at 2016-03-30 10:13:09 
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

