//
// EvhPayZuolinRefundCommand.m
//
#import "EvhPayZuolinRefundCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayZuolinRefundCommand
//

@implementation EvhPayZuolinRefundCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPayZuolinRefundCommand* obj = [EvhPayZuolinRefundCommand new];
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
    if(self.appKey)
        [jsonObject setObject: self.appKey forKey: @"appKey"];
    if(self.signature)
        [jsonObject setObject: self.signature forKey: @"signature"];
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
    if(self.nonce)
        [jsonObject setObject: self.nonce forKey: @"nonce"];
    if(self.crypto)
        [jsonObject setObject: self.crypto forKey: @"crypto"];
    if(self.refundOrderNo)
        [jsonObject setObject: self.refundOrderNo forKey: @"refundOrderNo"];
    if(self.orderNo)
        [jsonObject setObject: self.orderNo forKey: @"orderNo"];
    if(self.onlinePayStyleNo)
        [jsonObject setObject: self.onlinePayStyleNo forKey: @"onlinePayStyleNo"];
    if(self.orderType)
        [jsonObject setObject: self.orderType forKey: @"orderType"];
    if(self.refundAmount)
        [jsonObject setObject: self.refundAmount forKey: @"refundAmount"];
    if(self.refundMsg)
        [jsonObject setObject: self.refundMsg forKey: @"refundMsg"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appKey = [jsonObject objectForKey: @"appKey"];
        if(self.appKey && [self.appKey isEqual:[NSNull null]])
            self.appKey = nil;

        self.signature = [jsonObject objectForKey: @"signature"];
        if(self.signature && [self.signature isEqual:[NSNull null]])
            self.signature = nil;

        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        self.nonce = [jsonObject objectForKey: @"nonce"];
        if(self.nonce && [self.nonce isEqual:[NSNull null]])
            self.nonce = nil;

        self.crypto = [jsonObject objectForKey: @"crypto"];
        if(self.crypto && [self.crypto isEqual:[NSNull null]])
            self.crypto = nil;

        self.refundOrderNo = [jsonObject objectForKey: @"refundOrderNo"];
        if(self.refundOrderNo && [self.refundOrderNo isEqual:[NSNull null]])
            self.refundOrderNo = nil;

        self.orderNo = [jsonObject objectForKey: @"orderNo"];
        if(self.orderNo && [self.orderNo isEqual:[NSNull null]])
            self.orderNo = nil;

        self.onlinePayStyleNo = [jsonObject objectForKey: @"onlinePayStyleNo"];
        if(self.onlinePayStyleNo && [self.onlinePayStyleNo isEqual:[NSNull null]])
            self.onlinePayStyleNo = nil;

        self.orderType = [jsonObject objectForKey: @"orderType"];
        if(self.orderType && [self.orderType isEqual:[NSNull null]])
            self.orderType = nil;

        self.refundAmount = [jsonObject objectForKey: @"refundAmount"];
        if(self.refundAmount && [self.refundAmount isEqual:[NSNull null]])
            self.refundAmount = nil;

        self.refundMsg = [jsonObject objectForKey: @"refundMsg"];
        if(self.refundMsg && [self.refundMsg isEqual:[NSNull null]])
            self.refundMsg = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
