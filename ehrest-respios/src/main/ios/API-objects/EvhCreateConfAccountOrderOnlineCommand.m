//
// EvhCreateConfAccountOrderOnlineCommand.m
//
#import "EvhCreateConfAccountOrderOnlineCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateConfAccountOrderOnlineCommand
//

@implementation EvhCreateConfAccountOrderOnlineCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateConfAccountOrderOnlineCommand* obj = [EvhCreateConfAccountOrderOnlineCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.contactor)
        [jsonObject setObject: self.contactor forKey: @"contactor"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.buyChannel)
        [jsonObject setObject: self.buyChannel forKey: @"buyChannel"];
    if(self.quantity)
        [jsonObject setObject: self.quantity forKey: @"quantity"];
    if(self.period)
        [jsonObject setObject: self.period forKey: @"period"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.invoiceReqFlag)
        [jsonObject setObject: self.invoiceReqFlag forKey: @"invoiceReqFlag"];
    if(self.confCapacity)
        [jsonObject setObject: self.confCapacity forKey: @"confCapacity"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
    if(self.mailAddress)
        [jsonObject setObject: self.mailAddress forKey: @"mailAddress"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.contactor = [jsonObject objectForKey: @"contactor"];
        if(self.contactor && [self.contactor isEqual:[NSNull null]])
            self.contactor = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.buyChannel = [jsonObject objectForKey: @"buyChannel"];
        if(self.buyChannel && [self.buyChannel isEqual:[NSNull null]])
            self.buyChannel = nil;

        self.quantity = [jsonObject objectForKey: @"quantity"];
        if(self.quantity && [self.quantity isEqual:[NSNull null]])
            self.quantity = nil;

        self.period = [jsonObject objectForKey: @"period"];
        if(self.period && [self.period isEqual:[NSNull null]])
            self.period = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.invoiceReqFlag = [jsonObject objectForKey: @"invoiceReqFlag"];
        if(self.invoiceReqFlag && [self.invoiceReqFlag isEqual:[NSNull null]])
            self.invoiceReqFlag = nil;

        self.confCapacity = [jsonObject objectForKey: @"confCapacity"];
        if(self.confCapacity && [self.confCapacity isEqual:[NSNull null]])
            self.confCapacity = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        self.mailAddress = [jsonObject objectForKey: @"mailAddress"];
        if(self.mailAddress && [self.mailAddress isEqual:[NSNull null]])
            self.mailAddress = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
