//
// EvhFavoriteBusinessesCommand.m
//
#import "EvhFavoriteBusinessesCommand.h"
#import "EvhFavoriteBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFavoriteBusinessesCommand
//

@implementation EvhFavoriteBusinessesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFavoriteBusinessesCommand* obj = [EvhFavoriteBusinessesCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _bizs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.bizs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhFavoriteBusinessDTO* item in self.bizs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"bizs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"bizs"];
            for(id itemJson in jsonArray) {
                EvhFavoriteBusinessDTO* item = [EvhFavoriteBusinessDTO new];
                
                [item fromJson: itemJson];
                [self.bizs addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
