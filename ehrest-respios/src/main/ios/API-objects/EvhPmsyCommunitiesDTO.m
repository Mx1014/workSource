//
// EvhPmsyCommunitiesDTO.m
// generated at 2016-04-29 18:56:00 
//
#import "EvhPmsyCommunitiesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyCommunitiesDTO
//

@implementation EvhPmsyCommunitiesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmsyCommunitiesDTO* obj = [EvhPmsyCommunitiesDTO new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.billTip)
        [jsonObject setObject: self.billTip forKey: @"billTip"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.billTip = [jsonObject objectForKey: @"billTip"];
        if(self.billTip && [self.billTip isEqual:[NSNull null]])
            self.billTip = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
