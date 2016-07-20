//
// EvhGetResourceTypeListResponse.m
//
#import "EvhGetResourceTypeListResponse.h"
#import "EvhResourceTypeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetResourceTypeListResponse
//

@implementation EvhGetResourceTypeListResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetResourceTypeListResponse* obj = [EvhGetResourceTypeListResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _resourceTypes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.resourceTypes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhResourceTypeDTO* item in self.resourceTypes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"resourceTypes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"resourceTypes"];
            for(id itemJson in jsonArray) {
                EvhResourceTypeDTO* item = [EvhResourceTypeDTO new];
                
                [item fromJson: itemJson];
                [self.resourceTypes addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
