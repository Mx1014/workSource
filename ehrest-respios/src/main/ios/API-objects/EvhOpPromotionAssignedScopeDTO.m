//
// EvhOpPromotionAssignedScopeDTO.m
//
#import "EvhOpPromotionAssignedScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionAssignedScopeDTO
//

@implementation EvhOpPromotionAssignedScopeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpPromotionAssignedScopeDTO* obj = [EvhOpPromotionAssignedScopeDTO new];
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
    if(self.scopeCode)
        [jsonObject setObject: self.scopeCode forKey: @"scopeCode"];
    if(self.scopeId)
        [jsonObject setObject: self.scopeId forKey: @"scopeId"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.promotionId)
        [jsonObject setObject: self.promotionId forKey: @"promotionId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.scopeCode = [jsonObject objectForKey: @"scopeCode"];
        if(self.scopeCode && [self.scopeCode isEqual:[NSNull null]])
            self.scopeCode = nil;

        self.scopeId = [jsonObject objectForKey: @"scopeId"];
        if(self.scopeId && [self.scopeId isEqual:[NSNull null]])
            self.scopeId = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.promotionId = [jsonObject objectForKey: @"promotionId"];
        if(self.promotionId && [self.promotionId isEqual:[NSNull null]])
            self.promotionId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
