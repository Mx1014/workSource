//
// EvhActivityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityDTO
//
@interface EvhActivityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* activityId;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* postId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* stopTime;

@property(nonatomic, copy) NSString* location;

@property(nonatomic, copy) NSNumber* checkinFlag;

@property(nonatomic, copy) NSNumber* confirmFlag;

@property(nonatomic, copy) NSNumber* enrollUserCount;

@property(nonatomic, copy) NSNumber* enrollFamilyCount;

@property(nonatomic, copy) NSNumber* checkinUserCount;

@property(nonatomic, copy) NSNumber* checkinFamilyCount;

@property(nonatomic, copy) NSNumber* confirmUserCount;

@property(nonatomic, copy) NSNumber* confirmFamilyCount;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* posterUrl;

@property(nonatomic, copy) NSNumber* userActivityStatus;

@property(nonatomic, copy) NSNumber* processStatus;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSString* guest;

@property(nonatomic, copy) NSString* mediaUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

