//
// EvhUpdatePersistServerCommand.m
//
#import "EvhUpdatePersistServerCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePersistServerCommand
//

@implementation EvhUpdatePersistServerCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdatePersistServerCommand* obj = [EvhUpdatePersistServerCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.masterId)
        [jsonObject setObject: self.masterId forKey: @"masterId"];
    if(self.addressUri)
        [jsonObject setObject: self.addressUri forKey: @"addressUri"];
    if(self.addressPort)
        [jsonObject setObject: self.addressPort forKey: @"addressPort"];
    if(self.serverType)
        [jsonObject setObject: self.serverType forKey: @"serverType"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.configTag)
        [jsonObject setObject: self.configTag forKey: @"configTag"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.masterId = [jsonObject objectForKey: @"masterId"];
        if(self.masterId && [self.masterId isEqual:[NSNull null]])
            self.masterId = nil;

        self.addressUri = [jsonObject objectForKey: @"addressUri"];
        if(self.addressUri && [self.addressUri isEqual:[NSNull null]])
            self.addressUri = nil;

        self.addressPort = [jsonObject objectForKey: @"addressPort"];
        if(self.addressPort && [self.addressPort isEqual:[NSNull null]])
            self.addressPort = nil;

        self.serverType = [jsonObject objectForKey: @"serverType"];
        if(self.serverType && [self.serverType isEqual:[NSNull null]])
            self.serverType = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.configTag = [jsonObject objectForKey: @"configTag"];
        if(self.configTag && [self.configTag isEqual:[NSNull null]])
            self.configTag = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
