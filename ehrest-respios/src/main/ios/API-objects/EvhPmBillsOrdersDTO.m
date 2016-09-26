//
// EvhPmBillsOrdersDTO.m
//
#import "EvhPmBillsOrdersDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillsOrdersDTO
//

@implementation EvhPmBillsOrdersDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmBillsOrdersDTO* obj = [EvhPmBillsOrdersDTO new];
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
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.payerUid)
        [jsonObject setObject: self.payerUid forKey: @"payerUid"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userContact)
        [jsonObject setObject: self.userContact forKey: @"userContact"];
    if(self.orderAmount)
        [jsonObject setObject: self.orderAmount forKey: @"orderAmount"];
    if(self.paidTime)
        [jsonObject setObject: self.paidTime forKey: @"paidTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.paidType)
        [jsonObject setObject: self.paidType forKey: @"paidType"];
    if(self.billDate)
        [jsonObject setObject: self.billDate forKey: @"billDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.payerUid = [jsonObject objectForKey: @"payerUid"];
        if(self.payerUid && [self.payerUid isEqual:[NSNull null]])
            self.payerUid = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userContact = [jsonObject objectForKey: @"userContact"];
        if(self.userContact && [self.userContact isEqual:[NSNull null]])
            self.userContact = nil;

        self.orderAmount = [jsonObject objectForKey: @"orderAmount"];
        if(self.orderAmount && [self.orderAmount isEqual:[NSNull null]])
            self.orderAmount = nil;

        self.paidTime = [jsonObject objectForKey: @"paidTime"];
        if(self.paidTime && [self.paidTime isEqual:[NSNull null]])
            self.paidTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.paidType = [jsonObject objectForKey: @"paidType"];
        if(self.paidType && [self.paidType isEqual:[NSNull null]])
            self.paidType = nil;

        self.billDate = [jsonObject objectForKey: @"billDate"];
        if(self.billDate && [self.billDate isEqual:[NSNull null]])
            self.billDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
