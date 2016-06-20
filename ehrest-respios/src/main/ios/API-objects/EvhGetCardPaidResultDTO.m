//
// EvhGetCardPaidResultDTO.m
//
#import "EvhGetCardPaidResultDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCardPaidResultDTO
//

@implementation EvhGetCardPaidResultDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetCardPaidResultDTO* obj = [EvhGetCardPaidResultDTO new];
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
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.merchantNo)
        [jsonObject setObject: self.merchantNo forKey: @"merchantNo"];
    if(self.merchantName)
        [jsonObject setObject: self.merchantName forKey: @"merchantName"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.disAmount)
        [jsonObject setObject: self.disAmount forKey: @"disAmount"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.transactionTime)
        [jsonObject setObject: self.transactionTime forKey: @"transactionTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.merchantNo = [jsonObject objectForKey: @"merchantNo"];
        if(self.merchantNo && [self.merchantNo isEqual:[NSNull null]])
            self.merchantNo = nil;

        self.merchantName = [jsonObject objectForKey: @"merchantName"];
        if(self.merchantName && [self.merchantName isEqual:[NSNull null]])
            self.merchantName = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.disAmount = [jsonObject objectForKey: @"disAmount"];
        if(self.disAmount && [self.disAmount isEqual:[NSNull null]])
            self.disAmount = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.transactionTime = [jsonObject objectForKey: @"transactionTime"];
        if(self.transactionTime && [self.transactionTime isEqual:[NSNull null]])
            self.transactionTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
