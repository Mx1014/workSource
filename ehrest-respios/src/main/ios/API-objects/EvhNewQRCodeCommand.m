//
// EvhNewQRCodeCommand.m
//
#import "EvhNewQRCodeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewQRCodeCommand
//

@implementation EvhNewQRCodeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNewQRCodeCommand* obj = [EvhNewQRCodeCommand new];
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
    if(self.logoUri)
        [jsonObject setObject: self.logoUri forKey: @"logoUri"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.expireSeconds)
        [jsonObject setObject: self.expireSeconds forKey: @"expireSeconds"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.logoUri = [jsonObject objectForKey: @"logoUri"];
        if(self.logoUri && [self.logoUri isEqual:[NSNull null]])
            self.logoUri = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.expireSeconds = [jsonObject objectForKey: @"expireSeconds"];
        if(self.expireSeconds && [self.expireSeconds isEqual:[NSNull null]])
            self.expireSeconds = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
