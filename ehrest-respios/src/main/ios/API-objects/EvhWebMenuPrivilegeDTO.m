//
// EvhWebMenuPrivilegeDTO.m
//
#import "EvhWebMenuPrivilegeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWebMenuPrivilegeDTO
//

@implementation EvhWebMenuPrivilegeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhWebMenuPrivilegeDTO* obj = [EvhWebMenuPrivilegeDTO new];
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
    if(self.menuId)
        [jsonObject setObject: self.menuId forKey: @"menuId"];
    if(self.privilegeId)
        [jsonObject setObject: self.privilegeId forKey: @"privilegeId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.showFlag)
        [jsonObject setObject: self.showFlag forKey: @"showFlag"];
    if(self.discription)
        [jsonObject setObject: self.discription forKey: @"discription"];
    if(self.sortNum)
        [jsonObject setObject: self.sortNum forKey: @"sortNum"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.menuId = [jsonObject objectForKey: @"menuId"];
        if(self.menuId && [self.menuId isEqual:[NSNull null]])
            self.menuId = nil;

        self.privilegeId = [jsonObject objectForKey: @"privilegeId"];
        if(self.privilegeId && [self.privilegeId isEqual:[NSNull null]])
            self.privilegeId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.showFlag = [jsonObject objectForKey: @"showFlag"];
        if(self.showFlag && [self.showFlag isEqual:[NSNull null]])
            self.showFlag = nil;

        self.discription = [jsonObject objectForKey: @"discription"];
        if(self.discription && [self.discription isEqual:[NSNull null]])
            self.discription = nil;

        self.sortNum = [jsonObject objectForKey: @"sortNum"];
        if(self.sortNum && [self.sortNum isEqual:[NSNull null]])
            self.sortNum = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
