//
// EvhOpPromotionRangePriceData.m
//
#import "EvhOpPromotionRangePriceData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionRangePriceData
//

@implementation EvhOpPromotionRangePriceData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpPromotionRangePriceData* obj = [EvhOpPromotionRangePriceData new];
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
    if(self.from)
        [jsonObject setObject: self.from forKey: @"from"];
    if(self.to)
        [jsonObject setObject: self.to forKey: @"to"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.from = [jsonObject objectForKey: @"from"];
        if(self.from && [self.from isEqual:[NSNull null]])
            self.from = nil;

        self.to = [jsonObject objectForKey: @"to"];
        if(self.to && [self.to isEqual:[NSNull null]])
            self.to = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
