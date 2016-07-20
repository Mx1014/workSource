//
// EvhGetOpPromotionActivityByPromotionId.m
//
#import "EvhGetOpPromotionActivityByPromotionId.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetOpPromotionActivityByPromotionId
//

@implementation EvhGetOpPromotionActivityByPromotionId

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetOpPromotionActivityByPromotionId* obj = [EvhGetOpPromotionActivityByPromotionId new];
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
    if(self.promotionId)
        [jsonObject setObject: self.promotionId forKey: @"promotionId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.promotionId = [jsonObject objectForKey: @"promotionId"];
        if(self.promotionId && [self.promotionId isEqual:[NSNull null]])
            self.promotionId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
