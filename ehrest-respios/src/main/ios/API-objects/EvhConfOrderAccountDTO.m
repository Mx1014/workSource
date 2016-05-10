//
// EvhConfOrderAccountDTO.m
//
#import "EvhConfOrderAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfOrderAccountDTO
//

@implementation EvhConfOrderAccountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhConfOrderAccountDTO* obj = [EvhConfOrderAccountDTO new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.departmentId)
        [jsonObject setObject: self.departmentId forKey: @"departmentId"];
    if(self.department)
        [jsonObject setObject: self.department forKey: @"department"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.departmentId = [jsonObject objectForKey: @"departmentId"];
        if(self.departmentId && [self.departmentId isEqual:[NSNull null]])
            self.departmentId = nil;

        self.department = [jsonObject objectForKey: @"department"];
        if(self.department && [self.department isEqual:[NSNull null]])
            self.department = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
