//
// EvhExpressionDTO.m
//
#import "EvhExpressionDTO.h"
#import "EvhRepeatExpressionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExpressionDTO
//

@implementation EvhExpressionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhExpressionDTO* obj = [EvhExpressionDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _expression = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.expression) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRepeatExpressionDTO* item in self.expression) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"expression"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"expression"];
            for(id itemJson in jsonArray) {
                EvhRepeatExpressionDTO* item = [EvhRepeatExpressionDTO new];
                
                [item fromJson: itemJson];
                [self.expression addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
