//
// EvhCreatePropMemberCommand.m
//
#import "EvhCreatePropMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatePropMemberCommand
//

@implementation EvhCreatePropMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreatePropMemberCommand* obj = [EvhCreatePropMemberCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.memberGroup)
        [jsonObject setObject: self.memberGroup forKey: @"memberGroup"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactType)
        [jsonObject setObject: self.contactType forKey: @"contactType"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.contactDescription)
        [jsonObject setObject: self.contactDescription forKey: @"contactDescription"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.memberGroup = [jsonObject objectForKey: @"memberGroup"];
        if(self.memberGroup && [self.memberGroup isEqual:[NSNull null]])
            self.memberGroup = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.contactType = [jsonObject objectForKey: @"contactType"];
        if(self.contactType && [self.contactType isEqual:[NSNull null]])
            self.contactType = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.contactDescription = [jsonObject objectForKey: @"contactDescription"];
        if(self.contactDescription && [self.contactDescription isEqual:[NSNull null]])
            self.contactDescription = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
