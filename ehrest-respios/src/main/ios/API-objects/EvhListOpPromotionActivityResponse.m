//
// EvhListOpPromotionActivityResponse.m
//
#import "EvhListOpPromotionActivityResponse.h"

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
