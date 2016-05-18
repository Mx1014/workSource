//
// EvhPmsyBillItemDTO.m
//
#import "EvhPmsyBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyBillItemDTO
//

@implementation EvhPmsyBillItemDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmsyBillItemDTO* obj = [EvhPmsyBillItemDTO new];
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
    if(self.billDateStr)
        [jsonObject setObject: self.billDateStr forKey: @"billDateStr"];
    if(self.receivableAmount)
        [jsonObject setObject: self.receivableAmount forKey: @"receivableAmount"];
    if(self.debtAmount)
        [jsonObject setObject: self.debtAmount forKey: @"debtAmount"];
    if(self.customerId)
        [jsonObject setObject: self.customerId forKey: @"customerId"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        self.billDateStr = [jsonObject objectForKey: @"billDateStr"];
        if(self.billDateStr && [self.billDateStr isEqual:[NSNull null]])
            self.billDateStr = nil;

        self.receivableAmount = [jsonObject objectForKey: @"receivableAmount"];
        if(self.receivableAmount && [self.receivableAmount isEqual:[NSNull null]])
            self.receivableAmount = nil;

        self.debtAmount = [jsonObject objectForKey: @"debtAmount"];
        if(self.debtAmount && [self.debtAmount isEqual:[NSNull null]])
            self.debtAmount = nil;

        self.customerId = [jsonObject objectForKey: @"customerId"];
        if(self.customerId && [self.customerId isEqual:[NSNull null]])
            self.customerId = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
