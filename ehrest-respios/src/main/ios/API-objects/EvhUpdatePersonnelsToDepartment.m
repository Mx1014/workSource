//
// EvhUpdatePersonnelsToDepartment.m
//
#import "EvhUpdatePersonnelsToDepartment.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePersonnelsToDepartment
//

@implementation EvhUpdatePersonnelsToDepartment

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdatePersonnelsToDepartment* obj = [EvhUpdatePersonnelsToDepartment new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
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
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
