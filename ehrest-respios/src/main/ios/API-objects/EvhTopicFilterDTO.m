//
// EvhTopicFilterDTO.m
// generated at 2016-03-31 13:49:12 
//
#import "EvhTopicFilterDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTopicFilterDTO
//

@implementation EvhTopicFilterDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhTopicFilterDTO* obj = [EvhTopicFilterDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.actionUrl)
        [jsonObject setObject: self.actionUrl forKey: @"actionUrl"];
    if(self.isDefault)
        [jsonObject setObject: self.isDefault forKey: @"isDefault"];
    if(self.isLeaf)
        [jsonObject setObject: self.isLeaf forKey: @"isLeaf"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.actionUrl = [jsonObject objectForKey: @"actionUrl"];
        if(self.actionUrl && [self.actionUrl isEqual:[NSNull null]])
            self.actionUrl = nil;

        self.isDefault = [jsonObject objectForKey: @"isDefault"];
        if(self.isDefault && [self.isDefault isEqual:[NSNull null]])
            self.isDefault = nil;

        self.isLeaf = [jsonObject objectForKey: @"isLeaf"];
        if(self.isLeaf && [self.isLeaf isEqual:[NSNull null]])
            self.isLeaf = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
