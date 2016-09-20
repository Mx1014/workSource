//
// EvhSetUserInfoCommand.m
//
#import "EvhSetUserInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetUserInfoCommand
//

@implementation EvhSetUserInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetUserInfoCommand* obj = [EvhSetUserInfoCommand new];
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
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.statusLine)
        [jsonObject setObject: self.statusLine forKey: @"statusLine"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.birthday)
        [jsonObject setObject: self.birthday forKey: @"birthday"];
    if(self.homeTown)
        [jsonObject setObject: self.homeTown forKey: @"homeTown"];
    if(self.company)
        [jsonObject setObject: self.company forKey: @"company"];
    if(self.school)
        [jsonObject setObject: self.school forKey: @"school"];
    if(self.occupation)
        [jsonObject setObject: self.occupation forKey: @"occupation"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.statusLine = [jsonObject objectForKey: @"statusLine"];
        if(self.statusLine && [self.statusLine isEqual:[NSNull null]])
            self.statusLine = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        self.birthday = [jsonObject objectForKey: @"birthday"];
        if(self.birthday && [self.birthday isEqual:[NSNull null]])
            self.birthday = nil;

        self.homeTown = [jsonObject objectForKey: @"homeTown"];
        if(self.homeTown && [self.homeTown isEqual:[NSNull null]])
            self.homeTown = nil;

        self.company = [jsonObject objectForKey: @"company"];
        if(self.company && [self.company isEqual:[NSNull null]])
            self.company = nil;

        self.school = [jsonObject objectForKey: @"school"];
        if(self.school && [self.school isEqual:[NSNull null]])
            self.school = nil;

        self.occupation = [jsonObject objectForKey: @"occupation"];
        if(self.occupation && [self.occupation isEqual:[NSNull null]])
            self.occupation = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
