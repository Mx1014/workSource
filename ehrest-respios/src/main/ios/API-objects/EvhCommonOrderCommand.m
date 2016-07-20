//
// EvhCommonOrderCommand.m
//
#import "EvhCommonOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommonOrderCommand
//

@implementation EvhCommonOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommonOrderCommand* obj = [EvhCommonOrderCommand new];
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
    if(self.orderType)
        [jsonObject setObject: self.orderType forKey: @"orderType"];
    if(self.totalFee)
        [jsonObject setObject: self.totalFee forKey: @"totalFee"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.body)
        [jsonObject setObject: self.body forKey: @"body"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orderNo = [jsonObject objectForKey: @"orderNo"];
        if(self.orderNo && [self.orderNo isEqual:[NSNull null]])
            self.orderNo = nil;

        self.orderType = [jsonObject objectForKey: @"orderType"];
        if(self.orderType && [self.orderType isEqual:[NSNull null]])
            self.orderType = nil;

        self.totalFee = [jsonObject objectForKey: @"totalFee"];
        if(self.totalFee && [self.totalFee isEqual:[NSNull null]])
            self.totalFee = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.body = [jsonObject objectForKey: @"body"];
        if(self.body && [self.body isEqual:[NSNull null]])
            self.body = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
