//
// EvhSearchTopicAdminCommandResponse.m
//
#import "EvhSearchTopicAdminCommandResponse.h"
#import "EvhPostAdminDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTopicAdminCommandResponse
//

@implementation EvhSearchTopicAdminCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchTopicAdminCommandResponse* obj = [EvhSearchTopicAdminCommandResponse new];
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
        for(EvhPostAdminDTO* item in self.posts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"posts"];
    }
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
                EvhPostAdminDTO* item = [EvhPostAdminDTO new];
                
                [item fromJson: itemJson];
                [self.posts addObject: item];
            }
        }
        self.keywords = [jsonObject objectForKey: @"keywords"];
        if(self.keywords && [self.keywords isEqual:[NSNull null]])
            self.keywords = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
