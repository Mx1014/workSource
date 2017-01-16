//
// EvhLaunchPadPostActionCategoryDTO.m
//
#import "EvhLaunchPadPostActionCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadPostActionCategoryDTO
//

@implementation EvhLaunchPadPostActionCategoryDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLaunchPadPostActionCategoryDTO* obj = [EvhLaunchPadPostActionCategoryDTO new];
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
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.contentCategory)
        [jsonObject setObject: self.contentCategory forKey: @"contentCategory"];
    if(self.actionCategory)
        [jsonObject setObject: self.actionCategory forKey: @"actionCategory"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.contentCategory = [jsonObject objectForKey: @"contentCategory"];
        if(self.contentCategory && [self.contentCategory isEqual:[NSNull null]])
            self.contentCategory = nil;

        self.actionCategory = [jsonObject objectForKey: @"actionCategory"];
        if(self.actionCategory && [self.actionCategory isEqual:[NSNull null]])
            self.actionCategory = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
