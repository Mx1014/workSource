//
// EvhListQualityInspectionTasksResponse.m
//
#import "EvhListQualityInspectionTasksResponse.h"
#import "EvhQualityInspectionTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityInspectionTasksResponse
//

@implementation EvhListQualityInspectionTasksResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListQualityInspectionTasksResponse* obj = [EvhListQualityInspectionTasksResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _tasks = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.tasks) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhQualityInspectionTaskDTO* item in self.tasks) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"tasks"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"tasks"];
            for(id itemJson in jsonArray) {
                EvhQualityInspectionTaskDTO* item = [EvhQualityInspectionTaskDTO new];
                
                [item fromJson: itemJson];
                [self.tasks addObject: item];
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
