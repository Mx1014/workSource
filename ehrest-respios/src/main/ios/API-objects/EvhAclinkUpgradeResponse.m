//
// EvhAclinkUpgradeResponse.m
//
#import "EvhAclinkUpgradeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUpgradeResponse
//

@implementation EvhAclinkUpgradeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkUpgradeResponse* obj = [EvhAclinkUpgradeResponse new];
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
    if(self.infoUrl)
        [jsonObject setObject: self.infoUrl forKey: @"infoUrl"];
    if(self.downloadUrl)
        [jsonObject setObject: self.downloadUrl forKey: @"downloadUrl"];
    if(self.creatorId)
        [jsonObject setObject: self.creatorId forKey: @"creatorId"];
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
    if(self.version)
        [jsonObject setObject: self.version forKey: @"version"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.infoUrl = [jsonObject objectForKey: @"infoUrl"];
        if(self.infoUrl && [self.infoUrl isEqual:[NSNull null]])
            self.infoUrl = nil;

        self.downloadUrl = [jsonObject objectForKey: @"downloadUrl"];
        if(self.downloadUrl && [self.downloadUrl isEqual:[NSNull null]])
            self.downloadUrl = nil;

        self.creatorId = [jsonObject objectForKey: @"creatorId"];
        if(self.creatorId && [self.creatorId isEqual:[NSNull null]])
            self.creatorId = nil;

        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        self.version = [jsonObject objectForKey: @"version"];
        if(self.version && [self.version isEqual:[NSNull null]])
            self.version = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
