//
// EvhDeleteOrganizationOwnerCommand.m
//
#import "EvhDeleteOrganizationOwnerCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteOrganizationOwnerCommand
//

@implementation EvhDeleteOrganizationOwnerCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteOrganizationOwnerCommand* obj = [EvhDeleteOrganizationOwnerCommand new];
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
    if(self.contactType)
        [jsonObject setObject: self.contactType forKey: @"contactType"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.apartmentId)
        [jsonObject setObject: self.apartmentId forKey: @"apartmentId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contactType = [jsonObject objectForKey: @"contactType"];
        if(self.contactType && [self.contactType isEqual:[NSNull null]])
            self.contactType = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.apartmentId = [jsonObject objectForKey: @"apartmentId"];
        if(self.apartmentId && [self.apartmentId isEqual:[NSNull null]])
            self.apartmentId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
