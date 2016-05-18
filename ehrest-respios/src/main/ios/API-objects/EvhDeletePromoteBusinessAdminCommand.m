//
// EvhDeletePromoteBusinessAdminCommand.m
//
#import "EvhDeletePromoteBusinessAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeletePromoteBusinessAdminCommand
//

@implementation EvhDeletePromoteBusinessAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeletePromoteBusinessAdminCommand* obj = [EvhDeletePromoteBusinessAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _ids = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ids) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.ids) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"ids"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"ids"];
            for(id itemJson in jsonArray) {
                [self.ids addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
