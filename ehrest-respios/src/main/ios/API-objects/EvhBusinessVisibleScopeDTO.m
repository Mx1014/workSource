//
// EvhBusinessVisibleScopeDTO.m
//
#import "EvhBusinessVisibleScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessVisibleScopeDTO
//

@implementation EvhBusinessVisibleScopeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBusinessVisibleScopeDTO* obj = [EvhBusinessVisibleScopeDTO new];
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
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.scopeType)
        [jsonObject setObject: self.scopeType forKey: @"scopeType"];
    if(self.scopeId)
        [jsonObject setObject: self.scopeId forKey: @"scopeId"];
    if(self.scopeName)
        [jsonObject setObject: self.scopeName forKey: @"scopeName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.scopeType = [jsonObject objectForKey: @"scopeType"];
        if(self.scopeType && [self.scopeType isEqual:[NSNull null]])
            self.scopeType = nil;

        self.scopeId = [jsonObject objectForKey: @"scopeId"];
        if(self.scopeId && [self.scopeId isEqual:[NSNull null]])
            self.scopeId = nil;

        self.scopeName = [jsonObject objectForKey: @"scopeName"];
        if(self.scopeName && [self.scopeName isEqual:[NSNull null]])
            self.scopeName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
