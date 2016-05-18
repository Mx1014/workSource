//
// EvhListWebMenuPrivilegeDTO.m
//
#import "EvhListWebMenuPrivilegeDTO.h"
#import "EvhWebMenuPrivilegeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuPrivilegeDTO
//

@implementation EvhListWebMenuPrivilegeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListWebMenuPrivilegeDTO* obj = [EvhListWebMenuPrivilegeDTO new];
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
    if(self.moduleId)
        [jsonObject setObject: self.moduleId forKey: @"moduleId"];
    if(self.moduleName)
        [jsonObject setObject: self.moduleName forKey: @"moduleName"];
    if(self.dtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhWebMenuPrivilegeDTO* item in self.dtos) {
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
        self.moduleId = [jsonObject objectForKey: @"moduleId"];
        if(self.moduleId && [self.moduleId isEqual:[NSNull null]])
            self.moduleId = nil;

        self.moduleName = [jsonObject objectForKey: @"moduleName"];
        if(self.moduleName && [self.moduleName isEqual:[NSNull null]])
            self.moduleName = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"dtos"];
            for(id itemJson in jsonArray) {
                EvhWebMenuPrivilegeDTO* item = [EvhWebMenuPrivilegeDTO new];
                
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
