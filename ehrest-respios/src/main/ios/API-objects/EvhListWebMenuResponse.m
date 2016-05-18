//
// EvhListWebMenuResponse.m
//
#import "EvhListWebMenuResponse.h"
#import "EvhWebMenuDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuResponse
//

@implementation EvhListWebMenuResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListWebMenuResponse* obj = [EvhListWebMenuResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _menus = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.menus) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhWebMenuDTO* item in self.menus) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"menus"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"menus"];
            for(id itemJson in jsonArray) {
                EvhWebMenuDTO* item = [EvhWebMenuDTO new];
                
                [item fromJson: itemJson];
                [self.menus addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
