//
// EvhCommunityDTO.m
//
#import "EvhCommunityDTO.h"
#import "EvhCommunityGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityDTO
//

@implementation EvhCommunityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityDTO* obj = [EvhCommunityDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _geoPointList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.aliasName)
        [jsonObject setObject: self.aliasName forKey: @"aliasName"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.zipcode)
        [jsonObject setObject: self.zipcode forKey: @"zipcode"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.detailDescription)
        [jsonObject setObject: self.detailDescription forKey: @"detailDescription"];
    if(self.aptSegment1)
        [jsonObject setObject: self.aptSegment1 forKey: @"aptSegment1"];
    if(self.aptSegment2)
        [jsonObject setObject: self.aptSegment2 forKey: @"aptSegment2"];
    if(self.aptSegment3)
        [jsonObject setObject: self.aptSegment3 forKey: @"aptSegment3"];
    if(self.aptSeg1Sample)
        [jsonObject setObject: self.aptSeg1Sample forKey: @"aptSeg1Sample"];
    if(self.aptSeg2Sample)
        [jsonObject setObject: self.aptSeg2Sample forKey: @"aptSeg2Sample"];
    if(self.aptSeg3Sample)
        [jsonObject setObject: self.aptSeg3Sample forKey: @"aptSeg3Sample"];
    if(self.aptCount)
        [jsonObject setObject: self.aptCount forKey: @"aptCount"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.deleteTime)
        [jsonObject setObject: self.deleteTime forKey: @"deleteTime"];
    if(self.requestStatus)
        [jsonObject setObject: self.requestStatus forKey: @"requestStatus"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
    if(self.defaultForumId)
        [jsonObject setObject: self.defaultForumId forKey: @"defaultForumId"];
    if(self.feedbackForumId)
        [jsonObject setObject: self.feedbackForumId forKey: @"feedbackForumId"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.geoPointList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCommunityGeoPointDTO* item in self.geoPointList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"geoPointList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.aliasName = [jsonObject objectForKey: @"aliasName"];
        if(self.aliasName && [self.aliasName isEqual:[NSNull null]])
            self.aliasName = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.zipcode = [jsonObject objectForKey: @"zipcode"];
        if(self.zipcode && [self.zipcode isEqual:[NSNull null]])
            self.zipcode = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.detailDescription = [jsonObject objectForKey: @"detailDescription"];
        if(self.detailDescription && [self.detailDescription isEqual:[NSNull null]])
            self.detailDescription = nil;

        self.aptSegment1 = [jsonObject objectForKey: @"aptSegment1"];
        if(self.aptSegment1 && [self.aptSegment1 isEqual:[NSNull null]])
            self.aptSegment1 = nil;

        self.aptSegment2 = [jsonObject objectForKey: @"aptSegment2"];
        if(self.aptSegment2 && [self.aptSegment2 isEqual:[NSNull null]])
            self.aptSegment2 = nil;

        self.aptSegment3 = [jsonObject objectForKey: @"aptSegment3"];
        if(self.aptSegment3 && [self.aptSegment3 isEqual:[NSNull null]])
            self.aptSegment3 = nil;

        self.aptSeg1Sample = [jsonObject objectForKey: @"aptSeg1Sample"];
        if(self.aptSeg1Sample && [self.aptSeg1Sample isEqual:[NSNull null]])
            self.aptSeg1Sample = nil;

        self.aptSeg2Sample = [jsonObject objectForKey: @"aptSeg2Sample"];
        if(self.aptSeg2Sample && [self.aptSeg2Sample isEqual:[NSNull null]])
            self.aptSeg2Sample = nil;

        self.aptSeg3Sample = [jsonObject objectForKey: @"aptSeg3Sample"];
        if(self.aptSeg3Sample && [self.aptSeg3Sample isEqual:[NSNull null]])
            self.aptSeg3Sample = nil;

        self.aptCount = [jsonObject objectForKey: @"aptCount"];
        if(self.aptCount && [self.aptCount isEqual:[NSNull null]])
            self.aptCount = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.deleteTime = [jsonObject objectForKey: @"deleteTime"];
        if(self.deleteTime && [self.deleteTime isEqual:[NSNull null]])
            self.deleteTime = nil;

        self.requestStatus = [jsonObject objectForKey: @"requestStatus"];
        if(self.requestStatus && [self.requestStatus isEqual:[NSNull null]])
            self.requestStatus = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        self.defaultForumId = [jsonObject objectForKey: @"defaultForumId"];
        if(self.defaultForumId && [self.defaultForumId isEqual:[NSNull null]])
            self.defaultForumId = nil;

        self.feedbackForumId = [jsonObject objectForKey: @"feedbackForumId"];
        if(self.feedbackForumId && [self.feedbackForumId isEqual:[NSNull null]])
            self.feedbackForumId = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"geoPointList"];
            for(id itemJson in jsonArray) {
                EvhCommunityGeoPointDTO* item = [EvhCommunityGeoPointDTO new];
                
                [item fromJson: itemJson];
                [self.geoPointList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
