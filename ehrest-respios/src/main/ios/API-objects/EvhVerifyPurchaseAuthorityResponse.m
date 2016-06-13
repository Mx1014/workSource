//
// EvhVerifyPurchaseAuthorityResponse.m
//
#import "EvhVerifyPurchaseAuthorityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPurchaseAuthorityResponse
//

@implementation EvhVerifyPurchaseAuthorityResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyPurchaseAuthorityResponse* obj = [EvhVerifyPurchaseAuthorityResponse new];
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
    if(self.purchaseAuthority)
        [jsonObject setObject: self.purchaseAuthority forKey: @"purchaseAuthority"];
    if(self.enterpriseActiveAccountCount)
        [jsonObject setObject: self.enterpriseActiveAccountCount forKey: @"enterpriseActiveAccountCount"];
    if(self.enterpriseAccountCount)
        [jsonObject setObject: self.enterpriseAccountCount forKey: @"enterpriseAccountCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.purchaseAuthority = [jsonObject objectForKey: @"purchaseAuthority"];
        if(self.purchaseAuthority && [self.purchaseAuthority isEqual:[NSNull null]])
            self.purchaseAuthority = nil;

        self.enterpriseActiveAccountCount = [jsonObject objectForKey: @"enterpriseActiveAccountCount"];
        if(self.enterpriseActiveAccountCount && [self.enterpriseActiveAccountCount isEqual:[NSNull null]])
            self.enterpriseActiveAccountCount = nil;

        self.enterpriseAccountCount = [jsonObject objectForKey: @"enterpriseAccountCount"];
        if(self.enterpriseAccountCount && [self.enterpriseAccountCount isEqual:[NSNull null]])
            self.enterpriseAccountCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
