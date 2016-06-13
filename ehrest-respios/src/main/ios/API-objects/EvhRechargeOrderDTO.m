//
// EvhRechargeOrderDTO.m
//
#import "EvhRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeOrderDTO
//

@implementation EvhRechargeOrderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRechargeOrderDTO* obj = [EvhRechargeOrderDTO new];
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
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.orderType)
        [jsonObject setObject: self.orderType forKey: @"orderType"];
    if(self.appKey)
        [jsonObject setObject: self.appKey forKey: @"appKey"];
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
    if(self.randomNum)
        [jsonObject setObject: self.randomNum forKey: @"randomNum"];
    if(self.signature)
        [jsonObject setObject: self.signature forKey: @"signature"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.orderType = [jsonObject objectForKey: @"orderType"];
        if(self.orderType && [self.orderType isEqual:[NSNull null]])
            self.orderType = nil;

        self.appKey = [jsonObject objectForKey: @"appKey"];
        if(self.appKey && [self.appKey isEqual:[NSNull null]])
            self.appKey = nil;

        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        self.randomNum = [jsonObject objectForKey: @"randomNum"];
        if(self.randomNum && [self.randomNum isEqual:[NSNull null]])
            self.randomNum = nil;

        self.signature = [jsonObject objectForKey: @"signature"];
        if(self.signature && [self.signature isEqual:[NSNull null]])
            self.signature = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
