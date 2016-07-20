//
// EvhListPunchCountCommandResponse.m
//
#import "EvhListPunchCountCommandResponse.h"
#import "EvhPunchCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchCountCommandResponse
//

@implementation EvhListPunchCountCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPunchCountCommandResponse* obj = [EvhListPunchCountCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _punchCountList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.punchCountList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchCountDTO* item in self.punchCountList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchCountList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchCountList"];
            for(id itemJson in jsonArray) {
                EvhPunchCountDTO* item = [EvhPunchCountDTO new];
                
                [item fromJson: itemJson];
                [self.punchCountList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
