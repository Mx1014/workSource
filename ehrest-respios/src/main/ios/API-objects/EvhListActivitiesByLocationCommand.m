//
// EvhListActivitiesByLocationCommand.m
//
#import "EvhListActivitiesByLocationCommand.h"
#import "EvhGeoLocation.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActivitiesByLocationCommand
//

@implementation EvhListActivitiesByLocationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListActivitiesByLocationCommand* obj = [EvhListActivitiesByLocationCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _locationPointList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.locationPointList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhGeoLocation* item in self.locationPointList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"locationPointList"];
    }
    if(self.scope)
        [jsonObject setObject: self.scope forKey: @"scope"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"locationPointList"];
            for(id itemJson in jsonArray) {
                EvhGeoLocation* item = [EvhGeoLocation new];
                
                [item fromJson: itemJson];
                [self.locationPointList addObject: item];
            }
        }
        self.scope = [jsonObject objectForKey: @"scope"];
        if(self.scope && [self.scope isEqual:[NSNull null]])
            self.scope = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
