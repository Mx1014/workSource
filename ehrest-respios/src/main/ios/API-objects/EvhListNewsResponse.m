//
// EvhListNewsResponse.m
//
#import "EvhListNewsResponse.h"
#import "EvhBriefNewsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNewsResponse
//

@implementation EvhListNewsResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNewsResponse* obj = [EvhListNewsResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _theNewsList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.theNewsList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBriefNewsDTO* item in self.theNewsList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"newsList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"newsList"];
            for(id itemJson in jsonArray) {
                EvhBriefNewsDTO* item = [EvhBriefNewsDTO new];
                
                [item fromJson: itemJson];
                [self.theNewsList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
