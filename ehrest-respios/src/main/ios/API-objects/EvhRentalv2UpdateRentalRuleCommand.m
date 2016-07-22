//
// EvhRentalv2UpdateRentalRuleCommand.m
//
#import "EvhRentalv2UpdateRentalRuleCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2UpdateRentalRuleCommand
//

@implementation EvhRentalv2UpdateRentalRuleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2UpdateRentalRuleCommand* obj = [EvhRentalv2UpdateRentalRuleCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.rentalStartTime)
        [jsonObject setObject: self.rentalStartTime forKey: @"rentalStartTime"];
    if(self.rentalEndTime)
        [jsonObject setObject: self.rentalEndTime forKey: @"rentalEndTime"];
    if(self.payStartTime)
        [jsonObject setObject: self.payStartTime forKey: @"payStartTime"];
    if(self.payEndTime)
        [jsonObject setObject: self.payEndTime forKey: @"payEndTime"];
    if(self.payRatio)
        [jsonObject setObject: self.payRatio forKey: @"payRatio"];
    if(self.refundFlag)
        [jsonObject setObject: self.refundFlag forKey: @"refundFlag"];
    if(self.rentalType)
        [jsonObject setObject: self.rentalType forKey: @"rentalType"];
    if(self.contactNum)
        [jsonObject setObject: self.contactNum forKey: @"contactNum"];
    if(self.cancelTime)
        [jsonObject setObject: self.cancelTime forKey: @"cancelTime"];
    if(self.overtimeTime)
        [jsonObject setObject: self.overtimeTime forKey: @"overtimeTime"];
    if(self.contactAddress)
        [jsonObject setObject: self.contactAddress forKey: @"contactAddress"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.rentalStartTime = [jsonObject objectForKey: @"rentalStartTime"];
        if(self.rentalStartTime && [self.rentalStartTime isEqual:[NSNull null]])
            self.rentalStartTime = nil;

        self.rentalEndTime = [jsonObject objectForKey: @"rentalEndTime"];
        if(self.rentalEndTime && [self.rentalEndTime isEqual:[NSNull null]])
            self.rentalEndTime = nil;

        self.payStartTime = [jsonObject objectForKey: @"payStartTime"];
        if(self.payStartTime && [self.payStartTime isEqual:[NSNull null]])
            self.payStartTime = nil;

        self.payEndTime = [jsonObject objectForKey: @"payEndTime"];
        if(self.payEndTime && [self.payEndTime isEqual:[NSNull null]])
            self.payEndTime = nil;

        self.payRatio = [jsonObject objectForKey: @"payRatio"];
        if(self.payRatio && [self.payRatio isEqual:[NSNull null]])
            self.payRatio = nil;

        self.refundFlag = [jsonObject objectForKey: @"refundFlag"];
        if(self.refundFlag && [self.refundFlag isEqual:[NSNull null]])
            self.refundFlag = nil;

        self.rentalType = [jsonObject objectForKey: @"rentalType"];
        if(self.rentalType && [self.rentalType isEqual:[NSNull null]])
            self.rentalType = nil;

        self.contactNum = [jsonObject objectForKey: @"contactNum"];
        if(self.contactNum && [self.contactNum isEqual:[NSNull null]])
            self.contactNum = nil;

        self.cancelTime = [jsonObject objectForKey: @"cancelTime"];
        if(self.cancelTime && [self.cancelTime isEqual:[NSNull null]])
            self.cancelTime = nil;

        self.overtimeTime = [jsonObject objectForKey: @"overtimeTime"];
        if(self.overtimeTime && [self.overtimeTime isEqual:[NSNull null]])
            self.overtimeTime = nil;

        self.contactAddress = [jsonObject objectForKey: @"contactAddress"];
        if(self.contactAddress && [self.contactAddress isEqual:[NSNull null]])
            self.contactAddress = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
