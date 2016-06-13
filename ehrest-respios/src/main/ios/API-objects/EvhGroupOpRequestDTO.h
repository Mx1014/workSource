//
// EvhGroupOpRequestDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupOpRequestDTO
//
@interface EvhGroupOpRequestDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSNumber* requestorUid;

@property(nonatomic, copy) NSString* requestorName;

@property(nonatomic, copy) NSString* requestorAvatar;

@property(nonatomic, copy) NSString* requestorAvatarUrl;

@property(nonatomic, copy) NSString* requestorComment;

@property(nonatomic, copy) NSNumber* operationType;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSString* processMessage;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* processTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

