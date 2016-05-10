//
// EvhDeleteLaunchPadItemCommand.m
//
#import "EvhDeleteLaunchPadItemCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteLaunchPadItemCommand
//

@implementation EvhDeleteLaunchPadItemCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteLaunchPadItemCommand* obj = [EvhDeleteLaunchPadItemCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.itemIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.itemIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"itemIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemIds"];
            for(id itemJson in jsonArray) {
                [self.itemIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
