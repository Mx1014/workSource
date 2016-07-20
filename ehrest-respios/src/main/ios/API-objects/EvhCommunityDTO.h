//
// EvhCommunityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityDTO
//
@interface EvhCommunityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* aliasName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* zipcode;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* detailDescription;

@property(nonatomic, copy) NSString* aptSegment1;

@property(nonatomic, copy) NSString* aptSegment2;

@property(nonatomic, copy) NSString* aptSegment3;

@property(nonatomic, copy) NSString* aptSeg1Sample;

@property(nonatomic, copy) NSString* aptSeg2Sample;

@property(nonatomic, copy) NSString* aptSeg3Sample;

@property(nonatomic, copy) NSNumber* aptCount;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* deleteTime;

@property(nonatomic, copy) NSNumber* requestStatus;

@property(nonatomic, copy) NSNumber* communityType;

@property(nonatomic, copy) NSNumber* defaultForumId;

@property(nonatomic, copy) NSNumber* feedbackForumId;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* areaSize;

// item type EvhCommunityGeoPointDTO*
@property(nonatomic, strong) NSMutableArray* geoPointList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

