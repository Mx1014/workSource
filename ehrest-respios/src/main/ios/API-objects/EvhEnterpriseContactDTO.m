//
// EvhEnterpriseContactDTO.m
//
#import "EvhEnterpriseContactDTO.h"
#import "EvhEnterpriseContactEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactDTO
//

@implementation EvhEnterpriseContactDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseContactDTO* obj = [EvhEnterpriseContactDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _entries = [NSMutableArray new];
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
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.employeeNo)
        [jsonObject setObject: self.employeeNo forKey: @"employeeNo"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.sex)
        [jsonObject setObject: self.sex forKey: @"sex"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.role)
        [jsonObject setObject: self.role forKey: @"role"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.entries) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseContactEntryDTO* item in self.entries) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"entries"];
    }
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

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.employeeNo = [jsonObject objectForKey: @"employeeNo"];
        if(self.employeeNo && [self.employeeNo isEqual:[NSNull null]])
            self.employeeNo = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.sex = [jsonObject objectForKey: @"sex"];
        if(self.sex && [self.sex isEqual:[NSNull null]])
            self.sex = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.role = [jsonObject objectForKey: @"role"];
        if(self.role && [self.role isEqual:[NSNull null]])
            self.role = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"entries"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseContactEntryDTO* item = [EvhEnterpriseContactEntryDTO new];
                
                [item fromJson: itemJson];
                [self.entries addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
