//
// EvhUserInfo.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserCurrentEntity.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInfo
//
@interface EvhUserInfo
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* accountName;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* statusLine;

@property(nonatomic, copy) NSNumber* gender;

@property(nonatomic, copy) NSString* birthday;

@property(nonatomic, copy) NSNumber* homeTown;

@property(nonatomic, copy) NSString* hometownName;

@property(nonatomic, copy) NSString* company;

@property(nonatomic, copy) NSString* school;

@property(nonatomic, copy) NSString* occupation;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* regionId;

@property(nonatomic, copy) NSString* regionName;

@property(nonatomic, copy) NSString* regionPath;

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* namespaceUserToken;

@property(nonatomic, copy) NSString* uuid;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* phones;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* emails;

@property(nonatomic, copy) NSNumber* communityType;

// item type EvhUserCurrentEntity*
@property(nonatomic, strong) NSMutableArray* entityList;

@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

