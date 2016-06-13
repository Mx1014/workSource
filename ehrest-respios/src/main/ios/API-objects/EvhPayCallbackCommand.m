//
// EvhPayCallbackCommand.m
//
#import "EvhPayCallbackCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayCallbackCommand
//

@implementation EvhPayCallbackCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPayCallbackCommand* obj = [EvhPayCallbackCommand new];
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
    if(self.orderNo)
        [jsonObject setObject: self.orderNo forKey: @"orderNo"];
    if(self.payStatus)
        [jsonObject setObject: self.payStatus forKey: @"payStatus"];
    if(self.orderType)
        [jsonObject setObject: self.orderType forKey: @"orderType"];
    if(self.vendorType)
        [jsonObject setObject: self.vendorType forKey: @"vendorType"];
    if(self.payTime)
        [jsonObject setObject: self.payTime forKey: @"payTime"];
    if(self.payAmount)
        [jsonObject setObject: self.payAmount forKey: @"payAmount"];
    if(self.payAccount)
        [jsonObject setObject: self.payAccount forKey: @"payAccount"];
    if(self.payObj)
        [jsonObject setObject: self.payObj forKey: @"payObj"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orderNo = [jsonObject objectForKey: @"orderNo"];
        if(self.orderNo && [self.orderNo isEqual:[NSNull null]])
            self.orderNo = nil;

        self.payStatus = [jsonObject objectForKey: @"payStatus"];
        if(self.payStatus && [self.payStatus isEqual:[NSNull null]])
            self.payStatus = nil;

        self.orderType = [jsonObject objectForKey: @"orderType"];
        if(self.orderType && [self.orderType isEqual:[NSNull null]])
            self.orderType = nil;

        self.vendorType = [jsonObject objectForKey: @"vendorType"];
        if(self.vendorType && [self.vendorType isEqual:[NSNull null]])
            self.vendorType = nil;

        self.payTime = [jsonObject objectForKey: @"payTime"];
        if(self.payTime && [self.payTime isEqual:[NSNull null]])
            self.payTime = nil;

        self.payAmount = [jsonObject objectForKey: @"payAmount"];
        if(self.payAmount && [self.payAmount isEqual:[NSNull null]])
            self.payAmount = nil;

        self.payAccount = [jsonObject objectForKey: @"payAccount"];
        if(self.payAccount && [self.payAccount isEqual:[NSNull null]])
            self.payAccount = nil;

        self.payObj = [jsonObject objectForKey: @"payObj"];
        if(self.payObj && [self.payObj isEqual:[NSNull null]])
            self.payObj = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
