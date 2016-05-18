//
// EvhOrganizationTaskDTO.m
//
#import "EvhOrganizationTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationTaskDTO
//

@implementation EvhOrganizationTaskDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationTaskDTO* obj = [EvhOrganizationTaskDTO new];
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
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.organizationType)
        [jsonObject setObject: self.organizationType forKey: @"organizationType"];
    if(self.applyEntityType)
        [jsonObject setObject: self.applyEntityType forKey: @"applyEntityType"];
    if(self.applyEntityId)
        [jsonObject setObject: self.applyEntityId forKey: @"applyEntityId"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.taskType)
        [jsonObject setObject: self.taskType forKey: @"taskType"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.taskStatus)
        [jsonObject setObject: self.taskStatus forKey: @"taskStatus"];
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.operateTime)
        [jsonObject setObject: self.operateTime forKey: @"operateTime"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.unprocessedTime)
        [jsonObject setObject: self.unprocessedTime forKey: @"unprocessedTime"];
    if(self.processingTime)
        [jsonObject setObject: self.processingTime forKey: @"processingTime"];
    if(self.processedTime)
        [jsonObject setObject: self.processedTime forKey: @"processedTime"];
    if(self.taskCategory)
        [jsonObject setObject: self.taskCategory forKey: @"taskCategory"];
    if(self.option)
        [jsonObject setObject: self.option forKey: @"option"];
    if(self.entrancePrivilege)
        [jsonObject setObject: self.entrancePrivilege forKey: @"entrancePrivilege"];
    if(self.targetName)
        [jsonObject setObject: self.targetName forKey: @"targetName"];
    if(self.targetToken)
        [jsonObject setObject: self.targetToken forKey: @"targetToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.organizationType = [jsonObject objectForKey: @"organizationType"];
        if(self.organizationType && [self.organizationType isEqual:[NSNull null]])
            self.organizationType = nil;

        self.applyEntityType = [jsonObject objectForKey: @"applyEntityType"];
        if(self.applyEntityType && [self.applyEntityType isEqual:[NSNull null]])
            self.applyEntityType = nil;

        self.applyEntityId = [jsonObject objectForKey: @"applyEntityId"];
        if(self.applyEntityId && [self.applyEntityId isEqual:[NSNull null]])
            self.applyEntityId = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.taskType = [jsonObject objectForKey: @"taskType"];
        if(self.taskType && [self.taskType isEqual:[NSNull null]])
            self.taskType = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.taskStatus = [jsonObject objectForKey: @"taskStatus"];
        if(self.taskStatus && [self.taskStatus isEqual:[NSNull null]])
            self.taskStatus = nil;

        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.operateTime = [jsonObject objectForKey: @"operateTime"];
        if(self.operateTime && [self.operateTime isEqual:[NSNull null]])
            self.operateTime = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.unprocessedTime = [jsonObject objectForKey: @"unprocessedTime"];
        if(self.unprocessedTime && [self.unprocessedTime isEqual:[NSNull null]])
            self.unprocessedTime = nil;

        self.processingTime = [jsonObject objectForKey: @"processingTime"];
        if(self.processingTime && [self.processingTime isEqual:[NSNull null]])
            self.processingTime = nil;

        self.processedTime = [jsonObject objectForKey: @"processedTime"];
        if(self.processedTime && [self.processedTime isEqual:[NSNull null]])
            self.processedTime = nil;

        self.taskCategory = [jsonObject objectForKey: @"taskCategory"];
        if(self.taskCategory && [self.taskCategory isEqual:[NSNull null]])
            self.taskCategory = nil;

        self.option = [jsonObject objectForKey: @"option"];
        if(self.option && [self.option isEqual:[NSNull null]])
            self.option = nil;

        self.entrancePrivilege = [jsonObject objectForKey: @"entrancePrivilege"];
        if(self.entrancePrivilege && [self.entrancePrivilege isEqual:[NSNull null]])
            self.entrancePrivilege = nil;

        self.targetName = [jsonObject objectForKey: @"targetName"];
        if(self.targetName && [self.targetName isEqual:[NSNull null]])
            self.targetName = nil;

        self.targetToken = [jsonObject objectForKey: @"targetToken"];
        if(self.targetToken && [self.targetToken isEqual:[NSNull null]])
            self.targetToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
