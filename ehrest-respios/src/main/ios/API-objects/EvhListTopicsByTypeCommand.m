//
// EvhListTopicsByTypeCommand.m
//
#import "EvhListTopicsByTypeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTopicsByTypeCommand
//

@implementation EvhListTopicsByTypeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListTopicsByTypeCommand* obj = [EvhListTopicsByTypeCommand new];
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
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.taskType)
        [jsonObject setObject: self.taskType forKey: @"taskType"];
    if(self.taskStatus)
        [jsonObject setObject: self.taskStatus forKey: @"taskStatus"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.option)
        [jsonObject setObject: self.option forKey: @"option"];
    if(self.entrancePrivilege)
        [jsonObject setObject: self.entrancePrivilege forKey: @"entrancePrivilege"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.taskType = [jsonObject objectForKey: @"taskType"];
        if(self.taskType && [self.taskType isEqual:[NSNull null]])
            self.taskType = nil;

        self.taskStatus = [jsonObject objectForKey: @"taskStatus"];
        if(self.taskStatus && [self.taskStatus isEqual:[NSNull null]])
            self.taskStatus = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.option = [jsonObject objectForKey: @"option"];
        if(self.option && [self.option isEqual:[NSNull null]])
            self.option = nil;

        self.entrancePrivilege = [jsonObject objectForKey: @"entrancePrivilege"];
        if(self.entrancePrivilege && [self.entrancePrivilege isEqual:[NSNull null]])
            self.entrancePrivilege = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
