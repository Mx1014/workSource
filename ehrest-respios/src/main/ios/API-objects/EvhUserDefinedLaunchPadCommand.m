//
// EvhUserDefinedLaunchPadCommand.m
//
#import "EvhUserDefinedLaunchPadCommand.h"
#import "EvhItem.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserDefinedLaunchPadCommand
//

@implementation EvhUserDefinedLaunchPadCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserDefinedLaunchPadCommand* obj = [EvhUserDefinedLaunchPadCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _Items = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.Items) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhItem* item in self.Items) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"Items"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"Items"];
            for(id itemJson in jsonArray) {
                EvhItem* item = [EvhItem new];
                
                [item fromJson: itemJson];
                [self.Items addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
