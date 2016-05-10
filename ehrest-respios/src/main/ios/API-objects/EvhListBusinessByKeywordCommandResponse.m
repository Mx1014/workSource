//
// EvhListBusinessByKeywordCommandResponse.m
//
#import "EvhListBusinessByKeywordCommandResponse.h"
#import "EvhBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBusinessByKeywordCommandResponse
//

@implementation EvhListBusinessByKeywordCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListBusinessByKeywordCommandResponse* obj = [EvhListBusinessByKeywordCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _list = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.list) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBusinessDTO* item in self.list) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"list"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"list"];
            for(id itemJson in jsonArray) {
                EvhBusinessDTO* item = [EvhBusinessDTO new];
                
                [item fromJson: itemJson];
                [self.list addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
