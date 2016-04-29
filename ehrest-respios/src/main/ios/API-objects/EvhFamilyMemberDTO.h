//
// EvhFamilyMemberDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
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
// EvhFamilyMemberDTO
//
@interface EvhFamilyMemberDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSNumber* memberUid;

@property(nonatomic, copy) NSString* memberName;

@property(nonatomic, copy) NSString* memberAvatarUri;

@property(nonatomic, copy) NSString* memberAvatarUrl;

@property(nonatomic, copy) NSString* cellPhone;

@property(nonatomic, copy) NSString* statusLine;

@property(nonatomic, copy) NSNumber* gender;

@property(nonatomic, copy) NSString* birthday;

@property(nonatomic, copy) NSString* occupation;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

