//
// EvhSceneContactDTO.m
//
#import "EvhSceneContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSceneContactDTO
//

@implementation EvhSceneContactDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSceneContactDTO* obj = [EvhSceneContactDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.sceneType)
        [jsonObject setObject: self.sceneType forKey: @"sceneType"];
    if(self.entityType)
        [jsonObject setObject: self.entityType forKey: @"entityType"];
    if(self.entityId)
        [jsonObject setObject: self.entityId forKey: @"entityId"];
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
    if(self.contactPhone)
        [jsonObject setObject: self.contactPhone forKey: @"contactPhone"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactAvatar)
        [jsonObject setObject: self.contactAvatar forKey: @"contactAvatar"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.departmentName)
        [jsonObject setObject: self.departmentName forKey: @"departmentName"];
    if(self.statusLine)
        [jsonObject setObject: self.statusLine forKey: @"statusLine"];
    if(self.occupation)
        [jsonObject setObject: self.occupation forKey: @"occupation"];
    if(self.initial)
        [jsonObject setObject: self.initial forKey: @"initial"];
    if(self.fullPinyin)
        [jsonObject setObject: self.fullPinyin forKey: @"fullPinyin"];
    if(self.fullInitial)
        [jsonObject setObject: self.fullInitial forKey: @"fullInitial"];
    if(self.neighborhoodRelation)
        [jsonObject setObject: self.neighborhoodRelation forKey: @"neighborhoodRelation"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sceneType = [jsonObject objectForKey: @"sceneType"];
        if(self.sceneType && [self.sceneType isEqual:[NSNull null]])
            self.sceneType = nil;

        self.entityType = [jsonObject objectForKey: @"entityType"];
        if(self.entityType && [self.entityType isEqual:[NSNull null]])
            self.entityType = nil;

        self.entityId = [jsonObject objectForKey: @"entityId"];
        if(self.entityId && [self.entityId isEqual:[NSNull null]])
            self.entityId = nil;

        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        self.contactPhone = [jsonObject objectForKey: @"contactPhone"];
        if(self.contactPhone && [self.contactPhone isEqual:[NSNull null]])
            self.contactPhone = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.contactAvatar = [jsonObject objectForKey: @"contactAvatar"];
        if(self.contactAvatar && [self.contactAvatar isEqual:[NSNull null]])
            self.contactAvatar = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.departmentName = [jsonObject objectForKey: @"departmentName"];
        if(self.departmentName && [self.departmentName isEqual:[NSNull null]])
            self.departmentName = nil;

        self.statusLine = [jsonObject objectForKey: @"statusLine"];
        if(self.statusLine && [self.statusLine isEqual:[NSNull null]])
            self.statusLine = nil;

        self.occupation = [jsonObject objectForKey: @"occupation"];
        if(self.occupation && [self.occupation isEqual:[NSNull null]])
            self.occupation = nil;

        self.initial = [jsonObject objectForKey: @"initial"];
        if(self.initial && [self.initial isEqual:[NSNull null]])
            self.initial = nil;

        self.fullPinyin = [jsonObject objectForKey: @"fullPinyin"];
        if(self.fullPinyin && [self.fullPinyin isEqual:[NSNull null]])
            self.fullPinyin = nil;

        self.fullInitial = [jsonObject objectForKey: @"fullInitial"];
        if(self.fullInitial && [self.fullInitial isEqual:[NSNull null]])
            self.fullInitial = nil;

        self.neighborhoodRelation = [jsonObject objectForKey: @"neighborhoodRelation"];
        if(self.neighborhoodRelation && [self.neighborhoodRelation isEqual:[NSNull null]])
            self.neighborhoodRelation = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
