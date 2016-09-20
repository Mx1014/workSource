//
// EvhPmBillsDTO.m
//
#import "EvhPmBillsDTO.h"
#import "EvhFamilyBillingTransactionDTO.h"
#import "EvhPmBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillsDTO
//

@implementation EvhPmBillsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmBillsDTO* obj = [EvhPmBillsDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _payList = [NSMutableArray new];
        _billItems = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.entityId)
        [jsonObject setObject: self.entityId forKey: @"entityId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.startDate)
        [jsonObject setObject: self.startDate forKey: @"startDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.payDate)
        [jsonObject setObject: self.payDate forKey: @"payDate"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.dueAmount)
        [jsonObject setObject: self.dueAmount forKey: @"dueAmount"];
    if(self.oweAmount)
        [jsonObject setObject: self.oweAmount forKey: @"oweAmount"];
    if(self.payedAmount)
        [jsonObject setObject: self.payedAmount forKey: @"payedAmount"];
    if(self.waitPayAmount)
        [jsonObject setObject: self.waitPayAmount forKey: @"waitPayAmount"];
    if(self.totalAmount)
        [jsonObject setObject: self.totalAmount forKey: @"totalAmount"];
    if(self.payList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhFamilyBillingTransactionDTO* item in self.payList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"payList"];
    }
    if(self.billItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPmBillItemDTO* item in self.billItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"billItems"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.entityId = [jsonObject objectForKey: @"entityId"];
        if(self.entityId && [self.entityId isEqual:[NSNull null]])
            self.entityId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.startDate = [jsonObject objectForKey: @"startDate"];
        if(self.startDate && [self.startDate isEqual:[NSNull null]])
            self.startDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.payDate = [jsonObject objectForKey: @"payDate"];
        if(self.payDate && [self.payDate isEqual:[NSNull null]])
            self.payDate = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.dueAmount = [jsonObject objectForKey: @"dueAmount"];
        if(self.dueAmount && [self.dueAmount isEqual:[NSNull null]])
            self.dueAmount = nil;

        self.oweAmount = [jsonObject objectForKey: @"oweAmount"];
        if(self.oweAmount && [self.oweAmount isEqual:[NSNull null]])
            self.oweAmount = nil;

        self.payedAmount = [jsonObject objectForKey: @"payedAmount"];
        if(self.payedAmount && [self.payedAmount isEqual:[NSNull null]])
            self.payedAmount = nil;

        self.waitPayAmount = [jsonObject objectForKey: @"waitPayAmount"];
        if(self.waitPayAmount && [self.waitPayAmount isEqual:[NSNull null]])
            self.waitPayAmount = nil;

        self.totalAmount = [jsonObject objectForKey: @"totalAmount"];
        if(self.totalAmount && [self.totalAmount isEqual:[NSNull null]])
            self.totalAmount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"payList"];
            for(id itemJson in jsonArray) {
                EvhFamilyBillingTransactionDTO* item = [EvhFamilyBillingTransactionDTO new];
                
                [item fromJson: itemJson];
                [self.payList addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"billItems"];
            for(id itemJson in jsonArray) {
                EvhPmBillItemDTO* item = [EvhPmBillItemDTO new];
                
                [item fromJson: itemJson];
                [self.billItems addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
