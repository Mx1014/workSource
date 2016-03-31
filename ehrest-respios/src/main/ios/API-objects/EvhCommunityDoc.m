//
// EvhCommunityDoc.m
// generated at 2016-03-31 11:07:26 
//
#import "EvhCommunityDoc.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityDoc
//

@implementation EvhCommunityDoc

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityDoc* obj = [EvhCommunityDoc new];
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
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.regionId)
        [jsonObject setObject: self.regionId forKey: @"regionId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.regionId = [jsonObject objectForKey: @"regionId"];
        if(self.regionId && [self.regionId isEqual:[NSNull null]])
            self.regionId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
