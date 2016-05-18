//
// EvhUpdateFamilyInfoCommand.m
//
#import "EvhUpdateFamilyInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateFamilyInfoCommand
//

@implementation EvhUpdateFamilyInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateFamilyInfoCommand* obj = [EvhUpdateFamilyInfoCommand new];
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
    if(self.familyName)
        [jsonObject setObject: self.familyName forKey: @"familyName"];
    if(self.familyDescription)
        [jsonObject setObject: self.familyDescription forKey: @"familyDescription"];
    if(self.familyAvatarUri)
        [jsonObject setObject: self.familyAvatarUri forKey: @"familyAvatarUri"];
    if(self.familyAvatarUrl)
        [jsonObject setObject: self.familyAvatarUrl forKey: @"familyAvatarUrl"];
    if(self.memberNickName)
        [jsonObject setObject: self.memberNickName forKey: @"memberNickName"];
    if(self.memberAvatarUri)
        [jsonObject setObject: self.memberAvatarUri forKey: @"memberAvatarUri"];
    if(self.memberAvatarUrl)
        [jsonObject setObject: self.memberAvatarUrl forKey: @"memberAvatarUrl"];
    if(self.proofResourceUri)
        [jsonObject setObject: self.proofResourceUri forKey: @"proofResourceUri"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.familyName = [jsonObject objectForKey: @"familyName"];
        if(self.familyName && [self.familyName isEqual:[NSNull null]])
            self.familyName = nil;

        self.familyDescription = [jsonObject objectForKey: @"familyDescription"];
        if(self.familyDescription && [self.familyDescription isEqual:[NSNull null]])
            self.familyDescription = nil;

        self.familyAvatarUri = [jsonObject objectForKey: @"familyAvatarUri"];
        if(self.familyAvatarUri && [self.familyAvatarUri isEqual:[NSNull null]])
            self.familyAvatarUri = nil;

        self.familyAvatarUrl = [jsonObject objectForKey: @"familyAvatarUrl"];
        if(self.familyAvatarUrl && [self.familyAvatarUrl isEqual:[NSNull null]])
            self.familyAvatarUrl = nil;

        self.memberNickName = [jsonObject objectForKey: @"memberNickName"];
        if(self.memberNickName && [self.memberNickName isEqual:[NSNull null]])
            self.memberNickName = nil;

        self.memberAvatarUri = [jsonObject objectForKey: @"memberAvatarUri"];
        if(self.memberAvatarUri && [self.memberAvatarUri isEqual:[NSNull null]])
            self.memberAvatarUri = nil;

        self.memberAvatarUrl = [jsonObject objectForKey: @"memberAvatarUrl"];
        if(self.memberAvatarUrl && [self.memberAvatarUrl isEqual:[NSNull null]])
            self.memberAvatarUrl = nil;

        self.proofResourceUri = [jsonObject objectForKey: @"proofResourceUri"];
        if(self.proofResourceUri && [self.proofResourceUri isEqual:[NSNull null]])
            self.proofResourceUri = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
