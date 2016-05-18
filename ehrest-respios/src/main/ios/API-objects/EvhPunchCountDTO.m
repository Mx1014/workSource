//
// EvhPunchCountDTO.m
//
#import "EvhPunchCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchCountDTO
//

@implementation EvhPunchCountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchCountDTO* obj = [EvhPunchCountDTO new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.workDayCount)
        [jsonObject setObject: self.workDayCount forKey: @"workDayCount"];
    if(self.workCount)
        [jsonObject setObject: self.workCount forKey: @"workCount"];
    if(self.belateCount)
        [jsonObject setObject: self.belateCount forKey: @"belateCount"];
    if(self.leaveEarlyCount)
        [jsonObject setObject: self.leaveEarlyCount forKey: @"leaveEarlyCount"];
    if(self.unPunchCount)
        [jsonObject setObject: self.unPunchCount forKey: @"unPunchCount"];
    if(self.blandleCount)
        [jsonObject setObject: self.blandleCount forKey: @"blandleCount"];
    if(self.absenceCount)
        [jsonObject setObject: self.absenceCount forKey: @"absenceCount"];
    if(self.sickCount)
        [jsonObject setObject: self.sickCount forKey: @"sickCount"];
    if(self.exchangeCount)
        [jsonObject setObject: self.exchangeCount forKey: @"exchangeCount"];
    if(self.outworkCount)
        [jsonObject setObject: self.outworkCount forKey: @"outworkCount"];
    if(self.overTimeSum)
        [jsonObject setObject: self.overTimeSum forKey: @"overTimeSum"];
    if(self.punchTimesPerDay)
        [jsonObject setObject: self.punchTimesPerDay forKey: @"punchTimesPerDay"];
    if(self.userEnterpriseGroup)
        [jsonObject setObject: self.userEnterpriseGroup forKey: @"userEnterpriseGroup"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.workDayCount = [jsonObject objectForKey: @"workDayCount"];
        if(self.workDayCount && [self.workDayCount isEqual:[NSNull null]])
            self.workDayCount = nil;

        self.workCount = [jsonObject objectForKey: @"workCount"];
        if(self.workCount && [self.workCount isEqual:[NSNull null]])
            self.workCount = nil;

        self.belateCount = [jsonObject objectForKey: @"belateCount"];
        if(self.belateCount && [self.belateCount isEqual:[NSNull null]])
            self.belateCount = nil;

        self.leaveEarlyCount = [jsonObject objectForKey: @"leaveEarlyCount"];
        if(self.leaveEarlyCount && [self.leaveEarlyCount isEqual:[NSNull null]])
            self.leaveEarlyCount = nil;

        self.unPunchCount = [jsonObject objectForKey: @"unPunchCount"];
        if(self.unPunchCount && [self.unPunchCount isEqual:[NSNull null]])
            self.unPunchCount = nil;

        self.blandleCount = [jsonObject objectForKey: @"blandleCount"];
        if(self.blandleCount && [self.blandleCount isEqual:[NSNull null]])
            self.blandleCount = nil;

        self.absenceCount = [jsonObject objectForKey: @"absenceCount"];
        if(self.absenceCount && [self.absenceCount isEqual:[NSNull null]])
            self.absenceCount = nil;

        self.sickCount = [jsonObject objectForKey: @"sickCount"];
        if(self.sickCount && [self.sickCount isEqual:[NSNull null]])
            self.sickCount = nil;

        self.exchangeCount = [jsonObject objectForKey: @"exchangeCount"];
        if(self.exchangeCount && [self.exchangeCount isEqual:[NSNull null]])
            self.exchangeCount = nil;

        self.outworkCount = [jsonObject objectForKey: @"outworkCount"];
        if(self.outworkCount && [self.outworkCount isEqual:[NSNull null]])
            self.outworkCount = nil;

        self.overTimeSum = [jsonObject objectForKey: @"overTimeSum"];
        if(self.overTimeSum && [self.overTimeSum isEqual:[NSNull null]])
            self.overTimeSum = nil;

        self.punchTimesPerDay = [jsonObject objectForKey: @"punchTimesPerDay"];
        if(self.punchTimesPerDay && [self.punchTimesPerDay isEqual:[NSNull null]])
            self.punchTimesPerDay = nil;

        self.userEnterpriseGroup = [jsonObject objectForKey: @"userEnterpriseGroup"];
        if(self.userEnterpriseGroup && [self.userEnterpriseGroup isEqual:[NSNull null]])
            self.userEnterpriseGroup = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
