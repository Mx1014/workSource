//
// EvhQualityStandardsDTO.m
//
#import "EvhQualityStandardsDTO.h"
#import "EvhRepeatSettingsDTO.h"
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityStandardsDTO
//

@implementation EvhQualityStandardsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQualityStandardsDTO* obj = [EvhQualityStandardsDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _executiveGroup = [NSMutableArray new];
        _reviewGroup = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.standardNumber)
        [jsonObject setObject: self.standardNumber forKey: @"standardNumber"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.categoryName)
        [jsonObject setObject: self.categoryName forKey: @"categoryName"];
    if(self.repeat) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.repeat toJson: dic];
        
        [jsonObject setObject: dic forKey: @"repeat"];
    }
    if(self.executiveGroup) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhStandardGroupDTO* item in self.executiveGroup) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"executiveGroup"];
    }
    if(self.reviewGroup) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhStandardGroupDTO* item in self.reviewGroup) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"reviewGroup"];
    }
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.deleterUid)
        [jsonObject setObject: self.deleterUid forKey: @"deleterUid"];
    if(self.deleteTime)
        [jsonObject setObject: self.deleteTime forKey: @"deleteTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.standardNumber = [jsonObject objectForKey: @"standardNumber"];
        if(self.standardNumber && [self.standardNumber isEqual:[NSNull null]])
            self.standardNumber = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.categoryName = [jsonObject objectForKey: @"categoryName"];
        if(self.categoryName && [self.categoryName isEqual:[NSNull null]])
            self.categoryName = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"repeat"];

        self.repeat = [EvhRepeatSettingsDTO new];
        self.repeat = [self.repeat fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"executiveGroup"];
            for(id itemJson in jsonArray) {
                EvhStandardGroupDTO* item = [EvhStandardGroupDTO new];
                
                [item fromJson: itemJson];
                [self.executiveGroup addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"reviewGroup"];
            for(id itemJson in jsonArray) {
                EvhStandardGroupDTO* item = [EvhStandardGroupDTO new];
                
                [item fromJson: itemJson];
                [self.reviewGroup addObject: item];
            }
        }
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.deleterUid = [jsonObject objectForKey: @"deleterUid"];
        if(self.deleterUid && [self.deleterUid isEqual:[NSNull null]])
            self.deleterUid = nil;

        self.deleteTime = [jsonObject objectForKey: @"deleteTime"];
        if(self.deleteTime && [self.deleteTime isEqual:[NSNull null]])
            self.deleteTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
