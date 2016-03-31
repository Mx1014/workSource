//
// EvhSetFollowedFamilyAliasCommand.m
// generated at 2016-03-31 11:07:27 
//
#import "EvhSetFollowedFamilyAliasCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetFollowedFamilyAliasCommand
//

@implementation EvhSetFollowedFamilyAliasCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetFollowedFamilyAliasCommand* obj = [EvhSetFollowedFamilyAliasCommand new];
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
