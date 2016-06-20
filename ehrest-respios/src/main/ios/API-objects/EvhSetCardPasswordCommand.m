//
// EvhSetCardPasswordCommand.m
//
#import "EvhSetCardPasswordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetCardPasswordCommand
//

@implementation EvhSetCardPasswordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetCardPasswordCommand* obj = [EvhSetCardPasswordCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.oldPassword)
        [jsonObject setObject: self.oldPassword forKey: @"oldPassword"];
    if(self.theNewPassword)
        [jsonObject setObject: self.theNewPassword forKey: @"newPassword"];
    if(self.cardId)
        [jsonObject setObject: self.cardId forKey: @"cardId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.oldPassword = [jsonObject objectForKey: @"oldPassword"];
        if(self.oldPassword && [self.oldPassword isEqual:[NSNull null]])
            self.oldPassword = nil;

        self.theNewPassword = [jsonObject objectForKey: @"newPassword"];
        if(self.theNewPassword && [self.theNewPassword isEqual:[NSNull null]])
            self.theNewPassword = nil;

        self.cardId = [jsonObject objectForKey: @"cardId"];
        if(self.cardId && [self.cardId isEqual:[NSNull null]])
            self.cardId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
