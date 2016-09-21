//
// EvhSceneTypeInfoDTO.m
//
#import "EvhSceneTypeInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSceneTypeInfoDTO
//

@implementation EvhSceneTypeInfoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSceneTypeInfoDTO* obj = [EvhSceneTypeInfoDTO new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
