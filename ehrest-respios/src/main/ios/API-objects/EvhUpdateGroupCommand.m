//
// EvhUpdateGroupCommand.m
//
#import "EvhUpdateGroupCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateGroupCommand
//

@implementation EvhUpdateGroupCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateGroupCommand* obj = [EvhUpdateGroupCommand new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.visibilityScope)
        [jsonObject setObject: self.visibilityScope forKey: @"visibilityScope"];
    if(self.visibilityScopeId)
        [jsonObject setObject: self.visibilityScopeId forKey: @"visibilityScopeId"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.visibilityScope = [jsonObject objectForKey: @"visibilityScope"];
        if(self.visibilityScope && [self.visibilityScope isEqual:[NSNull null]])
            self.visibilityScope = nil;

        self.visibilityScopeId = [jsonObject objectForKey: @"visibilityScopeId"];
        if(self.visibilityScopeId && [self.visibilityScopeId isEqual:[NSNull null]])
            self.visibilityScopeId = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
