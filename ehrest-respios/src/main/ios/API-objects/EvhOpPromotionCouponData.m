//
// EvhOpPromotionCouponData.m
//
#import "EvhOpPromotionCouponData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionCouponData
//

@implementation EvhOpPromotionCouponData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpPromotionCouponData* obj = [EvhOpPromotionCouponData new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _couponList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
    if(self.couponList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.couponList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"couponList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"couponList"];
            for(id itemJson in jsonArray) {
                [self.couponList addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
