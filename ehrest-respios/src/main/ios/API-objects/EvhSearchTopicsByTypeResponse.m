//
// EvhSearchTopicsByTypeResponse.m
//
#import "EvhSearchTopicsByTypeResponse.h"
#import "EvhOrganizationTaskDTO2.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTopicsByTypeResponse
//

@implementation EvhSearchTopicsByTypeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchTopicsByTypeResponse* obj = [EvhSearchTopicsByTypeResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _requests = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationTaskDTO2* item in self.requests) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"requests"];
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
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhOrganizationTaskDTO2* item = [EvhOrganizationTaskDTO2 new];
                
                [item fromJson: itemJson];
                [self.requests addObject: item];
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
