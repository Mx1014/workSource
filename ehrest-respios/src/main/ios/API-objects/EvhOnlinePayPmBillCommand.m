//
// EvhOnlinePayPmBillCommand.m
//
#import "EvhOnlinePayPmBillCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOnlinePayPmBillCommand
//

@implementation EvhOnlinePayPmBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOnlinePayPmBillCommand* obj = [EvhOnlinePayPmBillCommand new];
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
    if(self.payStatus)
        [jsonObject setObject: self.payStatus forKey: @"payStatus"];
    if(self.vendorType)
        [jsonObject setObject: self.vendorType forKey: @"vendorType"];
    if(self.orderNo)
        [jsonObject setObject: self.orderNo forKey: @"orderNo"];
    if(self.payTime)
        [jsonObject setObject: self.payTime forKey: @"payTime"];
    if(self.payAmount)
        [jsonObject setObject: self.payAmount forKey: @"payAmount"];
    if(self.payAccount)
        [jsonObject setObject: self.payAccount forKey: @"payAccount"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.payObj)
        [jsonObject setObject: self.payObj forKey: @"payObj"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.payStatus = [jsonObject objectForKey: @"payStatus"];
        if(self.payStatus && [self.payStatus isEqual:[NSNull null]])
            self.payStatus = nil;

        self.vendorType = [jsonObject objectForKey: @"vendorType"];
        if(self.vendorType && [self.vendorType isEqual:[NSNull null]])
            self.vendorType = nil;

        self.orderNo = [jsonObject objectForKey: @"orderNo"];
        if(self.orderNo && [self.orderNo isEqual:[NSNull null]])
            self.orderNo = nil;

        self.payTime = [jsonObject objectForKey: @"payTime"];
        if(self.payTime && [self.payTime isEqual:[NSNull null]])
            self.payTime = nil;

        self.payAmount = [jsonObject objectForKey: @"payAmount"];
        if(self.payAmount && [self.payAmount isEqual:[NSNull null]])
            self.payAmount = nil;

        self.payAccount = [jsonObject objectForKey: @"payAccount"];
        if(self.payAccount && [self.payAccount isEqual:[NSNull null]])
            self.payAccount = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.payObj = [jsonObject objectForKey: @"payObj"];
        if(self.payObj && [self.payObj isEqual:[NSNull null]])
            self.payObj = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
