//
// EvhListDoorAccessResponse.m
//
#import "EvhListDoorAccessResponse.h"
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListDoorAccessResponse
//

@implementation EvhListDoorAccessResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListDoorAccessResponse* obj = [EvhListDoorAccessResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _doors = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.doors) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhDoorAccessDTO* item in self.doors) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"doors"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"doors"];
            for(id itemJson in jsonArray) {
                EvhDoorAccessDTO* item = [EvhDoorAccessDTO new];
                
                [item fromJson: itemJson];
                [self.doors addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
