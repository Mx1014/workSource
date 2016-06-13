//
// EvhPmManagementsDTO.m
//
#import "EvhPmManagementsDTO.h"
#import "EvhPmBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmManagementsDTO
//

@implementation EvhPmManagementsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmManagementsDTO* obj = [EvhPmManagementsDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _buildings = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.buildings) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPmBuildingDTO* item in self.buildings) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"buildings"];
    }
    if(self.pmName)
        [jsonObject setObject: self.pmName forKey: @"pmName"];
    if(self.plate)
        [jsonObject setObject: self.plate forKey: @"plate"];
    if(self.pmId)
        [jsonObject setObject: self.pmId forKey: @"pmId"];
    if(self.isAll)
        [jsonObject setObject: self.isAll forKey: @"isAll"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"buildings"];
            for(id itemJson in jsonArray) {
                EvhPmBuildingDTO* item = [EvhPmBuildingDTO new];
                
                [item fromJson: itemJson];
                [self.buildings addObject: item];
            }
        }
        self.pmName = [jsonObject objectForKey: @"pmName"];
        if(self.pmName && [self.pmName isEqual:[NSNull null]])
            self.pmName = nil;

        self.plate = [jsonObject objectForKey: @"plate"];
        if(self.plate && [self.plate isEqual:[NSNull null]])
            self.plate = nil;

        self.pmId = [jsonObject objectForKey: @"pmId"];
        if(self.pmId && [self.pmId isEqual:[NSNull null]])
            self.pmId = nil;

        self.isAll = [jsonObject objectForKey: @"isAll"];
        if(self.isAll && [self.isAll isEqual:[NSNull null]])
            self.isAll = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
