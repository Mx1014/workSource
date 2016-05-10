//
// EvhListCommunityByKeywordCommand.m
//
#import "EvhListCommunityByKeywordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunityByKeywordCommand
//

@implementation EvhListCommunityByKeywordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListCommunityByKeywordCommand* obj = [EvhListCommunityByKeywordCommand new];
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
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
