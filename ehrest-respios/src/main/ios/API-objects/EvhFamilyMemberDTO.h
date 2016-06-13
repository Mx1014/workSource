//
// EvhFamilyMemberDTO.h
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

