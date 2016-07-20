//
// EvhConfAccountDTO.m
//
#import "EvhConfAccountDTO.h"
#import "EvhConfCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfAccountDTO
//

@implementation EvhConfAccountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhConfAccountDTO* obj = [EvhConfAccountDTO new];
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
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.department)
        [jsonObject setObject: self.department forKey: @"department"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
    if(self.accountType)
        [jsonObject setObject: self.accountType forKey: @"accountType"];
    if(self.updateDate)
        [jsonObject setObject: self.updateDate forKey: @"updateDate"];
    if(self.validDate)
        [jsonObject setObject: self.validDate forKey: @"validDate"];
    if(self.userType)
        [jsonObject setObject: self.userType forKey: @"userType"];
    if(self.validFlag)
        [jsonObject setObject: self.validFlag forKey: @"validFlag"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.category) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.category toJson: dic];
        
        [jsonObject setObject: dic forKey: @"category"];
    }
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

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.department = [jsonObject objectForKey: @"department"];
        if(self.department && [self.department isEqual:[NSNull null]])
            self.department = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        self.accountType = [jsonObject objectForKey: @"accountType"];
        if(self.accountType && [self.accountType isEqual:[NSNull null]])
            self.accountType = nil;

        self.updateDate = [jsonObject objectForKey: @"updateDate"];
        if(self.updateDate && [self.updateDate isEqual:[NSNull null]])
            self.updateDate = nil;

        self.validDate = [jsonObject objectForKey: @"validDate"];
        if(self.validDate && [self.validDate isEqual:[NSNull null]])
            self.validDate = nil;

        self.userType = [jsonObject objectForKey: @"userType"];
        if(self.userType && [self.userType isEqual:[NSNull null]])
            self.userType = nil;

        self.validFlag = [jsonObject objectForKey: @"validFlag"];
        if(self.validFlag && [self.validFlag isEqual:[NSNull null]])
            self.validFlag = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"category"];

        self.category = [EvhConfCategoryDTO new];
        self.category = [self.category fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
