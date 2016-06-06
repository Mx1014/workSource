//
// EvhActivityPostCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityPostCommand
//
@interface EvhActivityPostCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* location;

@property(nonatomic, copy) NSString* contactPerson;

@property(nonatomic, copy) NSString* contactNumber;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* endTime;

@property(nonatomic, copy) NSNumber* checkinFlag;

@property(nonatomic, copy) NSNumber* confirmFlag;

@property(nonatomic, copy) NSNumber* maxAttendeeCount;

@property(nonatomic, copy) NSNumber* creatorFamilyId;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* changeVersion;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* posterUri;

@property(nonatomic, copy) NSString* guest;

@property(nonatomic, copy) NSString* mediaUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

