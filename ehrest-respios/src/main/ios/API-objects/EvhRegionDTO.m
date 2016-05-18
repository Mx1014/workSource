//
// EvhRegionDTO.m
//
#import "EvhRegionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionDTO
//

@implementation EvhRegionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRegionDTO* obj = [EvhRegionDTO new];
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
    if(self.path)
        [jsonObject setObject: self.path forKey: @"path"];
    if(self.scopeCode)
        [jsonObject setObject: self.scopeCode forKey: @"scopeCode"];
    if(self.isoCode)
        [jsonObject setObject: self.isoCode forKey: @"isoCode"];
    if(self.telCode)
        [jsonObject setObject: self.telCode forKey: @"telCode"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.pinyinName)
        [jsonObject setObject: self.pinyinName forKey: @"pinyinName"];
    if(self.pinyinPrefix)
        [jsonObject setObject: self.pinyinPrefix forKey: @"pinyinPrefix"];
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

        self.path = [jsonObject objectForKey: @"path"];
        if(self.path && [self.path isEqual:[NSNull null]])
            self.path = nil;

        self.scopeCode = [jsonObject objectForKey: @"scopeCode"];
        if(self.scopeCode && [self.scopeCode isEqual:[NSNull null]])
            self.scopeCode = nil;

        self.isoCode = [jsonObject objectForKey: @"isoCode"];
        if(self.isoCode && [self.isoCode isEqual:[NSNull null]])
            self.isoCode = nil;

        self.telCode = [jsonObject objectForKey: @"telCode"];
        if(self.telCode && [self.telCode isEqual:[NSNull null]])
            self.telCode = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.pinyinName = [jsonObject objectForKey: @"pinyinName"];
        if(self.pinyinName && [self.pinyinName isEqual:[NSNull null]])
            self.pinyinName = nil;

        self.pinyinPrefix = [jsonObject objectForKey: @"pinyinPrefix"];
        if(self.pinyinPrefix && [self.pinyinPrefix isEqual:[NSNull null]])
            self.pinyinPrefix = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
