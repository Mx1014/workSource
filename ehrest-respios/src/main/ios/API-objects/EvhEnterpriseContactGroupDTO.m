//
// EvhEnterpriseContactGroupDTO.m
//
#import "EvhEnterpriseContactGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactGroupDTO
//

@implementation EvhEnterpriseContactGroupDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseContactGroupDTO* obj = [EvhEnterpriseContactGroupDTO new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.applyGroup)
        [jsonObject setObject: self.applyGroup forKey: @"applyGroup"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.parentGroupName)
        [jsonObject setObject: self.parentGroupName forKey: @"parentGroupName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.applyGroup = [jsonObject objectForKey: @"applyGroup"];
        if(self.applyGroup && [self.applyGroup isEqual:[NSNull null]])
            self.applyGroup = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.parentGroupName = [jsonObject objectForKey: @"parentGroupName"];
        if(self.parentGroupName && [self.parentGroupName isEqual:[NSNull null]])
            self.parentGroupName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
