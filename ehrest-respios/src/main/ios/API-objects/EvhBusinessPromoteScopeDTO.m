//
// EvhBusinessPromoteScopeDTO.m
//
#import "EvhBusinessPromoteScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessPromoteScopeDTO
//

@implementation EvhBusinessPromoteScopeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBusinessPromoteScopeDTO* obj = [EvhBusinessPromoteScopeDTO new];
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
    if(self.regionName)
        [jsonObject setObject: self.regionName forKey: @"regionName"];
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

        self.regionName = [jsonObject objectForKey: @"regionName"];
        if(self.regionName && [self.regionName isEqual:[NSNull null]])
            self.regionName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
