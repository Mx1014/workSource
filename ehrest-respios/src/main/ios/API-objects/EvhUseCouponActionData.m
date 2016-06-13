//
// EvhUseCouponActionData.m
//
#import "EvhUseCouponActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUseCouponActionData
//

@implementation EvhUseCouponActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUseCouponActionData* obj = [EvhUseCouponActionData new];
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
    if(self.couponId)
        [jsonObject setObject: self.couponId forKey: @"couponId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.couponId = [jsonObject objectForKey: @"couponId"];
        if(self.couponId && [self.couponId isEqual:[NSNull null]])
            self.couponId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
