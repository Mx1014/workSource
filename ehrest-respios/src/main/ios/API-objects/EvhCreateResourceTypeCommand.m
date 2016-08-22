//
// EvhCreateResourceTypeCommand.m
//
#import "EvhCreateResourceTypeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateResourceTypeCommand
//

@implementation EvhCreateResourceTypeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateResourceTypeCommand* obj = [EvhCreateResourceTypeCommand new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.pageType)
        [jsonObject setObject: self.pageType forKey: @"pageType"];
    if(self.iconUri)
        [jsonObject setObject: self.iconUri forKey: @"iconUri"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.pageType = [jsonObject objectForKey: @"pageType"];
        if(self.pageType && [self.pageType isEqual:[NSNull null]])
            self.pageType = nil;

        self.iconUri = [jsonObject objectForKey: @"iconUri"];
        if(self.iconUri && [self.iconUri isEqual:[NSNull null]])
            self.iconUri = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
