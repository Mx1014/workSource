//
// EvhPmsyBillsResponse.m
//
#import "EvhPmsyBillsResponse.h"
#import "EvhPmsyBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyBillsResponse
//

@implementation EvhPmsyBillsResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmsyBillsResponse* obj = [EvhPmsyBillsResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _requests = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.customerId)
        [jsonObject setObject: self.customerId forKey: @"customerId"];
    if(self.projectId)
        [jsonObject setObject: self.projectId forKey: @"projectId"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.payerId)
        [jsonObject setObject: self.payerId forKey: @"payerId"];
    if(self.monthCount)
        [jsonObject setObject: self.monthCount forKey: @"monthCount"];
    if(self.totalAmount)
        [jsonObject setObject: self.totalAmount forKey: @"totalAmount"];
    if(self.billTip)
        [jsonObject setObject: self.billTip forKey: @"billTip"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPmsyBillsDTO* item in self.requests) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"requests"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.customerId = [jsonObject objectForKey: @"customerId"];
        if(self.customerId && [self.customerId isEqual:[NSNull null]])
            self.customerId = nil;

        self.projectId = [jsonObject objectForKey: @"projectId"];
        if(self.projectId && [self.projectId isEqual:[NSNull null]])
            self.projectId = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.payerId = [jsonObject objectForKey: @"payerId"];
        if(self.payerId && [self.payerId isEqual:[NSNull null]])
            self.payerId = nil;

        self.monthCount = [jsonObject objectForKey: @"monthCount"];
        if(self.monthCount && [self.monthCount isEqual:[NSNull null]])
            self.monthCount = nil;

        self.totalAmount = [jsonObject objectForKey: @"totalAmount"];
        if(self.totalAmount && [self.totalAmount isEqual:[NSNull null]])
            self.totalAmount = nil;

        self.billTip = [jsonObject objectForKey: @"billTip"];
        if(self.billTip && [self.billTip isEqual:[NSNull null]])
            self.billTip = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhPmsyBillsDTO* item = [EvhPmsyBillsDTO new];
                
                [item fromJson: itemJson];
                [self.requests addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
