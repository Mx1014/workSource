//
// EvhGetBusinessesByCategoryCommandResponse.m
//
#import "EvhGetBusinessesByCategoryCommandResponse.h"
#import "EvhBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBusinessesByCategoryCommandResponse
//

@implementation EvhGetBusinessesByCategoryCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetBusinessesByCategoryCommandResponse* obj = [EvhGetBusinessesByCategoryCommandResponse new];
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
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBusinessDTO* item in self.requests) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"requests"];
    }
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.favoriteCount)
        [jsonObject setObject: self.favoriteCount forKey: @"favoriteCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhBusinessDTO* item = [EvhBusinessDTO new];
                
                [item fromJson: itemJson];
                [self.requests addObject: item];
            }
        }
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        self.favoriteCount = [jsonObject objectForKey: @"favoriteCount"];
        if(self.favoriteCount && [self.favoriteCount isEqual:[NSNull null]])
            self.favoriteCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
