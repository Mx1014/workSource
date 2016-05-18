//
// EvhGetLaunchPadItemsByKeywordAdminCommandResponse.m
//
#import "EvhGetLaunchPadItemsByKeywordAdminCommandResponse.h"
#import "EvhLaunchPadItemAdminDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsByKeywordAdminCommandResponse
//

@implementation EvhGetLaunchPadItemsByKeywordAdminCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetLaunchPadItemsByKeywordAdminCommandResponse* obj = [EvhGetLaunchPadItemsByKeywordAdminCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _launchPadItems = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.launchPadItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhLaunchPadItemAdminDTO* item in self.launchPadItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"launchPadItems"];
    }
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"launchPadItems"];
            for(id itemJson in jsonArray) {
                EvhLaunchPadItemAdminDTO* item = [EvhLaunchPadItemAdminDTO new];
                
                [item fromJson: itemJson];
                [self.launchPadItems addObject: item];
            }
        }
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
