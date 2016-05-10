//
// EvhCreateOrganizationAccountCommand.m
//
#import "EvhCreateOrganizationAccountCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationAccountCommand
//

@implementation EvhCreateOrganizationAccountCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateOrganizationAccountCommand* obj = [EvhCreateOrganizationAccountCommand new];
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
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.accountPhone)
        [jsonObject setObject: self.accountPhone forKey: @"accountPhone"];
    if(self.accountName)
        [jsonObject setObject: self.accountName forKey: @"accountName"];
    if(self.assignmentId)
        [jsonObject setObject: self.assignmentId forKey: @"assignmentId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.accountPhone = [jsonObject objectForKey: @"accountPhone"];
        if(self.accountPhone && [self.accountPhone isEqual:[NSNull null]])
            self.accountPhone = nil;

        self.accountName = [jsonObject objectForKey: @"accountName"];
        if(self.accountName && [self.accountName isEqual:[NSNull null]])
            self.accountName = nil;

        self.assignmentId = [jsonObject objectForKey: @"assignmentId"];
        if(self.assignmentId && [self.assignmentId isEqual:[NSNull null]])
            self.assignmentId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
