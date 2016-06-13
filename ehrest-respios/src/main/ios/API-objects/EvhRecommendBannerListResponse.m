//
// EvhRecommendBannerListResponse.m
//
#import "EvhRecommendBannerListResponse.h"
#import "EvhRecommendBannerInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendBannerListResponse
//

@implementation EvhRecommendBannerListResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecommendBannerListResponse* obj = [EvhRecommendBannerListResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _banners = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.banners) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRecommendBannerInfo* item in self.banners) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"banners"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"banners"];
            for(id itemJson in jsonArray) {
                EvhRecommendBannerInfo* item = [EvhRecommendBannerInfo new];
                
                [item fromJson: itemJson];
                [self.banners addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
