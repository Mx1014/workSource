//
// EvhNavigationActionData.m
//
#import "EvhNavigationActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNavigationActionData
//

@implementation EvhNavigationActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNavigationActionData* obj = [EvhNavigationActionData new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _callPhones = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.itemLocation)
        [jsonObject setObject: self.itemLocation forKey: @"itemLocation"];
    if(self.layoutName)
        [jsonObject setObject: self.layoutName forKey: @"layoutName"];
    if(self.callPhones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.callPhones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"callPhones"];
    }
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.entityTag)
        [jsonObject setObject: self.entityTag forKey: @"entityTag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemLocation = [jsonObject objectForKey: @"itemLocation"];
        if(self.itemLocation && [self.itemLocation isEqual:[NSNull null]])
            self.itemLocation = nil;

        self.layoutName = [jsonObject objectForKey: @"layoutName"];
        if(self.layoutName && [self.layoutName isEqual:[NSNull null]])
            self.layoutName = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"callPhones"];
            for(id itemJson in jsonArray) {
                [self.callPhones addObject: itemJson];
            }
        }
        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        self.entityTag = [jsonObject objectForKey: @"entityTag"];
        if(self.entityTag && [self.entityTag isEqual:[NSNull null]])
            self.entityTag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
