//
// EvhListRegionCommand.m
//
#import "EvhListRegionCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRegionCommand
//

@implementation EvhListRegionCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListRegionCommand* obj = [EvhListRegionCommand new];
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
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.scope)
        [jsonObject setObject: self.scope forKey: @"scope"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.sortBy)
        [jsonObject setObject: self.sortBy forKey: @"sortBy"];
    if(self.sortOrder)
        [jsonObject setObject: self.sortOrder forKey: @"sortOrder"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.scope = [jsonObject objectForKey: @"scope"];
        if(self.scope && [self.scope isEqual:[NSNull null]])
            self.scope = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.sortBy = [jsonObject objectForKey: @"sortBy"];
        if(self.sortBy && [self.sortBy isEqual:[NSNull null]])
            self.sortBy = nil;

        self.sortOrder = [jsonObject objectForKey: @"sortOrder"];
        if(self.sortOrder && [self.sortOrder isEqual:[NSNull null]])
            self.sortOrder = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
