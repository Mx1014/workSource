//
// EvhProcessOrganizationTaskCommand.m
//
#import "EvhProcessOrganizationTaskCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhProcessOrganizationTaskCommand
//

@implementation EvhProcessOrganizationTaskCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhProcessOrganizationTaskCommand* obj = [EvhProcessOrganizationTaskCommand new];
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
    if(self.taskId)
        [jsonObject setObject: self.taskId forKey: @"taskId"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.taskStatus)
        [jsonObject setObject: self.taskStatus forKey: @"taskStatus"];
    if(self.taskCategory)
        [jsonObject setObject: self.taskCategory forKey: @"taskCategory"];
    if(self.privateFlag)
        [jsonObject setObject: self.privateFlag forKey: @"privateFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.taskId = [jsonObject objectForKey: @"taskId"];
        if(self.taskId && [self.taskId isEqual:[NSNull null]])
            self.taskId = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.taskStatus = [jsonObject objectForKey: @"taskStatus"];
        if(self.taskStatus && [self.taskStatus isEqual:[NSNull null]])
            self.taskStatus = nil;

        self.taskCategory = [jsonObject objectForKey: @"taskCategory"];
        if(self.taskCategory && [self.taskCategory isEqual:[NSNull null]])
            self.taskCategory = nil;

        self.privateFlag = [jsonObject objectForKey: @"privateFlag"];
        if(self.privateFlag && [self.privateFlag isEqual:[NSNull null]])
            self.privateFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
