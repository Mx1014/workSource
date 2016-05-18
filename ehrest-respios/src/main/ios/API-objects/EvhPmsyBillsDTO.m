//
// EvhPmsyBillsDTO.m
//
#import "EvhPmsyBillsDTO.h"
#import "EvhPmsyBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyBillsDTO
//

@implementation EvhPmsyBillsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmsyBillsDTO* obj = [EvhPmsyBillsDTO new];
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
    if(self.billDateStr)
        [jsonObject setObject: self.billDateStr forKey: @"billDateStr"];
    if(self.monthlyReceivableAmount)
        [jsonObject setObject: self.monthlyReceivableAmount forKey: @"monthlyReceivableAmount"];
    if(self.monthlyDebtAmount)
        [jsonObject setObject: self.monthlyDebtAmount forKey: @"monthlyDebtAmount"];
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPmsyBillItemDTO* item in self.requests) {
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
        self.billDateStr = [jsonObject objectForKey: @"billDateStr"];
        if(self.billDateStr && [self.billDateStr isEqual:[NSNull null]])
            self.billDateStr = nil;

        self.monthlyReceivableAmount = [jsonObject objectForKey: @"monthlyReceivableAmount"];
        if(self.monthlyReceivableAmount && [self.monthlyReceivableAmount isEqual:[NSNull null]])
            self.monthlyReceivableAmount = nil;

        self.monthlyDebtAmount = [jsonObject objectForKey: @"monthlyDebtAmount"];
        if(self.monthlyDebtAmount && [self.monthlyDebtAmount isEqual:[NSNull null]])
            self.monthlyDebtAmount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhPmsyBillItemDTO* item = [EvhPmsyBillItemDTO new];
                
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
