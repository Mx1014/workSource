//
// EvhYellowPageListResponse.m
//
#import "EvhYellowPageListResponse.h"
#import "EvhYellowPageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhYellowPageListResponse
//

@implementation EvhYellowPageListResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhYellowPageListResponse* obj = [EvhYellowPageListResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _yellowPages = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.yellowPages) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhYellowPageDTO* item in self.yellowPages) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"yellowPages"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"yellowPages"];
            for(id itemJson in jsonArray) {
                EvhYellowPageDTO* item = [EvhYellowPageDTO new];
                
                [item fromJson: itemJson];
                [self.yellowPages addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
