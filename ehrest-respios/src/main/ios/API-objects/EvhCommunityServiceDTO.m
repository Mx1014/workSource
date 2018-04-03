//
// EvhCommunityServiceDTO.m
//
#import "EvhCommunityServiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityServiceDTO
//

@implementation EvhCommunityServiceDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityServiceDTO* obj = [EvhCommunityServiceDTO new];
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
    if(self.scopeCode)
        [jsonObject setObject: self.scopeCode forKey: @"scopeCode"];
    if(self.scopeId)
        [jsonObject setObject: self.scopeId forKey: @"scopeId"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.iconUri)
        [jsonObject setObject: self.iconUri forKey: @"iconUri"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.scopeCode = [jsonObject objectForKey: @"scopeCode"];
        if(self.scopeCode && [self.scopeCode isEqual:[NSNull null]])
            self.scopeCode = nil;

        self.scopeId = [jsonObject objectForKey: @"scopeId"];
        if(self.scopeId && [self.scopeId isEqual:[NSNull null]])
            self.scopeId = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.iconUri = [jsonObject objectForKey: @"iconUri"];
        if(self.iconUri && [self.iconUri isEqual:[NSNull null]])
            self.iconUri = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
