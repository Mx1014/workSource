//
// EvhPmBillForOrderNoDTO.m
//
#import "EvhPmBillForOrderNoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillForOrderNoDTO
//

@implementation EvhPmBillForOrderNoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmBillForOrderNoDTO* obj = [EvhPmBillForOrderNoDTO new];
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
    if(self.payAmount)
        [jsonObject setObject: self.payAmount forKey: @"payAmount"];
    if(self.billName)
        [jsonObject setObject: self.billName forKey: @"billName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orderNo = [jsonObject objectForKey: @"orderNo"];
        if(self.orderNo && [self.orderNo isEqual:[NSNull null]])
            self.orderNo = nil;

        self.payAmount = [jsonObject objectForKey: @"payAmount"];
        if(self.payAmount && [self.payAmount isEqual:[NSNull null]])
            self.payAmount = nil;

        self.billName = [jsonObject objectForKey: @"billName"];
        if(self.billName && [self.billName isEqual:[NSNull null]])
            self.billName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
