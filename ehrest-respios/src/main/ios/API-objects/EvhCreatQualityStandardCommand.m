//
// EvhCreatQualityStandardCommand.m
//
#import "EvhCreatQualityStandardCommand.h"
#import "EvhRepeatSettingsDTO.h"
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatQualityStandardCommand
//

@implementation EvhCreatQualityStandardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreatQualityStandardCommand* obj = [EvhCreatQualityStandardCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _group = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.standardNumber)
        [jsonObject setObject: self.standardNumber forKey: @"standardNumber"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.repeat) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.repeat toJson: dic];
        
        [jsonObject setObject: dic forKey: @"repeat"];
    }
    if(self.group) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhStandardGroupDTO* item in self.group) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"group"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.standardNumber = [jsonObject objectForKey: @"standardNumber"];
        if(self.standardNumber && [self.standardNumber isEqual:[NSNull null]])
            self.standardNumber = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"repeat"];

        self.repeat = [EvhRepeatSettingsDTO new];
        self.repeat = [self.repeat fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"group"];
            for(id itemJson in jsonArray) {
                EvhStandardGroupDTO* item = [EvhStandardGroupDTO new];
                
                [item fromJson: itemJson];
                [self.group addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
