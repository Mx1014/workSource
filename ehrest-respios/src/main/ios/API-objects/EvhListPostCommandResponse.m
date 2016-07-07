//
// EvhListPostCommandResponse.m
//
#import "EvhListPostCommandResponse.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPostCommandResponse
//

@implementation EvhListPostCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPostCommandResponse* obj = [EvhListPostCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _posts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.posts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPostDTO* item in self.posts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"posts"];
    }
    if(self.commentCount)
        [jsonObject setObject: self.commentCount forKey: @"commentCount"];
    if(self.keywords)
        [jsonObject setObject: self.keywords forKey: @"keywords"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"posts"];
            for(id itemJson in jsonArray) {
                EvhPostDTO* item = [EvhPostDTO new];
                
                [item fromJson: itemJson];
                [self.posts addObject: item];
            }
        }
        self.commentCount = [jsonObject objectForKey: @"commentCount"];
        if(self.commentCount && [self.commentCount isEqual:[NSNull null]])
            self.commentCount = nil;

        self.keywords = [jsonObject objectForKey: @"keywords"];
        if(self.keywords && [self.keywords isEqual:[NSNull null]])
            self.keywords = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
