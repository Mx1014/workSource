//
// EvhFollowFamilyCommand.m
//
#import "EvhFollowFamilyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFollowFamilyCommand
//

@implementation EvhFollowFamilyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFollowFamilyCommand* obj = [EvhFollowFamilyCommand new];
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
    if(self.aliasName)
        [jsonObject setObject: self.aliasName forKey: @"aliasName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.aliasName = [jsonObject objectForKey: @"aliasName"];
        if(self.aliasName && [self.aliasName isEqual:[NSNull null]])
            self.aliasName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
