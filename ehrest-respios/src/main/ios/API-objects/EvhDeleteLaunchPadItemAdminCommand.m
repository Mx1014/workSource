//
// EvhDeleteLaunchPadItemAdminCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhDeleteLaunchPadItemAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteLaunchPadItemAdminCommand
//

@implementation EvhDeleteLaunchPadItemAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteLaunchPadItemAdminCommand* obj = [EvhDeleteLaunchPadItemAdminCommand new];
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
