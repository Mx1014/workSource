//
// EvhCreatePmsyBillOrderCommand.m
//
#import "EvhCreatePmsyBillOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatePmsyBillOrderCommand
//

@implementation EvhCreatePmsyBillOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreatePmsyBillOrderCommand* obj = [EvhCreatePmsyBillOrderCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _billIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.projectId)
        [jsonObject setObject: self.projectId forKey: @"projectId"];
    if(self.customerId)
        [jsonObject setObject: self.customerId forKey: @"customerId"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.orderAmount)
        [jsonObject setObject: self.orderAmount forKey: @"orderAmount"];
    if(self.paidType)
        [jsonObject setObject: self.paidType forKey: @"paidType"];
    if(self.billIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.billIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"billIds"];
    }
    if(self.pmPayerId)
        [jsonObject setObject: self.pmPayerId forKey: @"pmPayerId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.projectId = [jsonObject objectForKey: @"projectId"];
        if(self.projectId && [self.projectId isEqual:[NSNull null]])
            self.projectId = nil;

        self.customerId = [jsonObject objectForKey: @"customerId"];
        if(self.customerId && [self.customerId isEqual:[NSNull null]])
            self.customerId = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.orderAmount = [jsonObject objectForKey: @"orderAmount"];
        if(self.orderAmount && [self.orderAmount isEqual:[NSNull null]])
            self.orderAmount = nil;

        self.paidType = [jsonObject objectForKey: @"paidType"];
        if(self.paidType && [self.paidType isEqual:[NSNull null]])
            self.paidType = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"billIds"];
            for(id itemJson in jsonArray) {
                [self.billIds addObject: itemJson];
            }
        }
        self.pmPayerId = [jsonObject objectForKey: @"pmPayerId"];
        if(self.pmPayerId && [self.pmPayerId isEqual:[NSNull null]])
            self.pmPayerId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
