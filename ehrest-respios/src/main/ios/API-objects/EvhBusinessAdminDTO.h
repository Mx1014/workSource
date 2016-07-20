//
// EvhBusinessAdminDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCategoryDTO.h"
#import "EvhBusinessAssignedScopeDTO.h"
#import "EvhBusinessPromoteScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessAdminDTO
//
@interface EvhBusinessAdminDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSString* targetId;

@property(nonatomic, copy) NSNumber* bizOwnerUid;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* logoUri;

@property(nonatomic, copy) NSString* logoUrl;

@property(nonatomic, copy) NSString* url;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* geohash;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* description_;

// item type EvhCategoryDTO*
@property(nonatomic, strong) NSMutableArray* categories;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* deleteTime;

@property(nonatomic, copy) NSNumber* recommendStatus;

@property(nonatomic, copy) NSNumber* favoriteStatus;

@property(nonatomic, copy) NSNumber* distance;

// item type EvhBusinessAssignedScopeDTO*
@property(nonatomic, strong) NSMutableArray* assignedScopes;

@property(nonatomic, copy) NSNumber* scaleType;

@property(nonatomic, copy) NSNumber* promoteFlag;

// item type EvhBusinessPromoteScopeDTO*
@property(nonatomic, strong) NSMutableArray* promoteScopes;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

