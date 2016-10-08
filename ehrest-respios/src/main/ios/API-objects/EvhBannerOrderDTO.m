//
// EvhBannerOrderDTO.m
//
#import "EvhBannerOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerOrderDTO
//

@implementation EvhBannerOrderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBannerOrderDTO* obj = [EvhBannerOrderDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.bannerId)
        [jsonObject setObject: self.bannerId forKey: @"bannerId"];
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.vendorOrderTag)
        [jsonObject setObject: self.vendorOrderTag forKey: @"vendorOrderTag"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.purchaseTime)
        [jsonObject setObject: self.purchaseTime forKey: @"purchaseTime"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.bannerId = [jsonObject objectForKey: @"bannerId"];
        if(self.bannerId && [self.bannerId isEqual:[NSNull null]])
            self.bannerId = nil;

        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.vendorOrderTag = [jsonObject objectForKey: @"vendorOrderTag"];
        if(self.vendorOrderTag && [self.vendorOrderTag isEqual:[NSNull null]])
            self.vendorOrderTag = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.purchaseTime = [jsonObject objectForKey: @"purchaseTime"];
        if(self.purchaseTime && [self.purchaseTime isEqual:[NSNull null]])
            self.purchaseTime = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
