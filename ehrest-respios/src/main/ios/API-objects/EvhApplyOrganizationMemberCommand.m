//
// EvhApplyOrganizationMemberCommand.m
//
#import "EvhApplyOrganizationMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplyOrganizationMemberCommand
//

@implementation EvhApplyOrganizationMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApplyOrganizationMemberCommand* obj = [EvhApplyOrganizationMemberCommand new];
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
    if(self.organizationType)
        [jsonObject setObject: self.organizationType forKey: @"organizationType"];
    if(self.contactDescription)
        [jsonObject setObject: self.contactDescription forKey: @"contactDescription"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.organizationType = [jsonObject objectForKey: @"organizationType"];
        if(self.organizationType && [self.organizationType isEqual:[NSNull null]])
            self.organizationType = nil;

        self.contactDescription = [jsonObject objectForKey: @"contactDescription"];
        if(self.contactDescription && [self.contactDescription isEqual:[NSNull null]])
            self.contactDescription = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
