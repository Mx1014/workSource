//
// EvhListNeighborUsersCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhListNeighborUsersCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNeighborUsersCommand
//

@implementation EvhListNeighborUsersCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNeighborUsersCommand* obj = [EvhListNeighborUsersCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    [super toJson: jsonObject];
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
    if(self.isPinyin)
        [jsonObject setObject: self.isPinyin forKey: @"isPinyin"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        self.isPinyin = [jsonObject objectForKey: @"isPinyin"];
        if(self.isPinyin && [self.isPinyin isEqual:[NSNull null]])
            self.isPinyin = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
