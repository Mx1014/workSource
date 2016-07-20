//
// EvhRecommendBannerInfo.m
//
#import "EvhRecommendBannerInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendBannerInfo
//

@implementation EvhRecommendBannerInfo

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecommendBannerInfo* obj = [EvhRecommendBannerInfo new];
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
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.redirectUrl)
        [jsonObject setObject: self.redirectUrl forKey: @"redirectUrl"];
    if(self.posterUrl)
        [jsonObject setObject: self.posterUrl forKey: @"posterUrl"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.redirectUrl = [jsonObject objectForKey: @"redirectUrl"];
        if(self.redirectUrl && [self.redirectUrl isEqual:[NSNull null]])
            self.redirectUrl = nil;

        self.posterUrl = [jsonObject objectForKey: @"posterUrl"];
        if(self.posterUrl && [self.posterUrl isEqual:[NSNull null]])
            self.posterUrl = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
