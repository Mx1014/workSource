//
// EvhContactDTO.m
//
#import "EvhContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactDTO
//

@implementation EvhContactDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhContactDTO* obj = [EvhContactDTO new];
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
    if(self.contactPhone)
        [jsonObject setObject: self.contactPhone forKey: @"contactPhone"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.ehUsername)
        [jsonObject setObject: self.ehUsername forKey: @"ehUsername"];
    if(self.contactAvatar)
        [jsonObject setObject: self.contactAvatar forKey: @"contactAvatar"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.contactPhone = [jsonObject objectForKey: @"contactPhone"];
        if(self.contactPhone && [self.contactPhone isEqual:[NSNull null]])
            self.contactPhone = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.ehUsername = [jsonObject objectForKey: @"ehUsername"];
        if(self.ehUsername && [self.ehUsername isEqual:[NSNull null]])
            self.ehUsername = nil;

        self.contactAvatar = [jsonObject objectForKey: @"contactAvatar"];
        if(self.contactAvatar && [self.contactAvatar isEqual:[NSNull null]])
            self.contactAvatar = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
