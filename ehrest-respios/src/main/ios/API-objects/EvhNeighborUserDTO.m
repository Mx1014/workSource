//
// EvhNeighborUserDTO.m
//
#import "EvhNeighborUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNeighborUserDTO
//

@implementation EvhNeighborUserDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNeighborUserDTO* obj = [EvhNeighborUserDTO new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userAvatarUri)
        [jsonObject setObject: self.userAvatarUri forKey: @"userAvatarUri"];
    if(self.userAvatarUrl)
        [jsonObject setObject: self.userAvatarUrl forKey: @"userAvatarUrl"];
    if(self.userStatusLine)
        [jsonObject setObject: self.userStatusLine forKey: @"userStatusLine"];
    if(self.neighborhoodRelation)
        [jsonObject setObject: self.neighborhoodRelation forKey: @"neighborhoodRelation"];
    if(self.distance)
        [jsonObject setObject: self.distance forKey: @"distance"];
    if(self.occupation)
        [jsonObject setObject: self.occupation forKey: @"occupation"];
    if(self.initial)
        [jsonObject setObject: self.initial forKey: @"initial"];
    if(self.fullPinyin)
        [jsonObject setObject: self.fullPinyin forKey: @"fullPinyin"];
    if(self.fullInitial)
        [jsonObject setObject: self.fullInitial forKey: @"fullInitial"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userAvatarUri = [jsonObject objectForKey: @"userAvatarUri"];
        if(self.userAvatarUri && [self.userAvatarUri isEqual:[NSNull null]])
            self.userAvatarUri = nil;

        self.userAvatarUrl = [jsonObject objectForKey: @"userAvatarUrl"];
        if(self.userAvatarUrl && [self.userAvatarUrl isEqual:[NSNull null]])
            self.userAvatarUrl = nil;

        self.userStatusLine = [jsonObject objectForKey: @"userStatusLine"];
        if(self.userStatusLine && [self.userStatusLine isEqual:[NSNull null]])
            self.userStatusLine = nil;

        self.neighborhoodRelation = [jsonObject objectForKey: @"neighborhoodRelation"];
        if(self.neighborhoodRelation && [self.neighborhoodRelation isEqual:[NSNull null]])
            self.neighborhoodRelation = nil;

        self.distance = [jsonObject objectForKey: @"distance"];
        if(self.distance && [self.distance isEqual:[NSNull null]])
            self.distance = nil;

        self.occupation = [jsonObject objectForKey: @"occupation"];
        if(self.occupation && [self.occupation isEqual:[NSNull null]])
            self.occupation = nil;

        self.initial = [jsonObject objectForKey: @"initial"];
        if(self.initial && [self.initial isEqual:[NSNull null]])
            self.initial = nil;

        self.fullPinyin = [jsonObject objectForKey: @"fullPinyin"];
        if(self.fullPinyin && [self.fullPinyin isEqual:[NSNull null]])
            self.fullPinyin = nil;

        self.fullInitial = [jsonObject objectForKey: @"fullInitial"];
        if(self.fullInitial && [self.fullInitial isEqual:[NSNull null]])
            self.fullInitial = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
