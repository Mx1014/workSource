//
// EvhFamilyMemberDTO.m
//
#import "EvhFamilyMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyMemberDTO
//

@implementation EvhFamilyMemberDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFamilyMemberDTO* obj = [EvhFamilyMemberDTO new];
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
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.memberUid)
        [jsonObject setObject: self.memberUid forKey: @"memberUid"];
    if(self.memberName)
        [jsonObject setObject: self.memberName forKey: @"memberName"];
    if(self.memberAvatarUri)
        [jsonObject setObject: self.memberAvatarUri forKey: @"memberAvatarUri"];
    if(self.memberAvatarUrl)
        [jsonObject setObject: self.memberAvatarUrl forKey: @"memberAvatarUrl"];
    if(self.cellPhone)
        [jsonObject setObject: self.cellPhone forKey: @"cellPhone"];
    if(self.statusLine)
        [jsonObject setObject: self.statusLine forKey: @"statusLine"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.birthday)
        [jsonObject setObject: self.birthday forKey: @"birthday"];
    if(self.occupation)
        [jsonObject setObject: self.occupation forKey: @"occupation"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.memberUid = [jsonObject objectForKey: @"memberUid"];
        if(self.memberUid && [self.memberUid isEqual:[NSNull null]])
            self.memberUid = nil;

        self.memberName = [jsonObject objectForKey: @"memberName"];
        if(self.memberName && [self.memberName isEqual:[NSNull null]])
            self.memberName = nil;

        self.memberAvatarUri = [jsonObject objectForKey: @"memberAvatarUri"];
        if(self.memberAvatarUri && [self.memberAvatarUri isEqual:[NSNull null]])
            self.memberAvatarUri = nil;

        self.memberAvatarUrl = [jsonObject objectForKey: @"memberAvatarUrl"];
        if(self.memberAvatarUrl && [self.memberAvatarUrl isEqual:[NSNull null]])
            self.memberAvatarUrl = nil;

        self.cellPhone = [jsonObject objectForKey: @"cellPhone"];
        if(self.cellPhone && [self.cellPhone isEqual:[NSNull null]])
            self.cellPhone = nil;

        self.statusLine = [jsonObject objectForKey: @"statusLine"];
        if(self.statusLine && [self.statusLine isEqual:[NSNull null]])
            self.statusLine = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        self.birthday = [jsonObject objectForKey: @"birthday"];
        if(self.birthday && [self.birthday isEqual:[NSNull null]])
            self.birthday = nil;

        self.occupation = [jsonObject objectForKey: @"occupation"];
        if(self.occupation && [self.occupation isEqual:[NSNull null]])
            self.occupation = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
