//
// EvhPaymentRankingCommand.m
//
#import "EvhPaymentRankingCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentRankingCommand
//

@implementation EvhPaymentRankingCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPaymentRankingCommand* obj = [EvhPaymentRankingCommand new];
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
    if(self.orderNo)
        [jsonObject setObject: self.orderNo forKey: @"orderNo"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.payStatus = [jsonObject objectForKey: @"payStatus"];
        if(self.payStatus && [self.payStatus isEqual:[NSNull null]])
            self.payStatus = nil;

        self.orderNo = [jsonObject objectForKey: @"orderNo"];
        if(self.orderNo && [self.orderNo isEqual:[NSNull null]])
            self.orderNo = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
