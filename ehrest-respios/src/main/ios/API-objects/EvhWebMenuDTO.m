//
// EvhWebMenuDTO.m
//
#import "EvhWebMenuDTO.h"
#import "EvhWebMenuDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWebMenuDTO
//

@implementation EvhWebMenuDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhWebMenuDTO* obj = [EvhWebMenuDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _dtos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.iconUrl)
        [jsonObject setObject: self.iconUrl forKey: @"iconUrl"];
    if(self.dataType)
        [jsonObject setObject: self.dataType forKey: @"dataType"];
    if(self.leafFlag)
        [jsonObject setObject: self.leafFlag forKey: @"leafFlag"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.dtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhWebMenuDTO* item in self.dtos) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"dtos"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.iconUrl = [jsonObject objectForKey: @"iconUrl"];
        if(self.iconUrl && [self.iconUrl isEqual:[NSNull null]])
            self.iconUrl = nil;

        self.dataType = [jsonObject objectForKey: @"dataType"];
        if(self.dataType && [self.dataType isEqual:[NSNull null]])
            self.dataType = nil;

        self.leafFlag = [jsonObject objectForKey: @"leafFlag"];
        if(self.leafFlag && [self.leafFlag isEqual:[NSNull null]])
            self.leafFlag = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"dtos"];
            for(id itemJson in jsonArray) {
                EvhWebMenuDTO* item = [EvhWebMenuDTO new];
                
                [item fromJson: itemJson];
                [self.dtos addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
