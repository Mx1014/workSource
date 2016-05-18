//
// EvhQualityInspectionTaskDTO.m
//
#import "EvhQualityInspectionTaskDTO.h"
#import "EvhQualityInspectionTaskRecordsDTO.h"
#import "EvhGroupUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskDTO
//

@implementation EvhQualityInspectionTaskDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQualityInspectionTaskDTO* obj = [EvhQualityInspectionTaskDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _groupUsers = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.standardId)
        [jsonObject setObject: self.standardId forKey: @"standardId"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.taskName)
        [jsonObject setObject: self.taskName forKey: @"taskName"];
    if(self.taskNumber)
        [jsonObject setObject: self.taskNumber forKey: @"taskNumber"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.categoryName)
        [jsonObject setObject: self.categoryName forKey: @"categoryName"];
    if(self.executiveGroupId)
        [jsonObject setObject: self.executiveGroupId forKey: @"executiveGroupId"];
    if(self.executorId)
        [jsonObject setObject: self.executorId forKey: @"executorId"];
    if(self.executorName)
        [jsonObject setObject: self.executorName forKey: @"executorName"];
    if(self.operatorId)
        [jsonObject setObject: self.operatorId forKey: @"operatorId"];
    if(self.operatorName)
        [jsonObject setObject: self.operatorName forKey: @"operatorName"];
    if(self.executiveStartTime)
        [jsonObject setObject: self.executiveStartTime forKey: @"executiveStartTime"];
    if(self.executiveExpireTime)
        [jsonObject setObject: self.executiveExpireTime forKey: @"executiveExpireTime"];
    if(self.processExpireTime)
        [jsonObject setObject: self.processExpireTime forKey: @"processExpireTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.result)
        [jsonObject setObject: self.result forKey: @"result"];
    if(self.processResult)
        [jsonObject setObject: self.processResult forKey: @"processResult"];
    if(self.reviewResult)
        [jsonObject setObject: self.reviewResult forKey: @"reviewResult"];
    if(self.reviewerId)
        [jsonObject setObject: self.reviewerId forKey: @"reviewerId"];
    if(self.reviewerName)
        [jsonObject setObject: self.reviewerName forKey: @"reviewerName"];
    if(self.record) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.record toJson: dic];
        
        [jsonObject setObject: dic forKey: @"record"];
    }
    if(self.groupUsers) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhGroupUserDTO* item in self.groupUsers) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"groupUsers"];
    }
    if(self.taskFlag)
        [jsonObject setObject: self.taskFlag forKey: @"taskFlag"];
    if(self.standardDescription)
        [jsonObject setObject: self.standardDescription forKey: @"standardDescription"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.standardId = [jsonObject objectForKey: @"standardId"];
        if(self.standardId && [self.standardId isEqual:[NSNull null]])
            self.standardId = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.taskName = [jsonObject objectForKey: @"taskName"];
        if(self.taskName && [self.taskName isEqual:[NSNull null]])
            self.taskName = nil;

        self.taskNumber = [jsonObject objectForKey: @"taskNumber"];
        if(self.taskNumber && [self.taskNumber isEqual:[NSNull null]])
            self.taskNumber = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.categoryName = [jsonObject objectForKey: @"categoryName"];
        if(self.categoryName && [self.categoryName isEqual:[NSNull null]])
            self.categoryName = nil;

        self.executiveGroupId = [jsonObject objectForKey: @"executiveGroupId"];
        if(self.executiveGroupId && [self.executiveGroupId isEqual:[NSNull null]])
            self.executiveGroupId = nil;

        self.executorId = [jsonObject objectForKey: @"executorId"];
        if(self.executorId && [self.executorId isEqual:[NSNull null]])
            self.executorId = nil;

        self.executorName = [jsonObject objectForKey: @"executorName"];
        if(self.executorName && [self.executorName isEqual:[NSNull null]])
            self.executorName = nil;

        self.operatorId = [jsonObject objectForKey: @"operatorId"];
        if(self.operatorId && [self.operatorId isEqual:[NSNull null]])
            self.operatorId = nil;

        self.operatorName = [jsonObject objectForKey: @"operatorName"];
        if(self.operatorName && [self.operatorName isEqual:[NSNull null]])
            self.operatorName = nil;

        self.executiveStartTime = [jsonObject objectForKey: @"executiveStartTime"];
        if(self.executiveStartTime && [self.executiveStartTime isEqual:[NSNull null]])
            self.executiveStartTime = nil;

        self.executiveExpireTime = [jsonObject objectForKey: @"executiveExpireTime"];
        if(self.executiveExpireTime && [self.executiveExpireTime isEqual:[NSNull null]])
            self.executiveExpireTime = nil;

        self.processExpireTime = [jsonObject objectForKey: @"processExpireTime"];
        if(self.processExpireTime && [self.processExpireTime isEqual:[NSNull null]])
            self.processExpireTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.result = [jsonObject objectForKey: @"result"];
        if(self.result && [self.result isEqual:[NSNull null]])
            self.result = nil;

        self.processResult = [jsonObject objectForKey: @"processResult"];
        if(self.processResult && [self.processResult isEqual:[NSNull null]])
            self.processResult = nil;

        self.reviewResult = [jsonObject objectForKey: @"reviewResult"];
        if(self.reviewResult && [self.reviewResult isEqual:[NSNull null]])
            self.reviewResult = nil;

        self.reviewerId = [jsonObject objectForKey: @"reviewerId"];
        if(self.reviewerId && [self.reviewerId isEqual:[NSNull null]])
            self.reviewerId = nil;

        self.reviewerName = [jsonObject objectForKey: @"reviewerName"];
        if(self.reviewerName && [self.reviewerName isEqual:[NSNull null]])
            self.reviewerName = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"record"];

        self.record = [EvhQualityInspectionTaskRecordsDTO new];
        self.record = [self.record fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"groupUsers"];
            for(id itemJson in jsonArray) {
                EvhGroupUserDTO* item = [EvhGroupUserDTO new];
                
                [item fromJson: itemJson];
                [self.groupUsers addObject: item];
            }
        }
        self.taskFlag = [jsonObject objectForKey: @"taskFlag"];
        if(self.taskFlag && [self.taskFlag isEqual:[NSNull null]])
            self.taskFlag = nil;

        self.standardDescription = [jsonObject objectForKey: @"standardDescription"];
        if(self.standardDescription && [self.standardDescription isEqual:[NSNull null]])
            self.standardDescription = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
