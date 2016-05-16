//
// EvhActivityMemberDTO.m
//
#import "EvhActivityMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityMemberDTO
//

@implementation EvhActivityMemberDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityMemberDTO* obj = [EvhActivityMemberDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _phone = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userAvatar)
        [jsonObject setObject: self.userAvatar forKey: @"userAvatar"];
    if(self.familyName)
        [jsonObject setObject: self.familyName forKey: @"familyName"];
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.adultCount)
        [jsonObject setObject: self.adultCount forKey: @"adultCount"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.checkinFlag)
        [jsonObject setObject: self.checkinFlag forKey: @"checkinFlag"];
    if(self.checkinTime)
        [jsonObject setObject: self.checkinTime forKey: @"checkinTime"];
    if(self.confirmFlag)
        [jsonObject setObject: self.confirmFlag forKey: @"confirmFlag"];
    if(self.confirmTime)
        [jsonObject setObject: self.confirmTime forKey: @"confirmTime"];
    if(self.creatorFlag)
        [jsonObject setObject: self.creatorFlag forKey: @"creatorFlag"];
    if(self.lotteryWinnerFlag)
        [jsonObject setObject: self.lotteryWinnerFlag forKey: @"lotteryWinnerFlag"];
    if(self.lotteryWonTime)
        [jsonObject setObject: self.lotteryWonTime forKey: @"lotteryWonTime"];
    if(self.phone) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.phone) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"phone"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userAvatar = [jsonObject objectForKey: @"userAvatar"];
        if(self.userAvatar && [self.userAvatar isEqual:[NSNull null]])
            self.userAvatar = nil;

        self.familyName = [jsonObject objectForKey: @"familyName"];
        if(self.familyName && [self.familyName isEqual:[NSNull null]])
            self.familyName = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.adultCount = [jsonObject objectForKey: @"adultCount"];
        if(self.adultCount && [self.adultCount isEqual:[NSNull null]])
            self.adultCount = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        self.checkinFlag = [jsonObject objectForKey: @"checkinFlag"];
        if(self.checkinFlag && [self.checkinFlag isEqual:[NSNull null]])
            self.checkinFlag = nil;

        self.checkinTime = [jsonObject objectForKey: @"checkinTime"];
        if(self.checkinTime && [self.checkinTime isEqual:[NSNull null]])
            self.checkinTime = nil;

        self.confirmFlag = [jsonObject objectForKey: @"confirmFlag"];
        if(self.confirmFlag && [self.confirmFlag isEqual:[NSNull null]])
            self.confirmFlag = nil;

        self.confirmTime = [jsonObject objectForKey: @"confirmTime"];
        if(self.confirmTime && [self.confirmTime isEqual:[NSNull null]])
            self.confirmTime = nil;

        self.creatorFlag = [jsonObject objectForKey: @"creatorFlag"];
        if(self.creatorFlag && [self.creatorFlag isEqual:[NSNull null]])
            self.creatorFlag = nil;

        self.lotteryWinnerFlag = [jsonObject objectForKey: @"lotteryWinnerFlag"];
        if(self.lotteryWinnerFlag && [self.lotteryWinnerFlag isEqual:[NSNull null]])
            self.lotteryWinnerFlag = nil;

        self.lotteryWonTime = [jsonObject objectForKey: @"lotteryWonTime"];
        if(self.lotteryWonTime && [self.lotteryWonTime isEqual:[NSNull null]])
            self.lotteryWonTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"phone"];
            for(id itemJson in jsonArray) {
                [self.phone addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
