//
// EvhPmManagementsResponse.m
//
#import "EvhPmManagementsResponse.h"
#import "EvhPmManagementsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmManagementsResponse
//

@implementation EvhPmManagementsResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmManagementsResponse* obj = [EvhPmManagementsResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _pmManagement = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.pmManagement) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPmManagementsDTO* item in self.pmManagement) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"pmManagement"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"pmManagement"];
            for(id itemJson in jsonArray) {
                EvhPmManagementsDTO* item = [EvhPmManagementsDTO new];
                
                [item fromJson: itemJson];
                [self.pmManagement addObject: item];
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
