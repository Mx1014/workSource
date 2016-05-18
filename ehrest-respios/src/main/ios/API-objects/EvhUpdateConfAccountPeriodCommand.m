//
// EvhUpdateConfAccountPeriodCommand.m
//
#import "EvhUpdateConfAccountPeriodCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateConfAccountPeriodCommand
//

@implementation EvhUpdateConfAccountPeriodCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateConfAccountPeriodCommand* obj = [EvhUpdateConfAccountPeriodCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _accountIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.accountIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.accountIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"accountIds"];
    }
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
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
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.invoiceFlag)
        [jsonObject setObject: self.invoiceFlag forKey: @"invoiceFlag"];
    if(self.mailAddress)
        [jsonObject setObject: self.mailAddress forKey: @"mailAddress"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"accountIds"];
            for(id itemJson in jsonArray) {
                [self.accountIds addObject: itemJson];
            }
        }
        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

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

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.invoiceFlag = [jsonObject objectForKey: @"invoiceFlag"];
        if(self.invoiceFlag && [self.invoiceFlag isEqual:[NSNull null]])
            self.invoiceFlag = nil;

        self.mailAddress = [jsonObject objectForKey: @"mailAddress"];
        if(self.mailAddress && [self.mailAddress isEqual:[NSNull null]])
            self.mailAddress = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
