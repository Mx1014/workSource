//
// EvhGetItemListCommandResponse.m
//
#import "EvhGetItemListCommandResponse.h"
#import "EvhRentalv2SiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetItemListCommandResponse
//

@implementation EvhGetItemListCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetItemListCommandResponse* obj = [EvhGetItemListCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _siteItems = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.siteItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2SiteItemDTO* item in self.siteItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"siteItems"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"siteItems"];
            for(id itemJson in jsonArray) {
                EvhRentalv2SiteItemDTO* item = [EvhRentalv2SiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.siteItems addObject: item];
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
