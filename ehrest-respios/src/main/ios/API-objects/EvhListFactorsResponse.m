//
// EvhListFactorsResponse.m
//
#import "EvhListFactorsResponse.h"
#import "EvhFactorsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFactorsResponse
//

@implementation EvhListFactorsResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListFactorsResponse* obj = [EvhListFactorsResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _factorsdto = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.factorsdto) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhFactorsDTO* item in self.factorsdto) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"factorsdto"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"factorsdto"];
            for(id itemJson in jsonArray) {
                EvhFactorsDTO* item = [EvhFactorsDTO new];
                
                [item fromJson: itemJson];
                [self.factorsdto addObject: item];
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
