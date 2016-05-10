//
// EvhGetAllCardDescriptDTO.m
//
#import "EvhGetAllCardDescriptDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetAllCardDescriptDTO
//

@implementation EvhGetAllCardDescriptDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetAllCardDescriptDTO* obj = [EvhGetAllCardDescriptDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _cardDescript = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.cardDescript) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.cardDescript) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"cardDescript"];
    }
    if(self.success)
        [jsonObject setObject: self.success forKey: @"success"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"cardDescript"];
            for(id itemJson in jsonArray) {
                [self.cardDescript addObject: itemJson];
            }
        }
        self.success = [jsonObject objectForKey: @"success"];
        if(self.success && [self.success isEqual:[NSNull null]])
            self.success = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
