//
// EvhQualityCategoriesDTO.m
//
#import "EvhQualityCategoriesDTO.h"
#import "EvhQualityCategoriesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityCategoriesDTO
//

@implementation EvhQualityCategoriesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQualityCategoriesDTO* obj = [EvhQualityCategoriesDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _childrens = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.path)
        [jsonObject setObject: self.path forKey: @"path"];
    if(self.defaultOrder)
        [jsonObject setObject: self.defaultOrder forKey: @"defaultOrder"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.score)
        [jsonObject setObject: self.score forKey: @"score"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.childrens) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhQualityCategoriesDTO* item in self.childrens) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"childrens"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.path = [jsonObject objectForKey: @"path"];
        if(self.path && [self.path isEqual:[NSNull null]])
            self.path = nil;

        self.defaultOrder = [jsonObject objectForKey: @"defaultOrder"];
        if(self.defaultOrder && [self.defaultOrder isEqual:[NSNull null]])
            self.defaultOrder = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.score = [jsonObject objectForKey: @"score"];
        if(self.score && [self.score isEqual:[NSNull null]])
            self.score = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"childrens"];
            for(id itemJson in jsonArray) {
                EvhQualityCategoriesDTO* item = [EvhQualityCategoriesDTO new];
                
                [item fromJson: itemJson];
                [self.childrens addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
