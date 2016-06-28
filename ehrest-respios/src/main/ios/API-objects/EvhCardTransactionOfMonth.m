//
// EvhCardTransactionOfMonth.m
//
#import "EvhCardTransactionOfMonth.h"
#import "EvhCardTransactionFromVendorDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardTransactionOfMonth
//

@implementation EvhCardTransactionOfMonth

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCardTransactionOfMonth* obj = [EvhCardTransactionOfMonth new];
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
    if(self.date)
        [jsonObject setObject: self.date forKey: @"date"];
    if(self.consumeAmount)
        [jsonObject setObject: self.consumeAmount forKey: @"consumeAmount"];
    if(self.rechargeAmount)
        [jsonObject setObject: self.rechargeAmount forKey: @"rechargeAmount"];
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCardTransactionFromVendorDTO* item in self.requests) {
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
        self.date = [jsonObject objectForKey: @"date"];
        if(self.date && [self.date isEqual:[NSNull null]])
            self.date = nil;

        self.consumeAmount = [jsonObject objectForKey: @"consumeAmount"];
        if(self.consumeAmount && [self.consumeAmount isEqual:[NSNull null]])
            self.consumeAmount = nil;

        self.rechargeAmount = [jsonObject objectForKey: @"rechargeAmount"];
        if(self.rechargeAmount && [self.rechargeAmount isEqual:[NSNull null]])
            self.rechargeAmount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhCardTransactionFromVendorDTO* item = [EvhCardTransactionFromVendorDTO new];
                
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
