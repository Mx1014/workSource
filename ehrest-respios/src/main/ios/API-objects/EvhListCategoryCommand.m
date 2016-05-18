//
// EvhListCategoryCommand.m
//
#import "EvhListCategoryCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCategoryCommand
//

@implementation EvhListCategoryCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListCategoryCommand* obj = [EvhListCategoryCommand new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.sortOrder)
        [jsonObject setObject: self.sortOrder forKey: @"sortOrder"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.sortOrder = [jsonObject objectForKey: @"sortOrder"];
        if(self.sortOrder && [self.sortOrder isEqual:[NSNull null]])
            self.sortOrder = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
