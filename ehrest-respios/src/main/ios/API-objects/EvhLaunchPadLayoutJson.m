//
// EvhLaunchPadLayoutJson.m
//
#import "EvhLaunchPadLayoutJson.h"
#import "EvhLaunchPadLayoutGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadLayoutJson
//

@implementation EvhLaunchPadLayoutJson

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLaunchPadLayoutJson* obj = [EvhLaunchPadLayoutJson new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _groups = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.versionCode)
        [jsonObject setObject: self.versionCode forKey: @"versionCode"];
    if(self.versionName)
        [jsonObject setObject: self.versionName forKey: @"versionName"];
    if(self.layoutName)
        [jsonObject setObject: self.layoutName forKey: @"layoutName"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.groups) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhLaunchPadLayoutGroupDTO* item in self.groups) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"groups"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.versionCode = [jsonObject objectForKey: @"versionCode"];
        if(self.versionCode && [self.versionCode isEqual:[NSNull null]])
            self.versionCode = nil;

        self.versionName = [jsonObject objectForKey: @"versionName"];
        if(self.versionName && [self.versionName isEqual:[NSNull null]])
            self.versionName = nil;

        self.layoutName = [jsonObject objectForKey: @"layoutName"];
        if(self.layoutName && [self.layoutName isEqual:[NSNull null]])
            self.layoutName = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"groups"];
            for(id itemJson in jsonArray) {
                EvhLaunchPadLayoutGroupDTO* item = [EvhLaunchPadLayoutGroupDTO new];
                
                [item fromJson: itemJson];
                [self.groups addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
