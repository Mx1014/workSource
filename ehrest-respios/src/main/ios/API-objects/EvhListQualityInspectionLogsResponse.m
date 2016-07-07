//
// EvhListQualityInspectionLogsResponse.m
//
#import "EvhListQualityInspectionLogsResponse.h"
#import "EvhQualityInspectionLogDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityInspectionLogsResponse
//

@implementation EvhListQualityInspectionLogsResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListQualityInspectionLogsResponse* obj = [EvhListQualityInspectionLogsResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _dtos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.dtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhQualityInspectionLogDTO* item in self.dtos) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"dtos"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"dtos"];
            for(id itemJson in jsonArray) {
                EvhQualityInspectionLogDTO* item = [EvhQualityInspectionLogDTO new];
                
                [item fromJson: itemJson];
                [self.dtos addObject: item];
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
