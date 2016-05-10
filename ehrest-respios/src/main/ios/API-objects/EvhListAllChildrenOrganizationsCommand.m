//
// EvhListAllChildrenOrganizationsCommand.m
//
#import "EvhListAllChildrenOrganizationsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAllChildrenOrganizationsCommand
//

@implementation EvhListAllChildrenOrganizationsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListAllChildrenOrganizationsCommand* obj = [EvhListAllChildrenOrganizationsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _groupTypes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.groupTypes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.groupTypes) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"groupTypes"];
    }
    if(self.naviFlag)
        [jsonObject setObject: self.naviFlag forKey: @"naviFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"groupTypes"];
            for(id itemJson in jsonArray) {
                [self.groupTypes addObject: itemJson];
            }
        }
        self.naviFlag = [jsonObject objectForKey: @"naviFlag"];
        if(self.naviFlag && [self.naviFlag isEqual:[NSNull null]])
            self.naviFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
