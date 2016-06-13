//
// EvhVideoConfAccountDTO.m
//
#import "EvhVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountDTO
//

@implementation EvhVideoConfAccountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVideoConfAccountDTO* obj = [EvhVideoConfAccountDTO new];
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
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.userType)
        [jsonObject setObject: self.userType forKey: @"userType"];
    if(self.validDate)
        [jsonObject setObject: self.validDate forKey: @"validDate"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.accountType)
        [jsonObject setObject: self.accountType forKey: @"accountType"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.userType = [jsonObject objectForKey: @"userType"];
        if(self.userType && [self.userType isEqual:[NSNull null]])
            self.userType = nil;

        self.validDate = [jsonObject objectForKey: @"validDate"];
        if(self.validDate && [self.validDate isEqual:[NSNull null]])
            self.validDate = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.accountType = [jsonObject objectForKey: @"accountType"];
        if(self.accountType && [self.accountType isEqual:[NSNull null]])
            self.accountType = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
