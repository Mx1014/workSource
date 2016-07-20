//
// EvhCardRechargeOrderDTO.m
//
#import "EvhCardRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardRechargeOrderDTO
//

@implementation EvhCardRechargeOrderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCardRechargeOrderDTO* obj = [EvhCardRechargeOrderDTO new];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.cardNo)
        [jsonObject setObject: self.cardNo forKey: @"cardNo"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.rechargeTime)
        [jsonObject setObject: self.rechargeTime forKey: @"rechargeTime"];
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
    if(self.paidType)
        [jsonObject setObject: self.paidType forKey: @"paidType"];
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.cardNo = [jsonObject objectForKey: @"cardNo"];
        if(self.cardNo && [self.cardNo isEqual:[NSNull null]])
            self.cardNo = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.rechargeTime = [jsonObject objectForKey: @"rechargeTime"];
        if(self.rechargeTime && [self.rechargeTime isEqual:[NSNull null]])
            self.rechargeTime = nil;

        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        self.paidType = [jsonObject objectForKey: @"paidType"];
        if(self.paidType && [self.paidType isEqual:[NSNull null]])
            self.paidType = nil;

        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
