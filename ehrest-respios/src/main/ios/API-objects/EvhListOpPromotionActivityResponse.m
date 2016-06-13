//
// EvhListOpPromotionActivityResponse.m
//
#import "EvhListOpPromotionActivityResponse.h"
#import "EvhOpPromotionActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOpPromotionActivityResponse
//

@implementation EvhListOpPromotionActivityResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListOpPromotionActivityResponse* obj = [EvhListOpPromotionActivityResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _promotions = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.promotions) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOpPromotionActivityDTO* item in self.promotions) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"promotions"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"promotions"];
            for(id itemJson in jsonArray) {
                EvhOpPromotionActivityDTO* item = [EvhOpPromotionActivityDTO new];
                
                [item fromJson: itemJson];
                [self.promotions addObject: item];
            }
        }
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
